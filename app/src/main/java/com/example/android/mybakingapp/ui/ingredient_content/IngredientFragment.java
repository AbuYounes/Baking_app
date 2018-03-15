package com.example.android.mybakingapp.ui.ingredient_content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.adapter.RecipeIngredientAdapter;
import com.example.android.mybakingapp.data.model.Ingredient;
import com.example.android.mybakingapp.widgetRecipe.MyIngredientService;

import java.util.ArrayList;
import java.util.List;

public class IngredientFragment extends Fragment {

    private RecyclerView mRecyclerViewIngredients;
    private RecipeIngredientAdapter mAdapter;
    private List<Ingredient> mIngredients;

    public static final String EXTRA_INGREDIENT = "ingredients";

    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance(List<Ingredient> ingredients) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_INGREDIENT, (ArrayList<Ingredient>) ingredients);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mIngredients = getArguments().getParcelableArrayList(EXTRA_INGREDIENT);
        mRecyclerViewIngredients = view.findViewById(R.id.recyclerview_recipes_ingredientes);
        mAdapter = new RecipeIngredientAdapter(mIngredients, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewIngredients.setLayoutManager(mLayoutManager);
        mRecyclerViewIngredients.setAdapter(mAdapter);

        final ArrayList<String> recipeIngredientForWidgets = new ArrayList<>();

        for (int i = 0; i < mIngredients.size(); i++) {

            String ingredientName = mIngredients.get(i).getIngredient();
            float quantity = mIngredients.get(i).getQuantity();
            String measure = mIngredients.get(i).getMeasure();

            recipeIngredientForWidgets.add(ingredientName + "\n" + "Quantity: " + quantity + "\n" + "Measure: " + measure + "\n");
        }

        MyIngredientService.startIngredientService(getContext(), recipeIngredientForWidgets);

    }
}
