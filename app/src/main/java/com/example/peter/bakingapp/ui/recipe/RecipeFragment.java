package com.example.peter.bakingapp.ui.recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.RecipeDetailActivity;
import com.example.peter.bakingapp.WidgetService;
import com.example.peter.bakingapp.databinding.FragmentRecipesBinding;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.utils.GsonUtils;
import com.example.peter.bakingapp.utils.NetworkUtils;
import com.example.peter.bakingapp.utils.RecipeLoader;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.peter.bakingapp.app.Constants.RECIPE_INGREDIENTS;
import static com.example.peter.bakingapp.app.Constants.RECIPE_TITLE;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;


public class RecipeFragment
        extends
        Fragment
        implements
        LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    private FragmentRecipesBinding mRecipesBinding;
    private static final int RECIPE_LOADER_ID = 100;
    private ArrayList<Recipe> mRecipes;
    private RecipeAdapter mRecipeAdapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mRecipesBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipes, container, false);

        return mRecipesBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecipeAdapter = new RecipeAdapter(getActivity(), this);
        mRecipesBinding.fragmentRecipesRecyclerView.setAdapter(mRecipeAdapter);

        /* Create RecyclerViews with dynamic widths depending on the display type. */
        // Portrait for phone.
        // Landscape for phone.
        // Portrait for tablet.
        // Landscape for tablet.
        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(
                    Objects.requireNonNull(getActivity())
                    .getApplicationContext(), columnCalculator());

            mRecipesBinding.fragmentRecipesRecyclerView.setLayoutManager(mGridLayoutManager);

        } else {
            LinearLayoutManager mLinearLayoutManager = new
                    LinearLayoutManager(Objects.requireNonNull(getActivity())
                    .getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mRecipesBinding.fragmentRecipesRecyclerView.setLayoutManager(mLinearLayoutManager);

        }

        mRecipesBinding.fragmentRecipesRecyclerView.setHasFixedSize(true);

        /* Check for connectivity */
        if (NetworkUtils.getNetworkStatus(Objects.requireNonNull(getActivity()))) {
            getLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        }
    }

    /* Screen width column calculator */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity())
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }

    /* Implements the onClick interface in the Recipe adapter */
    @Override
    public void onClick(Recipe selectedRecipe) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        /*
        Store the recipe title and ingredient list (converted to JSON) in shared preferences
        This will be the base information for the widget.
        */
        preferences
                .edit()
                .putString(RECIPE_TITLE, selectedRecipe.getTitle())
                .putString(RECIPE_INGREDIENTS, GsonUtils
                        .ingredientsToJson(selectedRecipe.getIngredients()))
                .apply();

        /* Update the widget with the selected recipe */
        WidgetService.startActionUpdateWidget(getActivity());

        // Open the recipe detail activity and pass in the new recipe
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra(SELECTED_RECIPE, selectedRecipe);
        startActivity(intent);
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

    /* Data loading and result states */
    private void indicateLoading() {
        mRecipesBinding.fragmentRecipesProgressBar.setVisibility(View.VISIBLE);
    }

    private void showResults() {
        mRecipesBinding.fragmentRecipesProgressBar.setVisibility(View.GONE);
    }

    private void noDataAvailable() {
        showResults();
        mRecipesBinding.fragmentRecipesEmptyView.setVisibility(View.VISIBLE);
    }
}
