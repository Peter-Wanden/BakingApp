package com.example.peter.bakingapp.ui.stepDetail;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;

public class StepDetailFragment
        extends
        Fragment {

    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();

    private FragmentStepDetailBinding mStepDetailBinding;
    private ArrayList<Steps> mSteps;
    private int mStepId;

    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerPosition = -1;
    private boolean mPlayIfReady = true;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mStepDetailBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_step_detail, container, false);

        return mStepDetailBinding.getRoot();
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
        mStepDetailBinding.fragmentStepDetailStepButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepId > 0) {
                    mStepId --;
                    updateStep(mStepId);
                }
            }
        });

        mStepDetailBinding.fragmentStepDetailStepButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepId < mSteps.size() -1) {
                    releasePlayer();
                    mStepId ++;
                    updateStep(mStepId);
                }
            }
        });
    }

    /* Updates to the Step information and button visibility */
    public void updateStep(int stepId) {
        if (mExoPlayer != null) releasePlayer();
        Steps mCurrentStep = mSteps.get(stepId);
        String videoUrl = mCurrentStep.getVideoUrl();

        if (stepId == 0) {
            mStepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.INVISIBLE);
        } else {
            mStepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.VISIBLE);
        }
        if (stepId == mSteps.size() -1) {
            mStepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.INVISIBLE);
        } else {
            mStepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.VISIBLE);
        }
        mStepDetailBinding.fragmentStepDetailTitle.setText(mCurrentStep.getShortDescription());
        mStepDetailBinding.fragmentStepDetailDescription.setText(mCurrentStep.getDescription());

        /* If there is a video, play it */
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri uri = Uri.parse(videoUrl);
            mStepDetailBinding.fragmentStepDetailVideoPlaceholder.setVisibility(View.GONE);
            mStepDetailBinding.fragmentStepDetailVideoView.setVisibility(View.VISIBLE);
            setupPlayer(uri);
        } else {
            /* If not show a placeholder image */
            mStepDetailBinding.fragmentStepDetailVideoView.setVisibility(View.GONE);
            mStepDetailBinding.fragmentStepDetailVideoPlaceholder.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.menu_placeholder).into(mStepDetailBinding.fragmentStepDetailVideoPlaceholder);
        }
    }

    /* Setup the player */
    private void setupPlayer(Uri stepUri) {
        if (mExoPlayer == null) {

            BandwidthMeter meter = new DefaultBandwidthMeter();

            TrackSelection.Factory selectionFactory = new AdaptiveTrackSelection.Factory(meter);
            TrackSelector selector = new DefaultTrackSelector(selectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector);

            mStepDetailBinding.fragmentStepDetailVideoView.setPlayer(mExoPlayer);

            // Prepare the media source
            DataSource.Factory factory = new DefaultDataSourceFactory(
                    Objects.requireNonNull(getActivity()),
                    Util.getUserAgent(getActivity(), "BakingApp"),
                    new DefaultBandwidthMeter());

            MediaSource source = new ExtractorMediaSource
                    .Factory(factory)
                    .createMediaSource(stepUri);

            mExoPlayer.prepare(source);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    /* Release the player */
    private void releasePlayer() {
        if (mExoPlayer !=  null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }
}
