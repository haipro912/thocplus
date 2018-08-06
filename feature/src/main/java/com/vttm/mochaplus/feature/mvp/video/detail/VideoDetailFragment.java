package com.vttm.mochaplus.feature.mvp.video.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.VideoModel;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.widget.CustomLoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

public class VideoDetailFragment extends BaseFragment implements AbsInterface.OnItemListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IVideoDetailView {
    private View loadingView;
    private RecyclerView recyclerView;

    private VideoDetailAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View notDataView, errorView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<VideoModel> datas = new ArrayList<>();

    private VideoModel currentVideo;

    @Inject
    IVideoDetailPresenter<IVideoDetailView> presenter;

    public static VideoDetailFragment newInstance(Bundle bundle) {
        VideoDetailFragment fragment = new VideoDetailFragment();
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

        Object object = getArguments().getSerializable(AppConstants.KEY_BUNDLE.KEY_VIDEO_SELECT);;
        if(object != null && object instanceof VideoModel)
            currentVideo = (VideoModel) object;

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        loadingView = view.findViewById(R.id.loadingView);
        layout_refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        layout_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        layout_refresh.setOnRefreshListener(this);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VideoDetailAdapter(getBaseActivity(), R.layout.item_video, datas, this);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);

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


        if(currentVideo == null || (currentVideo != null && TextUtils.isEmpty(currentVideo.getOriginalPath())))
        {
            //Neu ko co media url thi goi api lay chi tiet video

        }
        else
        {
            //Neu co media url thi play luon sau do goi api cap nhat chi tiet sau

        }
    }

    private void loadData() {


        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(presenter != null)
                    presenter.loadData(currentPage, AppConstants.NUM_SIZE, currentCategoryId, lastId);
            }
        }, 200);
    }

    @Override
    public void bindData(VideoResponse response) {
        loadingView.setVisibility(View.GONE);
        hideLoading();

        if(response != null && response.getResult() != null && response.getResult().size() > 0)
        {
            int count = response.getResult().size();
            if (currentPage == 0) {
                if (count == 0) {
                    adapter.setEmptyView(notDataView);
                } else {
                    datas.clear();
                    adapter.setNewData(response.getResult());
                    datas.addAll(response.getResult());

                    if (layoutManager != null)
                        layoutManager.scrollToPosition(0);
                }
            } else {
                if (count == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(response.getResult());
                    datas.addAll(response.getResult());
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
