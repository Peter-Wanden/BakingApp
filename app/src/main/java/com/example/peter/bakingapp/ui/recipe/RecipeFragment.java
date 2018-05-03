package com.example.peter.bakingapp.ui.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.utils.NetworkUtils;
import com.example.peter.bakingapp.utils.RecipeLoader;

import java.util.ArrayList;
import java.util.Objects;


public class RecipeFragment
        extends
        Fragment
        implements
        LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();
    private static final int RECIPE_LOADER_ID = 100;
    private ArrayList<Recipe> mRecipes;
    private View mLoadingIndicator;
    private TextView mEmptyStateTextView;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private Recipe mCurrentRecipe;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        mRecyclerView = rootView.findViewById(R.id.fragment_recipes_recycler_view);
        mLoadingIndicator = rootView.findViewById(R.id.fragment_recipes_progress_bar);
        mEmptyStateTextView = rootView.findViewById(R.id.fragment_recipes_empty_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecipeAdapter = new RecipeAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        /* Create dynamic RecyclerViews depending on the display type */
        if (getResources().getBoolean(R.bool.is_tablet) || getResources().getBoolean(R.bool.is_landscape)) {
            mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),
                    columnCalculator());
            mRecyclerView.setLayoutManager(mGridLayoutManager);

        } else {
            mLinearLayoutManager = new
                    LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

        }
        // Portrait for phone
        // Landscape for phone
        // Portrait for tablet
        // Landscape for tablet.
        mRecyclerView.setHasFixedSize(true);

        /* Check for connectivity */
        if (NetworkUtils.getNetworkStatus(Objects.requireNonNull(getActivity()))) {

            /* Ensures a loader is initialized and active. If the loader doesn't already
             * exist, one is created and (if the activity/fragment is currently started)
             * starts the loader. Otherwise the last created loader is re-used.
             */
            getLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        }
    }

    /* Screen width column calculator */
    private int columnCalculator() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int colums = width / divider;
        if (colums < 2) return 2;
        return colums;
    }

    /* Implements the onClick interface in the Recipe adapter */
    @Override
    public void onClick(Recipe clickedRecipe) {
        mCurrentRecipe = clickedRecipe;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        indicateLoading();
        return new RecipeLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {

        if (recipes !=null && recipes.size() > 0) {
            showResults();
            mRecipeAdapter.swapRecipes(recipes);
        } else {
            noDataAvailable();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
        mRecipes.clear();
        mRecipes = null;
    }

    /* Data loading and results states */
    private void indicateLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showResults() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void noDataAvailable() {
        showResults();
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }
}
