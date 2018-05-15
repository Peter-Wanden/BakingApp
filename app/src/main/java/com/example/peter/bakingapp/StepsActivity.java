package com.example.peter.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.ui.stepDetail.StepDetailFragment;

import java.util.Objects;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;

public class StepsActivity extends AppCompatActivity {

    private static final String LOG_TAG = StepsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* If a phone in landscape mode, hide the title */
        if (getResources().getBoolean(R.bool.is_landscape)
                && !getResources().getBoolean(R.bool.is_tablet)) {
            requestWindowFeature(FEATURE_NO_TITLE);

            this.getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

             Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_steps);

        // Get the selected recipe and set the title
        Bundle incomingRecipe = getIntent().getExtras();
        Recipe recipe = incomingRecipe != null ? (Recipe)
                incomingRecipe.getParcelable(SELECTED_RECIPE) : null;

        setTitle(recipe != null ? recipe.getTitle() : null);

        FragmentManager mManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            StepDetailFragment detailFragment = new StepDetailFragment();
            detailFragment.setArguments(incomingRecipe);

            mManager.beginTransaction()
                    .add(R.id.activity_steps_fragment_container, detailFragment)
                    .commit();
        }
    }
}
