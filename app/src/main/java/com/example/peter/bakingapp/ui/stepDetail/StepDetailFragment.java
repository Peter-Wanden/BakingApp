package com.example.peter.bakingapp.ui.stepDetail;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.util.ArrayList;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;

public class StepDetailFragment
        extends
        Fragment {

    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();

    private FragmentStepDetailBinding stepDetailBinding;
    private ArrayList<Steps> mSteps;
    private int mStepId;
    private Steps mCurrentStep;

    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerPosition = -1;
    private boolean mPlayIfReady = true;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        stepDetailBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_step_detail, container, false);

        return stepDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Get the recipe and Steps objects */
        Recipe incomingRecipe = getArguments() != null ? (Recipe)
                getArguments().getParcelable(SELECTED_RECIPE) : null;
        mSteps = incomingRecipe != null ? incomingRecipe.getSteps() : null;
        mStepId = getArguments().getInt(STEP);

        /* Update the display */
        updateStep(mStepId);

        /* Setup the button actions */
        stepDetailBinding.fragmentStepDetailStepButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepId > 0) {
                    mStepId --;
                    updateStep(mStepId);
                }
            }
        });

        stepDetailBinding.fragmentStepDetailStepButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepId < mSteps.size() -1) {
                    mStepId ++;
                    updateStep(mStepId);
                }
            }
        });

        /* Setup the video player */
        String videoUrl = mCurrentStep.getVideoUrl();
        if (videoUrl != null && !videoUrl.isEmpty()) {

        }
    }

    /* Updates to the Step information and button visibility */
    public void updateStep(int stepId) {
        mCurrentStep = mSteps.get(stepId);
        if (stepId == 0) {
            stepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.INVISIBLE);
        } else {
            stepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.VISIBLE);
        }
        if (stepId == mSteps.size() -1) {
            stepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.INVISIBLE);
        } else {
            stepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.VISIBLE);
        }
        stepDetailBinding.fragmentStepDetailTitle.setText(mCurrentStep.getShortDescription());
        stepDetailBinding.fragmentStepDetailDescription.setText(mCurrentStep.getDescription());
    }

    /* Setup the player */
    private void setupPlayer(Uri stepUri) {
        if (mExoPlayer == null) {

            BandwidthMeter meter = new DefaultBandwidthMeter();
            TrackSelection.Factory selectionFactory = new AdaptiveTrackSelection.Factory(meter);
            TrackSelector selector = new DefaultTrackSelector(selectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector);



        }
    }
}
