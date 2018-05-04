package com.example.android.mybakingapp.ui.video_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Step;

import java.util.ArrayList;

public class RecipeVideoActivity extends AppCompatActivity {
    private static final String TAG = RecipeVideoActivity.class.getSimpleName();
    public static final String TAG_FRAGMENT_VIDEO = "video fragment";

    int mSelectedIndex;
    ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_VIDEO) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            mSteps = getIntent().getParcelableArrayListExtra(VideoFragment.EXTRA_STEPS);
            mSelectedIndex = getIntent().getIntExtra(VideoFragment.POSITION_STEP, 0);
            VideoFragment videoFragment = VideoFragment.newInstance(mSelectedIndex, mSteps);
            transaction.add(R.id.recipe_video, videoFragment, TAG_FRAGMENT_VIDEO);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        //Back Button to navigate back to the details screen
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
