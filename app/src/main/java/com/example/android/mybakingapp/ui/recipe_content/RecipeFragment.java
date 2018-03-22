package com.example.android.mybakingapp.ui.recipe_content;


import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.example.android.mybakingapp.ui.step_content.RecipeStepActivity.RECIPE;

public class RecipeFragment extends Fragment {

    private static final String LOG_TAG = RecipeFragment.class.getName();
    private static final String EXTRA_RECIPES = "extra recipe";
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipeMenuAdapter mAdapter;
    private RecyclerView mRecyclerViewRecipes;
    Call<List<Recipe>> mRecipeCall;

    public RecipeFragment() {
    }

    public static RecipeFragment newInstance(ArrayList<Recipe> recipes) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_RECIPES, recipes);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecipes = getArguments().getParcelableArrayList(EXTRA_RECIPES);
        mRecyclerViewRecipes = rootView.findViewById(R.id.recyclerview_recipes);
        mAdapter = new RecipeMenuAdapter(getContext(), mRecipes);


        final int columns = getResources().getInteger(R.integer.recipe_columns);
        mRecyclerViewRecipes.setLayoutManager(new GridLayoutManager(getActivity(), columns));
        mRecyclerViewRecipes.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Log.d(LOG_TAG, "onCreateView executed");
        ItemClickSupport.addTo(mRecyclerViewRecipes).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Recipe clickedDataItem = mRecipes.get(position);
                Intent intent = new Intent(getActivity(), RecipeStepActivity.class);
                intent.putExtra(RECIPE, clickedDataItem);
                Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.recipeName, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        return rootView;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mRecipeCall != null) {
            mRecipeCall.cancel();
        }
    }
}
