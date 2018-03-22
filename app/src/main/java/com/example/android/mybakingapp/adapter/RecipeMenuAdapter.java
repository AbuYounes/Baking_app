package com.example.android.mybakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeMenuAdapter extends RecyclerView.Adapter<RecipeMenuAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;
    private static final String LOG_TAG = RecipeMenuAdapter.class.getName();

    public RecipeMenuAdapter(Context mContext, List<Recipe> recipeList) {
        this.mContext = mContext;
        this.recipeList = recipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForRecipeCardItem = R.layout.card_view_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForRecipeCardItem, parent, shouldAttachToParentImmediately);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Recipe clickedDataItem = recipeList.get(position);
        holder.recipeName.setText(clickedDataItem.recipeName);

        String imageUrl = recipeList.get(position).image;

        if (imageUrl != null && imageUrl.isEmpty())
        {
            switch (position)
            {
                //Nutella Pie
                case 0:
                    Picasso.with(mContext).load(R.drawable.nutellapie).into(holder.thumbnail);
                    break;
                //Brownies
                case 1:
                    Picasso.with(mContext).load(R.drawable.brownis).into(holder.thumbnail);
                    break;
                //Yellow Cake
                case 2:
                    Picasso.with(mContext).load(R.drawable.yellow_cake).into(holder.thumbnail);
                    break;
                //cheesecake
                case 3:
                    Picasso.with(mContext).load(R.drawable.cheesecake).into(holder.thumbnail);
                    break;
            }
        } else
        {
            Picasso.with(mContext).load(imageUrl).into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name)
        TextView recipeName;

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.recipe_thumbnail)
        ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}



