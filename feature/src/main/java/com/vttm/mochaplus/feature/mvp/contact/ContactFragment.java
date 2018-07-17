package com.vttm.mochaplus.feature.mvp.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;

public class ContactFragment extends BaseFragment {
    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
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

    private void loadData()
    {

    }
}
