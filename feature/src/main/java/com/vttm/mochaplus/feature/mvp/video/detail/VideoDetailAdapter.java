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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.model.VideoModel;

import java.util.ArrayList;

import im.ene.toro.CacheManager;
import im.ene.toro.ToroPlayer;

/**
 * @author eneim | 6/19/17.
 */

@SuppressWarnings("Range")
public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailHolder> implements CacheManager {

    @NonNull
    private ArrayList<VideoModel> items = new ArrayList<>();

    OnCompleteCallback onCompleteCallback;

    public void setOnCompleteCallback(OnCompleteCallback onCompleteCallback) {
        this.onCompleteCallback = onCompleteCallback;
    }

    public VideoDetailAdapter(@NonNull ArrayList<VideoModel> items) {
        super();
        setHasStableIds(true);
        this.items = items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public VideoModel getItem(@IntRange(from = 0) int position) {
        return items.get(position);
    }

    @Override
    public VideoDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_detail, parent, false);
        VideoDetailHolder viewHolder = new VideoDetailHolder(view);
        viewHolder.setEventListener(new ToroPlayer.EventListener() {
            @Override
            public void onBuffering() {

            }

            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onCompleted() {
                if (onCompleteCallback != null) onCompleteCallback.onCompleted(viewHolder);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoDetailHolder holder, int position) {
        holder.bind(this, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Implement the CacheManager;

    @NonNull
    @Override
    public Object getKeyForOrder(int order) {
        return getItem(order);
    }

    @Nullable
    @Override
    public Integer getOrderForKey(@NonNull Object key) {
        return key instanceof VideoModel ? items.indexOf(key) : null;
    }

    // on complete stuff
    int findNextPlayerPosition(int base) {
        return base + 1;
    }

    static abstract class OnCompleteCallback {
        abstract void onCompleted(ToroPlayer player);
    }
}
