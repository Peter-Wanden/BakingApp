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

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.peter.bakingapp.model.Recipe;
import com.example.peter.bakingapp.model.Steps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
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

import static com.example.peter.bakingapp.app.Constants.PLAYER_POSITION;
import static com.example.peter.bakingapp.app.Constants.PLAYER_STATE;
import static com.example.peter.bakingapp.app.Constants.SELECTED_RECIPE;
import static com.example.peter.bakingapp.app.Constants.STEP;

public class StepDetailFragment
        extends
        Fragment {

    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();

    /* Screen members */
    private FragmentStepDetailBinding mStepDetailBinding;
    private boolean mTablet;
    private boolean mLandscape;
    private Steps mCurrentStep;

    /* Data members */
    private ArrayList<Steps> mSteps;
    private int mStepId;

    /* Player members */
    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerPosition = -1;
    private boolean mPlayIfReady = true;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mLandscape = getActivity().getResources().getBoolean(R.bool.is_landscape);
        mTablet = getActivity().getResources().getBoolean(R.bool.is_tablet);

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
        mStepId = getArguments().getInt(STEP, -1);

        if (mStepId != -1) {
            mCurrentStep = mSteps.get(mStepId);
            } else {
            mStepId = 0;
        }

        /* If being created due to a configuration change, get the saved configuration values */
        if (savedInstanceState != null && savedInstanceState.containsKey(PLAYER_POSITION)) {
            mExoPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            mPlayIfReady = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        updateStep();
    }

    /* Updates the Step information */
    public void updateStep() {
        mCurrentStep = mSteps.get(mStepId);
        String videoUrl = mCurrentStep.getVideoUrl();

        /* Release the player from its previous state */
        if (mExoPlayer != null) {
            releasePlayer();
        }

        /* Setup for phone in portrait */
        if (!mTablet && !mLandscape) {
            updateButtonVisibility();
            configureButtons();
            setDescriptionText();
        }

        /* Setup for tablet in portrait & landscape*/
        if (mTablet) {
            setDescriptionText();
        }

        /* If there is a video, play it */
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri uri = Uri.parse(videoUrl);

            /* Set the correct view visibilities for playing a video */
            mStepDetailBinding.fragmentStepDetailVideoPlaceholder.setVisibility(View.INVISIBLE);
            mStepDetailBinding.fragmentStepDetailVideoView.setVisibility(View.VISIBLE);

            setupPlayer(uri);

        } else {

            /* If not, set the correct view visibilities and show a placeholder image */
            mStepDetailBinding.fragmentStepDetailVideoView.setVisibility(View.INVISIBLE);
            mStepDetailBinding.fragmentStepDetailVideoPlaceholder.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(R.drawable.menu_placeholder)
                    .into(mStepDetailBinding.fragmentStepDetailVideoPlaceholder);
        }
    }

    /* Setup the player */
    private void setupPlayer(Uri stepUri) {
        if (mExoPlayer == null) {

            /* Estimate the available bandwidth */
            BandwidthMeter meter = new DefaultBandwidthMeter();

            /* Select the highest quality stream based on the available bandwidth */
            TrackSelection.Factory selectionFactory = new AdaptiveTrackSelection.Factory(meter);

            /* Select the track based on the bandwidth and stream info */
            TrackSelector selector = new DefaultTrackSelector(selectionFactory);

            /* Create a player instance and pass in the configuration info */
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector);

            /* Attach the player to the view */
            mStepDetailBinding.fragmentStepDetailVideoView.setPlayer(mExoPlayer);

            /* Prepare the media source */
            DataSource.Factory factory = new DefaultDataSourceFactory(
                    Objects.requireNonNull(getActivity()),
                    Util.getUserAgent(getActivity(), "BakingApp"),
                    new DefaultBandwidthMeter());

            /* Loads data from the Uri */
            MediaSource source = new ExtractorMediaSource
                    .Factory(factory)
                    .createMediaSource(stepUri);

            /* Prep the player with the data source */
            mExoPlayer.prepare(source);

            /* If a configuration change has taken place, restore previous values */
            if (mExoPlayerPosition != -1) {
                mExoPlayer.seekTo(mExoPlayerPosition);
                mExoPlayerPosition = -1;
                mExoPlayer.setPlayWhenReady(mPlayIfReady);
            }

            /* Play or pause the media */
            mExoPlayer.setPlayWhenReady(mPlayIfReady);
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

    /* Release the player */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    /* Save the player position and state. */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer !=null) {
            outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_STATE, mExoPlayer.getPlayWhenReady());
        }
        releasePlayer();
    }

    /* Configure the previous and next buttons */
    public void configureButtons() {

        mStepDetailBinding.fragmentStepDetailStepButtonPrevious
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStepId > 0) {
                            mStepId --;
                        }
                        mPlayIfReady = true;
                        updateStep();
                    }
                });

        mStepDetailBinding.fragmentStepDetailStepButtonNext
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStepId < mSteps.size() -1) {
                            mStepId ++;
                        }
                        mPlayIfReady = true;
                        updateStep();
                    }
                });
    }

    /* Update the buttons visibility */
    private void updateButtonVisibility() {

            if (mStepId == 0) {
                mStepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.INVISIBLE);
            } else {
                mStepDetailBinding.fragmentStepDetailStepButtonPrevious.setVisibility(View.VISIBLE);
            }
            if (mStepId == mSteps.size() - 1) {
                mStepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.INVISIBLE);
            } else {
                mStepDetailBinding.fragmentStepDetailStepButtonNext.setVisibility(View.VISIBLE);
            }
    }

    /* Set the description text */
    private void setDescriptionText(){
        mStepDetailBinding.fragmentStepDetailTitle.setText(mCurrentStep.getShortDescription());
        mStepDetailBinding.fragmentStepDetailDescription.setText(mCurrentStep.getDescription());
    }
}
