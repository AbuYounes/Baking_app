package com.example.android.mybakingapp.ui.recipe_content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.mybakingapp.ItemClickSupport;
import com.example.android.mybakingapp.MyApplication;
import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.adapter.RecipeMenuAdapter;
import com.example.android.mybakingapp.data.RecipesIdlingResources;
import com.example.android.mybakingapp.data.model.Recipe;
import com.example.android.mybakingapp.ui.step_content.RecipeStepActivity;
import com.example.android.mybakingapp.util.Client;
import com.example.android.mybakingapp.util.CommonUtils;
import com.example.android.mybakingapp.util.ConnectivityReceiver;
import com.example.android.mybakingapp.util.Service;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.mybakingapp.ui.step_content.RecipeStepActivity.RECIPE;


public class RecipeActivity extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener, Delayer.DelayerCallback{


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


    private Call<List<Recipe>> mRecipeCall;
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipeMenuAdapter mAdapter;
    boolean mIsConnected;


    @BindView(R.id.menu_toolbar)
    Toolbar mMenuToolbar;
    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerViewRecipes;
    @BindView(R.id.main_content)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);



        Log.d(LOG_TAG, "onCreateView executed");


                ItemClickSupport.addTo(mRecyclerViewRecipes).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            Recipe clickedDataItem = mRecipes.get(position);
                            Intent intent = new Intent(RecipeActivity.this, RecipeStepActivity.class);
                            intent.putExtra(RECIPE, clickedDataItem);
                            checkConnection();
                            if(mIsConnected) {
                                startActivity(intent);
                            }
                        }
                });



        Log.d(LOG_TAG, "onCreate executed");


        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Delayer.processDelay(this, RecipeActivity.this, mIdlingResource);
    }

    private void initViews() {
        setSupportActionBar(mMenuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

        final int columns = getResources().getInteger(R.integer.recipe_columns);
        mRecyclerViewRecipes.setLayoutManager(new GridLayoutManager(this, columns));

        mSwipeRefreshLayout.setRefreshing(true);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.darker_gray);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beforeRefreshing();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        beforeRefreshing();
        MyApplication.getInstance().setConnectivityListener(this);

    }

    private void beforeRefreshing() {
        mRecipes.clear();
        getRecipes();
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
                    mAdapter = new RecipeMenuAdapter(RecipeActivity.this, mRecipes);
                    mRecyclerViewRecipes.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerViewRecipes.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    CommonUtils apiError = CommonUtils.parseError(response);
                    Toast.makeText(RecipeActivity.this, apiError.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("Error", t.getMessage());
                checkConnection();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void checkConnection() {
        mIsConnected = ConnectivityReceiver.isConnected();
        showToast(mIsConnected);
    }

    private void showToast(boolean isConnected) {
        if (isConnected) {

        } else {
            Toast.makeText(this, "There is no INTERNET connection", Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mRecipeCall != null) {
            mRecipeCall.cancel();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showToast(isConnected);
    }

    @Override
    public void onDone(List<Recipe> recipes) {
        initViews();
    }
}
