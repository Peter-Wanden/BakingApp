package com.example.peter.bakingapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.ui.detail.RecipeDetailFragment;

import static com.example.peter.bakingapp.app.Constants.RECIPE_DETAIL_FRAGMENT;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;


public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeDetailFragment mDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* Get the selected recipe */
        Bundle bundle = getIntent().getExtras();

        /* Set the title */
        Recipe recipe = bundle != null ? (Recipe) bundle.getParcelable(SELECTED_RECIPE) : null;
        setTitle(recipe != null ? recipe.getTitle() : null);

        FragmentManager mManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            mDetailFragment = new RecipeDetailFragment();

            // Add the recipe to the fragments arguments
            mDetailFragment.setArguments(bundle);
            mManager.beginTransaction().add(R.id.recipe_detail_container, mDetailFragment).commit();
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
