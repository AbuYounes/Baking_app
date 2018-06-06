package com.example.android.mybakingapp.ui.video_content;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mybakingapp.R;
import com.example.android.mybakingapp.data.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


public class VideoFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = VideoFragment.class.getSimpleName();
    public static final String POSITION_STEP = "position_string";
    public static final String EXTRA_STEPS = "extra_steps";
    private static final String EXTRA_SELECTED_INDEX = "selected_index";
    private static final String EXTRA_POSITION = "position";

    private SimpleExoPlayer mExoplayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private SimpleExoPlayerView mExoplayerView;
    private TextView mDescription;
    private ImageView mThumbnail;

    public ImageView backButton, forwardButton;
    private int mSelectedIndex;
    private ArrayList<Step> mSteps;

    private Uri mVideoUri;
    private long mPosition;
    private Step mStep;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(int selectedIndex, ArrayList<Step> steps) {
        Bundle args = new Bundle();
        args.putInt(POSITION_STEP, selectedIndex);
        args.putParcelableArrayList(EXTRA_STEPS, steps);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        if (savedInstanceState != null) {
            mSelectedIndex = savedInstanceState.getInt(EXTRA_SELECTED_INDEX);
            mPosition = savedInstanceState.getLong(EXTRA_POSITION);
        } else {
            mSelectedIndex = getArguments().getInt(POSITION_STEP);
        }
        mSteps = getArguments().getParcelableArrayList(EXTRA_STEPS);
        mStep = mSteps.get(mSelectedIndex);

        initViews(rootView);

        playVideo();

        return rootView;
    }

    private void initViews(View rootView) {
        mDescription = rootView.findViewById(R.id.step_description_text_view);
        mThumbnail = rootView.findViewById(R.id.placeholder_no_video_image);
        mExoplayerView = rootView.findViewById(R.id.video_view_recipe);

        backButton = rootView.findViewById(R.id.btn_back);
        forwardButton = rootView.findViewById(R.id.btn_frw);

        if (backButton != null || forwardButton != null) {

            backButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mSelectedIndex > 0) {
                        mSelectedIndex--;
                        if (mExoplayer != null) {
                            mExoplayer.stop();
                        }
                        playVideo();
                        updateButtonVisibillity();
                    }
                }
            });

            forwardButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mSelectedIndex < mSteps.size() - 1) {
                        mSelectedIndex++;
                        if (mExoplayer != null) {
                            mExoplayer.stop();
                        }
                        updateButtonVisibillity();
                        playVideo();
                    }
                }
            });
        }
    }


    private void updateButtonVisibillity() {
        if (backButton != null || forwardButton != null) {
            if (mSelectedIndex == 0) {
                backButton.setVisibility(View.GONE);
            } else if (mSelectedIndex >= mSteps.size() - 1) {
                forwardButton.setVisibility(View.GONE);
            } else {
                backButton.setVisibility(View.VISIBLE);
                forwardButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setVideo(ArrayList<Step> steps) {
        mSteps = steps;
    }

    private void playVideo() {
        mStep = mSteps.get(mSelectedIndex);
        String videoUrl = mStep.getVideoURL();
        if (TextUtils.isEmpty(videoUrl)) {
            videoUrl = mStep.getThumbnailURL();
        }
        mVideoUri = Uri.parse(mStep.getVideoURL());
        mDescription.setText(mStep.getDescription());

        if (TextUtils.isEmpty(videoUrl) && videoUrl.equals("")) {
            mThumbnail.setVisibility(View.VISIBLE);
            mThumbnail.setImageResource(R.drawable.no_video);
            mExoplayerView.setVisibility(View.GONE);
        } else {
            mThumbnail.setVisibility(View.GONE);
            mExoplayerView.setVisibility(View.VISIBLE);
            initializeVideoPlayer(Uri.parse(videoUrl));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_POSITION, mPosition);
        outState.putInt(EXTRA_SELECTED_INDEX, mSelectedIndex);
    }

//    public static boolean isVideoFile(String path) {
//        String mimeType = URLConnection.guessContentTypeFromName(path);
//        return mimeType != null && mimeType.startsWith("video");
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoUri != null) {
            if (mExoplayer != null) {
                mExoplayer.seekTo(mPosition);
            } else {
                initializeVideoPlayer(mVideoUri);
            }
        }
        updateButtonVisibillity();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mExoplayer != null) {
            mPosition = mExoplayer.getCurrentPosition();
            Log.e(TAG, "get current position : " + mPosition);
        }
        releasePlayer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mExoplayer = null;
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mExoplayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                mExoplayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mExoplayer.seekTo(0);
            }
        });
        mMediaSession.setActive(true);
    }

    private void initializeVideoPlayer(Uri videoUri) {
        initializeMediaSession();
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mExoplayerView.setPlayer(mExoplayer);
        mExoplayer.addListener(this);

        String userAgent = Util.getUserAgent(getContext(), "StepVideo");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
        mExoplayer.prepare(mediaSource);
        mExoplayer.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        Log.d(TAG, "player initialized");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoplayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoplayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
