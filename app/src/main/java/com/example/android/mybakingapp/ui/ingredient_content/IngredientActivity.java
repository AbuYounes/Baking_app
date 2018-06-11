package com.example.android.mybakingapp.ui.ingredient_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);
        setSupportActionBar(mMenuToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
