/*
 * Copyright (c) 2017 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vttm.mochaplus.feature.mvp.video.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.helper.image.ImageLoader;
import com.vttm.mochaplus.feature.model.VideoModel;

import java.util.List;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static java.lang.String.format;
import static java.util.Locale.getDefault;

/**
 * @author eneim | 6/18/17.
 */

@SuppressWarnings("WeakerAccess") //
public class VideoDetailHolder extends RecyclerView.ViewHolder implements ToroPlayer {

    @Nullable
    ExoPlayerViewHelper helper;
    @Nullable
    private Uri mediaUri;
    private EventListener eventListener;
    private ViewPropertyAnimator onPlayAnimator;
    private  ViewPropertyAnimator onPauseAnimator;
    private int animatorDuration = 300;

    ImageView userIcon;
    TextView userName;
    TextView userProfile;
    FrameLayout container;
    PlayerView playerView;
    TextView state;
    View overLay;

    public VideoDetailHolder(View itemView) {
        super(itemView);

        userIcon = itemView.findViewById(R.id.fb_user_icon);
        userName = itemView.findViewById(R.id.fb_user_name);
        userProfile = itemView.findViewById(R.id.fb_user_profile);
        container = itemView.findViewById(R.id.fb_item_middle);
        playerView = itemView.findViewById(R.id.fb_video_player);
        state = itemView.findViewById(R.id.player_state);
        overLay = itemView.findViewById(R.id.over_lay);

        playerView.setVisibility(View.VISIBLE);
        playerView.setUseController(false);
    }

    private Playable.EventListener listener = new Playable.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            state.setText(format(getDefault(), "STATE: %d・PWR: %s", playbackState, playWhenReady));
        }
    };

    @SuppressWarnings("SameParameterValue")
    void bind(MoreVideosAdapter adapter, VideoModel item, List<Object> payloads) {
        if (item != null) {
            userName.setText(item.getChannels().get(0).getName());
            ImageLoader.setImage(itemView.getContext(), item.getImagePath(), userIcon);
            mediaUri = Uri.parse(item.getOriginalPath());
//            userProfile.setText(format("%s・%s", getRelativeTimeSpanString(item.timeStamp), url.name()));
        }
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return this.playerView;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (mediaUri == null) throw new IllegalStateException("mediaUri is null.");
        if (helper == null) {
            helper = new ExoPlayerViewHelper(this, mediaUri);
            helper.addEventListener(listener);
            helper.addPlayerEventListener(eventListener);
        }
        helper.initialize(container, playbackInfo);
    }

    @Override
    public void play() {
        playerView.setUseController(true);
        if (onPlayAnimator != null) onPlayAnimator.cancel();
        onPlayAnimator = overLay.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                animation.end();
            }
        }).setDuration(animatorDuration);
        onPlayAnimator.start();
        if (helper != null) helper.play();
    }

    @Override
    public void pause() {
        playerView.setUseController(false);
        if (onPauseAnimator != null) onPauseAnimator.cancel();
        onPauseAnimator = overLay.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                animation.end();
            }
        }).setDuration(animatorDuration);
        onPauseAnimator.start();
        if (helper != null) helper.pause();
    }

    @Override
    public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override
    public void release() {
        if (onPlayAnimator != null) onPlayAnimator.cancel();
        if (onPauseAnimator != null) onPauseAnimator.cancel();
        onPlayAnimator = null;
        onPauseAnimator = null;

        if (helper != null) {
            helper.removeEventListener(listener);
            helper.removePlayerEventListener(eventListener);
            helper.release();
            helper = null;
        }
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}
