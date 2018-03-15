package com.example.android.mybakingapp.ui.ingredient_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Ingredient;

import java.util.List;

public class IngredientActivity extends AppCompatActivity {

    List<Ingredient> mIngredientList;
    private static final String TAG_FRAGMENT_INGREDIENT = "ingredient fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        mIngredientList = getIntent().getParcelableArrayListExtra(IngredientFragment.EXTRA_INGREDIENT);
        IngredientFragment ingredientFragment = IngredientFragment.newInstance(mIngredientList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_INGREDIENT) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.recipe_ingredient, ingredientFragment, TAG_FRAGMENT_INGREDIENT);
            transaction.commit();
        }
    }
}
