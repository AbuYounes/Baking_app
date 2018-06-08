package com.example.android.mybakingapp.ui.step_content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mybakingapp.ItemClickSupport;
import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.adapter.RecipeStepAdapter;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.ui.ingredient_content.IngredientActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.mybakingapp.util.Constants.EXTRA_INGREDIENT;
import static com.example.android.mybakingapp.util.Constants.EXTRA_RECIPE;
import static com.example.android.mybakingapp.util.Constants.EXTRA_RECIPE_NAME;


public class StepFragment extends Fragment {
    public interface StepFragmentContract {
        void onStepSelected(int position);
    }


    private static final String LOG_TAG = StepFragment.class.getName();
    private Recipe mRecipe;

    @BindView(R.id.recyclerview_recipes_steps)
    RecyclerView mRecyclerViewSteps;
    @BindView(R.id.card_view_3)
    CardView mCardView;

    public StepFragment() {

    }

    public static StepFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    StepFragmentContract stepCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            stepCallback = (StepFragmentContract) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "oncreateview Stepfragment");

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        RecipeStepAdapter adapter = new RecipeStepAdapter(getActivity(), mRecipe.steps);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSteps.setLayoutManager(mLayoutManager);
        mRecyclerViewSteps.setAdapter(adapter);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingredientIntent = new Intent(getActivity(), IngredientActivity.class);
                ingredientIntent.putParcelableArrayListExtra(EXTRA_INGREDIENT, (ArrayList<? extends Parcelable>) mRecipe.ingredients);
                ingredientIntent.putExtra(EXTRA_RECIPE_NAME, mRecipe.recipeName);
                startActivity(ingredientIntent);
            }
        });

        Log.d(LOG_TAG, "onCreateView executed");
        ItemClickSupport.addTo(mRecyclerViewSteps).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                stepCallback.onStepSelected(position);
            }
        });
        return rootView;
    }


}


