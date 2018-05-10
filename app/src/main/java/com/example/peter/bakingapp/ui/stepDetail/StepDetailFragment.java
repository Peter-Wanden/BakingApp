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

    private TextView mStepTitle, mStepDescription;
    private ArrayList<Steps> mSteps;
    private int mStepId;
    private Button mPrevious, mNext;
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

        View rootView = inflater.inflate(
                R.layout.fragment_step_detail, container, false);

        mExoPlayer = rootView.findViewById(R.id.fragment_step_detail_video_view);
        mStepTitle = rootView.findViewById(R.id.fragment_step_detail_title);
        mStepDescription = rootView.findViewById(R.id.fragment_step_detail_description);
        mPrevious = rootView.findViewById(R.id.fragment_step_detail_step_button_previous);
        mNext = rootView.findViewById(R.id.fragment_step_detail_step_button_next);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Get the recipe and Steps objects */
        Recipe incomingRecipe = getArguments() != null ? (Recipe)
                getArguments().getParcelable(SELECTED_RECIPE) : null;
        mSteps = incomingRecipe != null ? incomingRecipe.getSteps() : null;
        mStepId = getArguments().getInt(STEP);

        updateStep(mStepId);

        /* Setup the buton actions */
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepId > 0) {
                    mStepId --;
                    updateStep(mStepId);
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
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
            mPrevious.setVisibility(View.INVISIBLE);
        } else {
            mPrevious.setVisibility(View.VISIBLE);
        }
        if (stepId == mSteps.size() -1) {
            mNext.setVisibility(View.INVISIBLE);
        } else {
            mNext.setVisibility(View.VISIBLE);
        }
        mStepTitle.setText(mCurrentStep.getShortDescription());
        mStepDescription.setText(mCurrentStep.getDescription());
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
