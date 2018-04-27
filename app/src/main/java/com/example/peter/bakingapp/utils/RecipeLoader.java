package com.example.peter.bakingapp.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.peter.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Created by peter on 20/03/2018.
 * Loads a list of Recipes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    private ArrayList<Recipe> mRecipes;

    /**
     * Constructs a new {@link RecipeLoader} that returns a list of recipes.
     *
     * @param context         - of the activity or fragment making the request.
     */
    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {

        if (mRecipes != null) {
            // Use cached data
            deliverResult(mRecipes);
        } else {
            // Get new data
            forceLoad();
        }
    }

    /**
     * This is done on a background thread.
     *
     * @return - A list of Recipe objects
     */
    @Override
    public ArrayList<Recipe> loadInBackground() {

        // Get new data
        mRecipes = GsonUtils.getRecipes();
        return mRecipes;
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Recipe> recipes) {
        // Save the data for retrieval later
        mRecipes = recipes;
        super.deliverResult(recipes);
    }
}
