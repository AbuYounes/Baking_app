package com.example.android.mybakingapp.widgetRecipe;


import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.mybakingapp.R;

import java.util.ArrayList;

import static com.example.android.mybakingapp.widgetRecipe.RecipeWidget.ingredientsList;

public class ListViewService extends RemoteViewsService {

    ArrayList<String> remoteViewIngredientList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;

        public ListRemoteViewsFactory(Context applicationContext) {
            this.mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteViewIngredientList = ingredientsList;

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (remoteViewIngredientList == null) {
                return 0;
            } else {
                return remoteViewIngredientList.size();
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_list_item);

            if(remoteViewIngredientList!=null) {
                views.setTextViewText(R.id.ingredient_name_text_view, remoteViewIngredientList.get(position));
            }else{
                views.setTextViewText(R.id.ingredient_name_text_view, ".....");
            }
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.ingredient_name_text_view, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
