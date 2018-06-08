package com.example.android.mybakingapp.widgetRecipe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.ui.recipe_content.RecipeActivity;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.android.mybakingapp.util.Constants.EXTRA_APPWIDGET_UPDATE;
import static com.example.android.mybakingapp.widgetRecipe.MyIngredientService.FROM_ACTIVITY_INGREDIENTS_LIST;
import static com.example.android.mybakingapp.widgetRecipe.MyIngredientService.RECIPE_NAME_SERVICE_WIDGET;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static ArrayList<String> ingredientsList = new ArrayList<>();
    static String recipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.listview_recipe_widget);

        // Set the ListWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, ListViewService.class);
        views.setRemoteAdapter(R.id.listview_widget, intent);

        // Create an Intent to launch RecipeActivity
        Intent showActiviyIntent = new Intent(context, RecipeActivity.class);
        showActiviyIntent.setAction(MyIngredientService.FROM_ACTIVITY_INGREDIENTS_LIST);
        showActiviyIntent.setAction(MyIngredientService.RECIPE_NAME_SERVICE_WIDGET);
        showActiviyIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, showActiviyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.back_button, pendingIntent);
        //views.setPendingIntentTemplate(R.id.listview_widget, pendingIntent);
        if(recipeName!=null) {
            views.setTextViewText(R.id.recipeName, recipeName + ":");
        }else{
            views.setTextViewText(R.id.recipeName,  "Choose recipe first");
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listview_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        MyIngredientService.startIngredientService(context, ingredientsList, recipeName);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listview_widget);
        }
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));

        final String action = intent.getAction();

        if (action.equals(EXTRA_APPWIDGET_UPDATE)) {
            ingredientsList = Objects.requireNonNull(intent.getExtras()).getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            recipeName = Objects.requireNonNull(intent.getExtras()).getString(RECIPE_NAME_SERVICE_WIDGET);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview_widget);

            RecipeWidget.updateBakingWidgets(context, appWidgetManager, appWidgetIds);

        }
    }
}

