package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.ui.detail.RecipeDetailFragment;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;


public class RecipeDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* Get the selected recipe */
        Bundle bundle = getIntent().getExtras();

        /* Set the title */
        Recipe recipe = bundle.getParcelable(SELECTED_RECIPE);
        setTitle(recipe.getTitle());

        FragmentManager mManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setArguments(bundle);
            mManager.beginTransaction().add(R.id.recipe_detail_container, detailFragment).commit();
        }
    }
}
