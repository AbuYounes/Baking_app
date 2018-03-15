package com.example.android.mybakingapp.ui.recipe_content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.RecipesIdlingResources;


public class RecipeActivity extends AppCompatActivity {

    @Nullable
    private RecipesIdlingResources mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link RecipesIdlingResources}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResources();
        }
        return mIdlingResource;
    }

    private static final String LOG_TAG = RecipeActivity.class.getName();
    public static final String TAG_FRAGMENT = "FaridsRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar menuToolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

        RecipeFragment frag = new RecipeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.recipe_view, frag, TAG_FRAGMENT);
            transaction.commit();
        }
        //todo check if fragment is already added to fragment manager before adding.
        Log.d(LOG_TAG, "onCreate executed");

        getIdlingResource();
    }
}
