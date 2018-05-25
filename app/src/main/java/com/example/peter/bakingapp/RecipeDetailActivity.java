package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.ui.detail.RecipeDetailFragment;
import com.example.peter.bakingapp.ui.stepDetail.StepDetailFragment;

import static com.example.peter.bakingapp.app.Constants.RECIPE_DETAIL_FRAGMENT;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;


public class RecipeDetailActivity
        extends
        AppCompatActivity
        implements
        RecipeDetailFragment.OnStepSelectedListener {

    private RecipeDetailFragment mRecipeDetailFragment;
    private Bundle mSelectedRecipe;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* Get the selected recipe */
        mSelectedRecipe = getIntent().getExtras();

        /* Set the title */
        Recipe recipe = mSelectedRecipe != null ? (Recipe)
                mSelectedRecipe.getParcelable(SELECTED_RECIPE) : null;

        setTitle(recipe != null ? recipe.getTitle() : null);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            mRecipeDetailFragment = new RecipeDetailFragment();

            // Add the recipe to the fragments arguments
            mRecipeDetailFragment.setArguments(mSelectedRecipe);
            mFragmentManager.beginTransaction()
                    .add(R.id.activity_recipe_detail_container, mRecipeDetailFragment)
                    .commit();

            // If this is a tablet, fill the second view pane with step detail
            if (getResources().getBoolean(R.bool.is_tablet)) {
                // This will always be the default step
                showNewStep(-1);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState,
                RECIPE_DETAIL_FRAGMENT, mRecipeDetailFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().getFragment(
                savedInstanceState, RECIPE_DETAIL_FRAGMENT);
    }


    /* Interface from StepDetailFragment, passes in the selected StepId when in tablet mode */
    @Override
    public void onStepSelected(int stepId) {
        showNewStep(stepId);
    }

    /* Creates a new StepDetailFragment */
    private void showNewStep (int stepId) {
        mSelectedRecipe.putInt(STEP, stepId);
        StepDetailFragment mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setArguments(mSelectedRecipe);

        mFragmentManager.beginTransaction()
        .replace(R.id.activity_steps_fragment_container, mStepDetailFragment)
        .commit();
    }
}
