package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.utils.NetworkUtils;
import com.example.peter.bakingapp.utils.RecipeLoader;

import java.util.ArrayList;


public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RECIPE_LOADER_ID = 100;
    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Check for connectivity */
        if (NetworkUtils.getNetworkStatus(this)) {

            /* Ensures a loader is initialized and active. If the loader doesn't already
             * exist, one is created and (if the activity/fragment is currently started)
             * starts the loader. Otherwise the last created loader is re-used.
             */
            getLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        }

    }

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        return new RecipeLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        if(mRecipes !=null || mRecipes.size() > 0) {
            mRecipes.clear();
        } else {
            Log.i(LOG_TAG, "")
        }
        mRecipes.addAll(recipes);
        Log.i(LOG_TAG, "Recipes loaded");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
        mRecipes.clear();
        mRecipes = null;
    }
}


