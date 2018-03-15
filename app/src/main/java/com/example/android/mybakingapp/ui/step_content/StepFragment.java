package com.example.android.mybakingapp.ui.step_content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mybakingapp.ItemClickSupport;
import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.ui.video_content.RecipeVideoActivity;
import com.example.android.mybakingapp.adapter.RecipeStepAdapter;
import com.example.android.mybakingapp.data.model.Step;
import com.example.android.mybakingapp.ui.video_content.VideoFragment;

import java.util.ArrayList;
import java.util.List;


public class StepFragment extends Fragment {

    private static final String LOG_TAG = StepFragment.class.getName();
    private static final String EXTRA_STEPS = "extra_steps";
    private RecyclerView mRecyclerViewSteps;
    private RecipeStepAdapter mAdapter;
    private List<Step> mSteps;
    private Context mContext;
    private boolean twoPane;

    public StepFragment() {

    }

    public static StepFragment newInstance(List<Step> steps) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_STEPS, (ArrayList<Step>) steps);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "oncreateview Stepfragment");
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSteps = getArguments().getParcelableArrayList(EXTRA_STEPS);
        mRecyclerViewSteps = view.findViewById(R.id.recyclerview_recipes_steps);
        mAdapter = new RecipeStepAdapter(getActivity(), mSteps);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSteps.setLayoutManager(mLayoutManager);
        mRecyclerViewSteps.setAdapter(mAdapter);

        Log.d(LOG_TAG, "onCreateView executed");
        ItemClickSupport.addTo(mRecyclerViewSteps).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Step clickedStepItem = mSteps.get(position);
                Intent intent = new Intent(getActivity(), RecipeVideoActivity.class);
                intent.putExtra(VideoFragment.EXTRA_STEP, clickedStepItem);
                startActivity(intent);
            }
        });

    }
}


