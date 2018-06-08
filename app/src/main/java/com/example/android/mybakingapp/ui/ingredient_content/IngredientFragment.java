package com.example.android.mybakingapp.ui.ingredient_content;

import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.mybakingapp.util.Constants.EXTRA_INGREDIENT;
import static com.example.android.mybakingapp.util.Constants.EXTRA_RECIPE_NAME;

public class IngredientFragment extends Fragment {

    @BindView(R.id.recyclerview_recipes_ingredientes)
    RecyclerView recyclerViewIngredients;

    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance(List<Ingredient> ingredients, String recipeName) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_INGREDIENT, (ArrayList<Ingredient>) ingredients);
        args.putString(EXTRA_RECIPE_NAME, recipeName);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, rootView);
        List<Ingredient> ingredients = getArguments().getParcelableArrayList(EXTRA_INGREDIENT);
        String recipeName = getArguments().getString(EXTRA_RECIPE_NAME);
        RecipeIngredientAdapter adapter = new RecipeIngredientAdapter(ingredients, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewIngredients.setLayoutManager(layoutManager);
        recyclerViewIngredients.setAdapter(adapter);




        final ArrayList<String> recipeIngredientForWidgets = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {

            String ingredientName = ingredients.get(i).getIngredient();
            float quantity = ingredients.get(i).getQuantity();
            String measure = ingredients.get(i).getMeasure();

            //recipeIngredientForWidgets.add(ingredientName + "\n" + "Quantity: " + quantity + "\n" + "Measure: " + measure + "\n");
            recipeIngredientForWidgets.add(ingredientName + "\n" + quantity +" "+ measure + "\n");
        }

        MyIngredientService.startIngredientService(getActivity(), recipeIngredientForWidgets, recipeName);
        // Inflate the layout for this fragment
        return rootView;
    }
}
