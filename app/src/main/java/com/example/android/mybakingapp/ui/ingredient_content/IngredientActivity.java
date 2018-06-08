package com.example.android.mybakingapp.ui.ingredient_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Ingredient;

import java.util.List;

import butterknife.BindView;

import static com.example.android.mybakingapp.util.Constants.EXTRA_INGREDIENT;
import static com.example.android.mybakingapp.util.Constants.EXTRA_RECIPE_NAME;
import static com.example.android.mybakingapp.util.Constants.TAG_FRAGMENT_INGREDIENT;

public class IngredientActivity extends AppCompatActivity {



    @BindView(R.id.menu_toolbar_ingredient)
    Toolbar mMenuToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        setSupportActionBar(mMenuToolbar);


        List<Ingredient> ingredientList = getIntent().getParcelableArrayListExtra(EXTRA_INGREDIENT);
        String recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        IngredientFragment ingredientFragment = IngredientFragment.newInstance(ingredientList, recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_INGREDIENT) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.recipe_ingredient, ingredientFragment, TAG_FRAGMENT_INGREDIENT);
            transaction.commit();
        }
    }
}
