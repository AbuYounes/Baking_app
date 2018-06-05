package com.example.android.mybakingapp.widgetRecipe;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Objects;


public class MyIngredientService extends IntentService {

    public static String FROM_ACTIVITY_INGREDIENTS_LIST ="FROM_ACTIVITY_INGREDIENTS_LIST";
    public static String RECIPE_NAME_SERVICE_WIDGET ="recipe_name_widget";

    public MyIngredientService() {
        super("MyIngredientService");
    }

    public static void startIngredientService(Context context, ArrayList<String> fromActivityIngredientsList, String recipeName) {

        Intent intent = new Intent(context, MyIngredientService.class);
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        intent.putExtra(RECIPE_NAME_SERVICE_WIDGET, recipeName);
        context.startService(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
                ArrayList<String> fromActivityIngredientsList = Objects.requireNonNull(intent.getExtras()).getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
                String recipeName = Objects.requireNonNull(intent.getExtras()).getString(RECIPE_NAME_SERVICE_WIDGET);
                handleActionShowIngredients(fromActivityIngredientsList, recipeName);
            }
    }

    private void handleActionShowIngredients(ArrayList<String> fromActivityIngredientsList, String recipeName) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST,fromActivityIngredientsList);
        intent.putExtra(RECIPE_NAME_SERVICE_WIDGET, recipeName);
        sendBroadcast(intent);
    }
}
