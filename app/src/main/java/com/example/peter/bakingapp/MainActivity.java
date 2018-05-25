package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.bakingapp.ui.recipe.RecipeFragment;

public class MainActivity
        extends AppCompatActivity {

    // ToDo - ReadMe file with libraries and what they were used for
    // ToDo - RecipeFragment is showing no network state after configuration change - fix for all views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If it doesn't already exist, fire up the RecipesFragment.
        if (savedInstanceState == null) {
            RecipeFragment recipeCardView = new RecipeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main_fragment_container, recipeCardView)
                    .commit();
        }
    }
}


