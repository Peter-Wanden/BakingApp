package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.ui.detail.RecipeDetailFragment;
import com.example.peter.bakingapp.ui.stepDetail.StepDetailFragment;

import static com.example.peter.bakingapp.app.Constants.RECIPE_DETAIL_FRAGMENT;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;


public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeDetailFragment mDetailFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* Get the selected recipe */
        Bundle selectedRecipe = getIntent().getExtras();

        /* Set the title */
        Recipe recipe = selectedRecipe != null ? (Recipe)
                selectedRecipe.getParcelable(SELECTED_RECIPE) : null;
        setTitle(recipe != null ? recipe.getTitle() : null);

        FragmentManager manager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            mDetailFragment = new RecipeDetailFragment();

            // Add the recipe to the fragments arguments
            mDetailFragment.setArguments(selectedRecipe);
            manager.beginTransaction()
                    .add(R.id.activity_recipe_detail_container, mDetailFragment)
                    .commit();

            // If this is a tablet, fill the second view pane with step detail
            if (getResources().getBoolean(R.bool.is_tablet)) {

                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setArguments(selectedRecipe);

                manager.beginTransaction()
                        .add(R.id.activity_steps_fragment_container, stepDetailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState,
                RECIPE_DETAIL_FRAGMENT, mDetailFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().getFragment(
                savedInstanceState, RECIPE_DETAIL_FRAGMENT);
    }
}
