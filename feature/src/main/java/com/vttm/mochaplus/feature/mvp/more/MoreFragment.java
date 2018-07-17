package com.vttm.mochaplus.feature.mvp.more;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;

public class MoreFragment extends BaseFragment {
    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
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

    }
}
