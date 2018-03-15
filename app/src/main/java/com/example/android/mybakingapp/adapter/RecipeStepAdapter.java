package com.example.android.mybakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.MyStepViewHolder> {

    private Context mContext;
    private List<Step> stepList;
    private static final String LOG_TAG = RecipeStepAdapter.class.getName();


    public RecipeStepAdapter(Context mContext, List<Step> stepList) {
        this.mContext = mContext;
        this.stepList = stepList;
    }

    @Override
    public MyStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForRecipeCardItem = R.layout.card_view_two;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForRecipeCardItem, parent, shouldAttachToParentImmediately);
        return new MyStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyStepViewHolder holder, int position) {
        Step stepItem = stepList.get(position);
        holder.description.setText(stepItem.getShortDescription());
    }

    @Override
    public int getItemCount() {
        //Log.d(LOG_TAG, "item count = " + stepList.size());
        return stepList.size();

    }

    public class MyStepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_name)
        TextView description;

        public MyStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
