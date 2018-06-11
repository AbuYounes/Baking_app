package com.example.android.mybakingapp.ui.step_content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.data.model.Step;
import com.example.android.mybakingapp.ui.video_content.RecipeVideoActivity;
import com.example.android.mybakingapp.ui.video_content.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.mybakingapp.util.Constants.EXTRA_STEPS;
import static com.example.android.mybakingapp.util.Constants.POSITION_STEP;
import static com.example.android.mybakingapp.util.Constants.TAG_FRAGMENT_STEP;

public class RecipeStepActivity extends AppCompatActivity implements StepFragment.StepFragmentContract {
    private static final String LOG_TAG = RecipeStepActivity.class.getName();
    public static final String RECIPE ="recipe";
    public boolean mTwoPane;
    private Recipe mRecipe;

    @BindView(R.id.steps_toolbar)
    Toolbar mMenuToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);
        setSupportActionBar(mMenuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager manager = getSupportFragmentManager();
        mRecipe = getIntent().getParcelableExtra(RECIPE);

            if (manager.findFragmentByTag(TAG_FRAGMENT_STEP) == null) {
                FragmentTransaction transaction = manager.beginTransaction();
                StepFragment frag = StepFragment.newInstance(mRecipe);
                transaction.add(R.id.recipe_step, frag, TAG_FRAGMENT_STEP);
                transaction.commit();
            }


        if (findViewById(R.id.linear_layout_tablet) != null) {
            mTwoPane = true;

        } else {
            mTwoPane = false;
        }
        Log.d(LOG_TAG, "oncreate recipestepactivity");
    }

    @Override
    public void onStepSelected(int position) {
        if (mTwoPane) {
            VideoFragment videoFragment = VideoFragment.newInstance(position, (ArrayList<Step>) mRecipe.steps);
            videoFragment.setVideo((ArrayList<Step>) mRecipe.steps);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_video, videoFragment).commit();
        } else {
            Intent intent = new Intent(this, RecipeVideoActivity.class);
            intent.putExtra(EXTRA_STEPS, (ArrayList<Step>) mRecipe.steps);
            intent.putExtra(POSITION_STEP, position);
            startActivity(intent);
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
