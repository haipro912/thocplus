package com.vttm.mochaplus.feature.mvp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.mvp.call.CallFragment;
import com.vttm.mochaplus.feature.mvp.chat.ChatFragment;
import com.vttm.mochaplus.feature.mvp.contact.ContactFragment;
import com.vttm.mochaplus.feature.mvp.more.MoreFragment;
import com.vttm.mochaplus.feature.mvp.video.main.TabVideoFragment;

import butterknife.ButterKnife;

public class MainFragment extends BaseFragment{
    public static final String TAG = MainFragment.class.getSimpleName();

    BottomNavigationBar bottomBar;
    ViewPager viewPager;

    private MainAdapter pagerAdapter;
    private int currentTabIndex = 0;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    protected void setUp(View view) {
        bottomBar = view.findViewById(R.id.bottomBar);
        viewPager = view.findViewById(R.id.tab_view_pager);
        setupViewPager();
        setupTabLayout();
    }

    private void setupTabLayout() {

        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar.clearAll();

        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_chat, getResources().getString(R.string.tab_chat)).setActiveColorResource(R.color.colorAccent));
        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_call, getResources().getString(R.string.tab_call)).setActiveColorResource(R.color.colorAccent));
        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_contact, getResources().getString(R.string.tab_contact)).setActiveColorResource(R.color.colorAccent));
        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_video, getResources().getString(R.string.tab_video)).setActiveColorResource(R.color.colorAccent));
        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_more_horiz, getResources().getString(R.string.tab_more)).setActiveColorResource(R.color.colorAccent));
        bottomBar.setFirstSelectedPosition(0);
        bottomBar.initialise();

        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int tabId) {
                viewPager.setCurrentItem(tabId);
            }

            @Override
            public void onTabUnselected(int i) {

            }

            @Override
            public void onTabReselected(int i) {

            }
        });
    }

    private void setupViewPager() {
        if (pagerAdapter == null)
        {
            pagerAdapter = new MainAdapter(getChildFragmentManager());
            pagerAdapter.addFragment(ChatFragment.newInstance(), getString(R.string.tab_chat));
            pagerAdapter.addFragment(CallFragment.newInstance(), getString(R.string.tab_call));
            pagerAdapter.addFragment(ContactFragment.newInstance(), getString(R.string.tab_contact));
            pagerAdapter.addFragment(TabVideoFragment.newInstance(), getString(R.string.tab_video));
            pagerAdapter.addFragment(MoreFragment.newInstance(), getString(R.string.tab_more));
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                bottomBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectTab(final int position) {
        if (viewPager != null)
        {
            bottomBar.selectTab(position);
            viewPager.setCurrentItem(position);
        }
        currentTabIndex = position;
    }
}
