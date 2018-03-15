package com.example.android.mybakingapp.ui.recipe_content;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.mybakingapp.ItemClickSupport;
import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.adapter.RecipeMenuAdapter;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.ui.step_content.RecipeStepActivity;
import com.example.android.mybakingapp.util.Client;
import com.example.android.mybakingapp.util.CommonUtils;
import com.example.android.mybakingapp.util.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    private static final String LOG_TAG = RecipeFragment.class.getName();
    private static final String EXTRA_RECIPE_NAME = "recipe name";
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipeMenuAdapter mAdapter;
    private RecyclerView mRecyclerViewRecipes;
    Call<List<Recipe>> mRecipeCall;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerViewRecipes = view.findViewById(R.id.recyclerview_recipes);
        mAdapter = new RecipeMenuAdapter(getContext(), mRecipes);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerViewRecipes.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        } else {
            mRecyclerViewRecipes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        mRecyclerViewRecipes.setAdapter(mAdapter);
        Log.d(LOG_TAG, "onCreateView executed");
        ItemClickSupport.addTo(mRecyclerViewRecipes).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Recipe clickedDataItem = mRecipes.get(position);
                Intent intent = new Intent(getActivity(), RecipeStepActivity.class);
                intent.putExtra("recipe", clickedDataItem);
                Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.recipeName, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        getRecipes();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecipeCall != null) {
            mRecipeCall.cancel();
        }
    }
    //todo networking should be in activities rather than fragments
    private void getRecipes() {
        Service apiService =
                Client.getClient().create(Service.class);
        mRecipeCall = apiService.getDetails();
        mRecipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipes.addAll(response.body());
                    //Log.d(LOG_TAG, "" + mRecipes.addAll(response.body()));
                    mAdapter.notifyDataSetChanged();

                } else {
                    CommonUtils apiError = CommonUtils.parseError(response);
                    Toast.makeText(getActivity(), apiError.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
