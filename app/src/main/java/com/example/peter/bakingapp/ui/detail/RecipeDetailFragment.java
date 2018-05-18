package com.example.peter.bakingapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.StepsActivity;
import com.example.peter.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;

public class RecipeDetailFragment
        extends
        Fragment
        implements
        RecipeDetailStepsAdapter.StepsAdapterOnCLickHandler {

    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private FragmentRecipeDetailBinding mDetailBinding;
    private Recipe mSelectedRecipe;
    private OnStepSelectedListener mStepsSelectedCallback;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mDetailBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_detail, container, false);

        return mDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Setup and display the ingredients */
        RecipeDetailIngredientAdapter mIngredientAdapter =
                new RecipeDetailIngredientAdapter(getActivity());

        mDetailBinding.fragmentRecipeDetailIngredientsRecyclerView
                .setAdapter(mIngredientAdapter);

        LinearLayoutManager mIngredientsLayoutManager =
                new LinearLayoutManager(Objects
                        .requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager
                        .VERTICAL, false);

        mDetailBinding.fragmentRecipeDetailIngredientsRecyclerView
                .setLayoutManager(mIngredientsLayoutManager);
        mDetailBinding.fragmentRecipeDetailIngredientsRecyclerView
                .setAdapter(mIngredientAdapter);
        mDetailBinding.fragmentRecipeDetailIngredientsRecyclerView
                .setHasFixedSize(true);
        mDetailBinding.fragmentRecipeDetailIngredientsRecyclerView
                .setNestedScrollingEnabled(false);

        /* Setup and display the steps */
        RecipeDetailStepsAdapter mStepsAdapter =
                new RecipeDetailStepsAdapter(getActivity(), this);

        LinearLayoutManager mStepsLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false);

        mDetailBinding.fragmentRecipeDetailStepsRecyclerView
                .setLayoutManager(mStepsLayoutManager);
        mDetailBinding.fragmentRecipeDetailStepsRecyclerView
                .setAdapter(mStepsAdapter);
        mDetailBinding.fragmentRecipeDetailStepsRecyclerView
                .setHasFixedSize(true);
        mDetailBinding.fragmentRecipeDetailStepsRecyclerView
                .setNestedScrollingEnabled(false);

        /* If it exists get the selected recipe form the intent */
        if (savedInstanceState == null && (getArguments() != null ? getArguments()
                .getParcelable(SELECTED_RECIPE) : null) != null) {
            mSelectedRecipe = getArguments().getParcelable(SELECTED_RECIPE);
        }

        /* If it exists get the selected recipe from savedInstanceState */
        if (savedInstanceState != null && savedInstanceState
                .getParcelable(SELECTED_RECIPE) != null) {
            // Get the recipe from savedInstanceState
            mSelectedRecipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
            // Get the recipe from savedInstanceState
            mSelectedRecipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
        }

        ArrayList<Ingredient> mIngredients =
                mSelectedRecipe != null ? mSelectedRecipe.getIngredients() : null;
        mIngredientAdapter.newIngredients(mIngredients);

        ArrayList<Steps> mSteps = mSelectedRecipe.getSteps();
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

        if (Objects.requireNonNull(getActivity())
                .getResources()
                .getBoolean(R.bool.is_tablet)) {

            mStepsSelectedCallback.onStepSelected(selectedStepId);
        } else {

            Intent intent = new Intent(getActivity(), StepsActivity.class);
            intent.putExtra(SELECTED_RECIPE, mSelectedRecipe);
            intent.putExtra(STEP, selectedStepId);
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mStepsSelectedCallback = (OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    /* onStepSelected interface, calls a method in the host activity named onStepSelected */
    public interface OnStepSelectedListener {
        void onStepSelected(int stepId);
    }
}
