package com.vttm.mochaplus.feature.mvp.video.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.api.response.VideoCategoryResponse;
import com.vttm.mochaplus.feature.model.VideoCategoryModel;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.mvp.video.home.VideoFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;

import java.util.ArrayList;

import javax.inject.Inject;

public class TabVideoFragment extends BaseFragment implements ITabVideoView {

    private View loadingView;
    private ArrayList<VideoCategoryModel> datas = new ArrayList<>();

    TabLayout tabLayout;
    ViewPager viewPager;
    TabVideoAdapter pagerAdapter;

    @Inject
    ITabVideoPresenter<ITabVideoView> presenter;

    public static TabVideoFragment newInstance() {
        TabVideoFragment fragment = new TabVideoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_video, container, false);

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        return view;
    }

    @Override
    protected void setUp(View view) {
        loadingView = view.findViewById(R.id.loadingView);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.tab_view_pager);

        setupViewPager();
        setupTabLayout();

        if(datas.size() == 0)
            loadCategory();
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        if (pagerAdapter == null)
        {
            pagerAdapter = new TabVideoAdapter(getChildFragmentManager());
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void loadCategory() {
        loadingView.setVisibility(View.VISIBLE);
        if(presenter != null)
            presenter.loadCategory();
    }

    @Override
    public void bindData(VideoCategoryResponse response) {
        loadingView.setVisibility(View.GONE);
        if(response != null && response.getResult() != null && response.getResult().size() > 0)
        {
            datas.clear();
            datas.addAll(response.getResult());

            for(int i = 0; i < datas.size(); i++)
            {
                VideoCategoryModel categoryModel = datas.get(i);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.KEY_BUNDLE.KEY_CATEGORY_ID, categoryModel.getId());
                pagerAdapter.addFragment(VideoFragment.newInstance(bundle), categoryModel.getCategoryname());
            }
            pagerAdapter.notifyDataSetChanged();
        }
    }
}
