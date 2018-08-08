package com.vttm.mochaplus.feature.mvp.video.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vttm.mochaplus.feature.MainActivity;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.VideoModel;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.widget.CustomLoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

import im.ene.toro.media.PlaybackInfo;

import static com.vttm.mochaplus.feature.mvp.video.detail.MoreVideosFragment.ARG_EXTRA_BASE_FB_VIDEO;
import static com.vttm.mochaplus.feature.mvp.video.detail.MoreVideosFragment.ARG_EXTRA_BASE_ORDER;
import static com.vttm.mochaplus.feature.mvp.video.detail.MoreVideosFragment.ARG_EXTRA_PLAYBACK_INFO;

public class VideoFragment extends BaseFragment implements AbsInterface.OnItemListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IVideoView {
    private View loadingView;
    private RecyclerView recyclerView;

    private VideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View notDataView, errorView;
    private ArrayList<VideoModel> datas = new ArrayList<>();
    private int currentCategoryId;
    private String lastId = "";

    @Inject
    IVideoPresenter<IVideoView> presenter;

    public static VideoFragment newInstance(Bundle bundle) {
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        return view;
    }

    @Override
    protected void setUp(View view) {
        loadingView = view.findViewById(R.id.loadingView);
        layout_refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        layout_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        layout_refresh.setOnRefreshListener(this);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VideoAdapter(getBaseActivity(), R.layout.item_video, datas, this);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
//        recyclerView.setPadding(recyclerView.getPaddingLeft(), getResources().getDimensionPixelOffset(R.dimen.padding10), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        notDataView = getBaseActivity().getLayoutInflater().inflate(R.layout.item_nodata, (ViewGroup) recyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        errorView = getBaseActivity().getLayoutInflater().inflate(R.layout.item_failed, (ViewGroup) recyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        Bundle bundle = getArguments();
        currentCategoryId = bundle.getInt(AppConstants.KEY_BUNDLE.KEY_CATEGORY_ID, -1);

        if(datas != null && datas.size() == 0)
        {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentPage = 0;
                    loadData();
                }
            }, 200);

        }
    }

    @Override
    public void notifyNetworkChange(boolean flag) {
        if(flag)
        {
            if(datas != null && datas.size() == 0)
            {
                currentPage = 0;
                loadData();
            }
        }
    }

    private void loadData() {
        if(currentPage == 0 && datas.size() ==0)
            loadingView.setVisibility(View.VISIBLE);
        if(currentCategoryId != -1)
        {
            if(presenter != null)
                presenter.loadData(currentPage, AppConstants.NUM_SIZE, currentCategoryId, lastId);
        }
    }

    @Override
    public void bindData(VideoResponse response) {
        loadingView.setVisibility(View.GONE);
        hideLoading();

        if(response != null && response.getResult() != null && response.getResult().size() > 0)
        {
            lastId = response.getLastIdStr();
            int count = response.getResult().size();
            if (currentPage == 0) {
                if (count == 0) {
                    adapter.setEmptyView(notDataView);
                } else {
                    datas.clear();
                    datas.addAll(response.getResult());
                    adapter.setNewData(datas);

                    if (layoutManager != null)
                        layoutManager.scrollToPosition(0);
                }
            } else {
                if (count == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(response.getResult());
                    adapter.loadMoreComplete();
                }
            }
        }
        else
        {
            if (currentPage == 0) {
                adapter.setEmptyView(errorView);
            } else {
                adapter.loadMoreFail();
            }
        }
    }

    @Override
    public void onItemClick(int pos) {
        if(getBaseActivity() != null && getBaseActivity() instanceof MainActivity)
        {
            Bundle bundle = new Bundle();
//            bundle.putSerializable(AppConstants.KEY_BUNDLE.KEY_VIDEO_SELECT, datas.get(pos));
            bundle.putInt(ARG_EXTRA_BASE_ORDER, 0);
            bundle.putParcelable(ARG_EXTRA_BASE_FB_VIDEO, datas.get(pos));
            bundle.putParcelable(ARG_EXTRA_PLAYBACK_INFO, new PlaybackInfo());
            ((MainActivity)getBaseActivity()).showFragment(AppConstants.TAB_VIDEO_DETAIL, bundle);
        }
    }

    @Override
    public void onItemMoreClick(int pos) {

    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage += datas.size();
                loadData();
            }

        }, 1000);
    }
}
