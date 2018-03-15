package com.example.android.mybakingapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.MyIngredientViewHolder>{

    private List<Ingredient> mIngredientsList;
    private Context mContext;

    public RecipeIngredientAdapter(List<Ingredient> mIngredients, Context mContext) {
        this.mIngredientsList = mIngredients;
        this.mContext = mContext;
    }

    @Override
    public MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForRecipeCardItem = R.layout.card_view_two;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForRecipeCardItem, parent, shouldAttachToParentImmediately);
        return new MyIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyIngredientViewHolder holder, int position) {
        Ingredient ingredientItem = mIngredientsList.get(position);
        holder.ingredients.setText(ingredientItem.getIngredient());

    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }

    public class MyIngredientViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.step_name)
        TextView ingredients;

        public MyIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
