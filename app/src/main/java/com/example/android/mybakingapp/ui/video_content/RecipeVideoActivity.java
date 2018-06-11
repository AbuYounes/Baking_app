package com.example.android.mybakingapp.ui.video_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Step;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.mybakingapp.util.Constants.EXTRA_STEPS;
import static com.example.android.mybakingapp.util.Constants.POSITION_STEP;
import static com.example.android.mybakingapp.util.Constants.TAG_FRAGMENT_VIDEO;

public class RecipeVideoActivity extends AppCompatActivity {
    private static final String TAG = RecipeVideoActivity.class.getSimpleName();

    @Nullable @BindView(R.id.menu_toolbar_video)
    Toolbar mMenuToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video);

        ButterKnife.bind(this);
        setSupportActionBar(mMenuToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_VIDEO) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
            int selectedIndex = getIntent().getIntExtra(POSITION_STEP, 0);
            VideoFragment videoFragment = VideoFragment.newInstance(selectedIndex, steps);
            transaction.add(R.id.recipe_video, videoFragment, TAG_FRAGMENT_VIDEO);
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
