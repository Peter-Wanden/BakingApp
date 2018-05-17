package com.example.peter.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.utils.GsonUtils;

import java.util.Collections;
import java.util.List;

import static com.example.peter.bakingapp.app.Constants.RECIPE_INGREDIENTS;


public class WidgetServiceList
        extends
        RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory
            implements
            RemoteViewsService.RemoteViewsFactory {

        private final String LOG_TAG = WidgetServiceList.class.getSimpleName();

        // TODO Is the widget ID being saved and restored from shared preferences?

        private Context mContext;
        private List<Ingredient> mIngredients = Collections.emptyList();
        private SharedPreferences mPreferences;
        private int mWidgetId;


        public ListRemoteViewsFactory(Context applicationContext) {

            mContext = applicationContext;

            this.mPreferences = mContext.getSharedPreferences(
                    String.valueOf(mWidgetId), MODE_PRIVATE);

//            mWidgetId = intent.getIntExtra(
//                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            Log.e(LOG_TAG, "called");
        }

        @Override
        public void onCreate() {

        }

        /*
        Called onStart() and once the remote views factory is created, as well as every time it
        is notified to update its data calling notifyAppWidgetViewDataChanged().
        */
        @Override
        public void onDataSetChanged() {
            // Get the JSON string containing the list of ingredients from shared preferences
            mIngredients = GsonUtils.ingredientsJsonToList(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(RECIPE_INGREDIENTS, null));
        }

        @Override
        public void onDestroy() {
            mIngredients = null;
        }

        @Override
        public int getCount() {
            return mIngredients == null ? 0 : mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Ingredient currentIngredient = mIngredients.get(position);

            RemoteViews views = new RemoteViews(
                    mContext.getPackageName(),
                    R.layout.list_item_ingredient);

            views.setTextViewText(
                    R.id.ingredient_list_item_quantity,
                    String.valueOf(currentIngredient.getQuantity()));

            views.setTextViewText(
                    R.id.ingredient_list_item_measure,
                    currentIngredient.getMeasure());

            views.setTextViewText(
                    R.id.ingredient_list_item_ingredient,
                    currentIngredient.getIngredient());

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
