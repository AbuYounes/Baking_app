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
import android.widget.Toast;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.RecipesIdlingResources;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.util.Client;
import com.example.android.mybakingapp.util.CommonUtils;
import com.example.android.mybakingapp.util.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    Call<List<Recipe>> mRecipeCall;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar menuToolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));



        RecipeFragment frag = RecipeFragment.newInstance((ArrayList<Recipe>) mRecipes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.recipe_view, frag, TAG_FRAGMENT);
            transaction.commit();
        }

        Log.d(LOG_TAG, "onCreate executed");
        getRecipes();

        getIdlingResource();
    }

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

                } else {
                    CommonUtils apiError = CommonUtils.parseError(response);
                    Toast.makeText(RecipeActivity.this, apiError.getMessage(),
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
