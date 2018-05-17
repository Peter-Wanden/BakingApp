package com.example.peter.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import static com.example.peter.bakingapp.app.Constants.RECIPE_TITLE;
import static com.example.peter.bakingapp.app.Constants.WIDGET_ID;
import static com.example.peter.bakingapp.app.Constants.WIDGET_REQUEST_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider
        extends
        AppWidgetProvider {

    private static final String LOG_TAG = WidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // TODO - Movie this lot into its own routines

        // Construct the RemoteViews object to update the widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.recipe_widget_provider);

        // An intent that launches the MainActivity when the widget button is clicked.
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(WIDGET_REQUEST_RECIPE, true);
        intent.putExtra(WIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, intent, 0);

        // Get the recipe title from shared preferences
        String recipeTitle = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(RECIPE_TITLE, "");

        // Set the recipe title in the widget
        remoteViews.setTextViewText(R.id.recipe_widget_provider_recipe_title, recipeTitle);

        // Set the ingredients to the remote adapter
        Intent ingredientService = new Intent(context, WidgetServiceList.class);
        ingredientService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        ingredientService.setData(Uri.parse(ingredientService.toUri(Intent.URI_INTENT_SCHEME)));


        // Set the ListView and the adapter to the remote view
        remoteViews.setRemoteAdapter(
                R.id.recipe_widget_provider_ingredients_list, ingredientService);
        remoteViews.setEmptyView(
                R.id.recipe_widget_provider_ingredients_list, R.id.empty_view);

        // Widgets only allow click handlers to launch PendingIntents
        remoteViews.setOnClickPendingIntent(R.id.recipe_widget_provider_add_button, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        // Runs onDataSetChanged() in the WidgetServiceList.class (the adapter for the Ingredients)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_provider_ingredients_list);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager widgetManager,
                                        int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }

    /*
     * Called each time a new widget is created and each update period as set in
     * recipe_widget_provider_info.xml - android:updatePeriodMillis="86400000"
     *
     * The AppWidgetManager class gives information regarding all existing widgets, it's also
     * how you access and force an update to available widgets.
     *
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // To update a widget you need to pass in its ID along with a RemoteViews object
        // describing the widget.
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

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        // There is no more widget so delete the recipe info from shared preferences
        for (int widgetId : appWidgetIds) {
            context.getSharedPreferences(String.valueOf(widgetId), Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
        }
    }
}

