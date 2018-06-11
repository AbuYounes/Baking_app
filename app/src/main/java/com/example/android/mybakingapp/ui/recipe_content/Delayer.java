package com.example.android.mybakingapp.ui.recipe_content;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.android.mybakingapp.data.RecipesIdlingResources;
import com.example.android.mybakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class Delayer {
    private static final int DELAY_MILLIS = 3000;

    final static List<Recipe> mRecipes = new ArrayList<>();

    interface DelayerCallback {
        void onDone(List<Recipe> recipes);
    }

    /**
     * Takes a String and returns it after {@link #DELAY_MILLIS} via a {@link DelayerCallback}.
     * @param callback used to notify the caller asynchronously
     */
    static void processDelay(Context context, final DelayerCallback callback,
                             @Nullable final RecipesIdlingResources idlingResource) {
        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        // Delay the execution, return message via callback.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(mRecipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}
