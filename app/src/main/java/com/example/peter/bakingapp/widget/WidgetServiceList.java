package com.example.peter.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.utils.GsonUtils;
import com.example.peter.bakingapp.utils.IngredientFormat;

import java.text.DecimalFormat;
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

        private Context mContext;
        private List<Ingredient> mIngredients = Collections.emptyList();

        public ListRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
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

            /* Set the TextView text color */
            views.setTextColor(R.id.ingredient_list_item_quantity,
                    getResources().getColor(R.color.white));

            views.setTextColor(R.id.ingredient_list_item_measure,
                    getResources().getColor(R.color.white));

            views.setTextColor(R.id.ingredient_list_item_ingredient,
                    getResources().getColor(R.color.white));

            /* Set the TextView text */
            views.setTextViewText(
                    R.id.ingredient_list_item_quantity,
                    IngredientFormat.formatRawQuantity(currentIngredient.getQuantity()));

            views.setTextViewText(
                    R.id.ingredient_list_item_measure,
                    IngredientFormat.formatRawUnits(currentIngredient.getMeasure()));

            views.setTextViewText(
                    R.id.ingredient_list_item_ingredient,
                    IngredientFormat.formatRawIngredient(currentIngredient.getIngredient()));

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
