package com.example.peter.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.StepsActivity;
import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;
import com.example.peter.bakingapp.ui.stepDetail.StepDetailFragment;

import java.util.ArrayList;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;

public class RecipeDetailFragment
        extends
        Fragment
        implements
        RecipeDetailStepsAdapter.StepsAdapterOnCLickHandler {

    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private Recipe mSelectedRecipe;

    private ArrayList<Ingredient> mIngredients;
    private RecyclerView mIngredientsRecyclerView;
    private RecipeDetailIngredientAdapter mIngredientAdapter;
    private LinearLayoutManager mIngredientsLayoutManager;

    private ArrayList<Steps> mSteps;
    private RecyclerView mStepsRecyclerView;
    private RecipeDetailStepsAdapter mStepsAdapter;
    private LinearLayoutManager mStepsLayoutManager;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_recipe_detail, container, false);

        mIngredientsRecyclerView = rootView.findViewById(
                R.id.fragment_recipe_detail_ingredients_recycler_view);

        mStepsRecyclerView = rootView.findViewById(
                R.id.fragment_recipe_detail_steps_recycler_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Setup and display the ingredients */
        mIngredientAdapter = new RecipeDetailIngredientAdapter(getActivity());

        mIngredientsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        /* Setup and display the steps */
        mStepsAdapter = new RecipeDetailStepsAdapter(getActivity(), this);

        mStepsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);

        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setNestedScrollingEnabled(false);

        /* If it exists get the selected recipe form the intent */
        if (savedInstanceState == null && getArguments().getParcelable(SELECTED_RECIPE) != null) {
                mSelectedRecipe = getArguments().getParcelable(SELECTED_RECIPE);
        }

        /* If it exists get the selected recipe from savedInstanceState */
        if (savedInstanceState !=null && savedInstanceState.getParcelable(SELECTED_RECIPE) !=null) {
            // Get the recipe from savedInstanceState
            mSelectedRecipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
            // Get the recipe from savedInstanceState
            mSelectedRecipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
        }

        mIngredients = mSelectedRecipe.getIngredients();
        mIngredientAdapter.newIngredients(mIngredients);

        mSteps = mSelectedRecipe.getSteps();
        mStepsAdapter.swapSteps(mSteps);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_RECIPE, mSelectedRecipe);
    }

    /* Implementation of RecipeDetailStepsAdapter.StepsAdapterOnCLickHandler */
    @Override
    public void onClick(int selectedStepId) {
        Intent intent = new Intent(getActivity(), StepsActivity.class);
        intent.putExtra(SELECTED_RECIPE, mSelectedRecipe);
        intent.putExtra(STEP, selectedStepId);
        startActivity(intent);
    }
}
