package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;
import com.example.peter.bakingapp.ui.stepDetail.StepDetailFragment;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;

public class StepsActivity extends AppCompatActivity {

    private static final String LOG_TAG = StepsActivity.class.getSimpleName();

    private FragmentManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Get the selected recipe
        Bundle incomingRecipe = getIntent().getExtras();
        Recipe recipe = incomingRecipe.getParcelable(SELECTED_RECIPE);
        setTitle(recipe.getTitle());


        mManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            StepDetailFragment detailFragment = new StepDetailFragment();
            detailFragment.setArguments(incomingRecipe);
        }
    }
}
