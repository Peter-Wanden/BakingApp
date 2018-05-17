package com.example.peter.bakingapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.peter.bakingapp.ui.recipe.RecipeFragment;

import java.util.Objects;

import static com.example.peter.bakingapp.app.Constants.WIDGET_REQUEST_RECIPE;

public class MainActivity
        extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // ToDo - Check for correct handling of network status
    // ToDo - Make widget look nice
    // ToDo - ReadMe file with libraries and what they were used for
    //

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


