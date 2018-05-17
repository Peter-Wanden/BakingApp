package com.example.peter.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.peter.bakingapp.model.Ingredient;

import java.util.Collections;
import java.util.List;

public class WidgetService extends IntentService {

    private static final String LOG_TAG = WidgetService.class.getSimpleName();

    /* Define the actions the intent service can handle */
    public static final String ACTION_CHANGE_RECIPE = "com.example.peter.bakingapp.change_recipe";
    public static final String ACTION_UPDATE_WIDGET = "com.example.peter.bakingapp.update_widget";

    private final List<Ingredient> mIngredients = Collections.emptyList();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public WidgetService() {
        super("WidgetService");
    }

    /* Static method that allows explicitly triggering this service to perform this action.
     * This intent refers to this class with its action set to 'change recipe'.
     */
    public static void startActionChangeRecipe(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_CHANGE_RECIPE);
        context.startService(intent);
    }

    /* Updates any and all widgets */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    /* To handle the intent we need to extract the action and handle each action separately */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_CHANGE_RECIPE.equals(action)) {
                handleActionChangeRecipe();
            } else if (ACTION_UPDATE_WIDGET.equals(action)){
                handleActionUpdateWidget();
            }
        }
    }


    private void handleActionChangeRecipe() {
        // ToDo - This is where we change the recipe

    }

    private void handleActionUpdateWidget() {
        // ToDo - update the widget with ingredients list - Get the ingredients from shared prefs here?
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(
                new ComponentName(this, WidgetProvider.class));

        WidgetProvider.updateAppWidgets(this, widgetManager, appWidgetIds);
    }
}
