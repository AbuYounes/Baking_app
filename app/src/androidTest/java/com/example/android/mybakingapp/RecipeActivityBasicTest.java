package com.example.android.mybakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.mybakingapp.ui.recipe_content.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityBasicTest {
    @Rule public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void onRecipeListActivityOpen_displayRecyclerView(){

        /* Check that the Recycler View is  displayed
        https://developer.android.com/training/testing/espresso/lists.html
        Interacting with recycler view list items
         */
        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }
    @Test
    public void clickRecyclerview_OpensStepView(){
        onView((withId(R.id.recyclerview_recipes)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.ingredient))
                .check(matches(isDisplayed()));

    }
}
