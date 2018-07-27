package com.vttm.mochaplus.feature.mvp.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.helper.image.ImageLoader;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;

public class ChatFragment extends BaseFragment {
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        setUp(view);

        return view;
    }

    @Override
    protected void setUp(View view) {
        ImageView imvImage = view.findViewById(R.id.imvImage);
        ImageLoader.setImage(getBaseActivity(), "", imvImage);
    }
}
