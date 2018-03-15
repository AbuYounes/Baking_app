package com.example.android.mybakingapp.ui.step_content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.example.android.mybakingapp.ui.ingredient_content.IngredientActivity;
import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Ingredient;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.ui.ingredient_content.IngredientFragment;

import java.util.ArrayList;

public class RecipeStepActivity extends AppCompatActivity {
    private static final String LOG_TAG = RecipeStepActivity.class.getName();
    private static final String TAG_FRAGMENT_STEP = "recipe step";
    private CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        final Recipe recipe = getIntent().getParcelableExtra("recipe");
        StepFragment frag = StepFragment.newInstance(recipe.steps);
        FragmentManager manager = getSupportFragmentManager();

        if(manager.findFragmentByTag(TAG_FRAGMENT_STEP)==null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.recipe_step, frag, TAG_FRAGMENT_STEP);
            transaction.commit();
        }
        Log.d(LOG_TAG, "oncreate recipestepactivity");

        mCardView = findViewById(R.id.card_view_3);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingredientIntent = new Intent(RecipeStepActivity.this, IngredientActivity.class);
                ingredientIntent.putParcelableArrayListExtra(IngredientFragment.EXTRA_INGREDIENT, (ArrayList<Ingredient>) recipe.ingredients);
                startActivity(ingredientIntent);
            }
        });

    }


}
