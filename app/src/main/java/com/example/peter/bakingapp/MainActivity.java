package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.bakingapp.ui.recipe.RecipeFragment;

public class MainActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            RecipeFragment recipeCardView = new RecipeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main_fragment_container, recipeCardView)
                    .commit();
        }
    }
}


