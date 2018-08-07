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
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.VideoModel;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.widget.CustomLoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

public class VideoDetailFragment extends BaseFragment implements AbsInterface.OnItemListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IVideoDetailView {
    private RecyclerView recyclerView;

    private VideoDetailAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View notDataView, errorView;
    private ImageView btnBack;
    private View root;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<VideoModel> datas = new ArrayList<>();
    private String lastId = "";
    private int dataSize;
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
        View view = inflater.inflate(R.layout.fragment_video_detail, container, false);

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        return view;
    }

    @Override
    protected void setUp(View view) {
        root = view.findViewById(R.id.root);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        btnBack = view.findViewById(R.id.btnBack);

        layout_refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        layout_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        layout_refresh.setOnRefreshListener(this);
        layout_refresh.setEnabled(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new VideoDetailAdapter(getBaseActivity(), R.layout.item_video, datas, this);
        adapter.setLoadMoreView(new CustomLoadMoreView());
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().onBackPressed();
            }
        });

        Object object = getArguments().getSerializable(AppConstants.KEY_BUNDLE.KEY_VIDEO_SELECT);
        if(object != null && object instanceof VideoModel)
            currentVideo = (VideoModel) object;

        loadVideoDetail();
    }

    @Override
    public void notifyNetworkChange(boolean flag) {
        if(flag)
        {
            if(currentVideo != null && TextUtils.isEmpty(currentVideo.getOriginalPath()))
            {
                loadVideoDetail();
            }
            else if(datas.size() == 1)
            {
                //Load video lien quan
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage = 0;
                        loadVideoRelate();
                    }

                }, 200);
            }
        }
    }

    private void loadVideoDetail() {
        if(presenter != null)
        {
            if(shimmerFrameLayout != null)
            {
                shimmerFrameLayout.startShimmer();
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                root.setVisibility(View.GONE);
            }
            presenter.loadVideoDetail(currentVideo.getLink());
        }
    }

    private void loadVideoRelate()
    {
        if(presenter != null)
            presenter.loadVideoRelate(currentVideo.getName(), currentPage, AppConstants.NUM_SIZE, lastId);
    }

    @Override
    public void bindDataVideoDetail(VideoDetailResponse response) {

        if(response != null && response.getVideoDetail() != null && !TextUtils.isEmpty(response.getVideoDetail().getOriginalPath()))
        {
            currentVideo = response.getVideoDetail();
        }

        //Truong hop ko co du lieu de play thi show popup loi, nhan back quay lai man hinh truoc do
        if(currentVideo != null && TextUtils.isEmpty(currentVideo.getOriginalPath()))
        {
            new MaterialDialog.Builder(getBaseActivity())
                    .title(R.string.app_name)
                    .content("Lấy dữ liệu bị lỗi!")
                    .positiveText("Thử lại")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getBaseActivity().onBackPressed();
                        }
                    })
                    .show();

            return;
        }

        datas.clear();
        datas.add(currentVideo);
        adapter.setNewData(datas);

        if(shimmerFrameLayout != null)
        {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            root.setVisibility(View.VISIBLE);
        }

        //Load video lien quan
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 0;
                loadVideoRelate();
            }

        }, 200);
    }

    @Override
    public void bindDataVideoRelate(VideoResponse response) {
        if(response != null && response.getResult() != null && response.getResult().size() > 0)
        {
            lastId = response.getLastIdStr();
            dataSize = response.getResult().size();

            if (dataSize == 0) {
                adapter.loadMoreEnd();
            } else {
                ArrayList<VideoModel> temp = new ArrayList<>();
                for(int i = 0; i < response.getResult().size(); i++)
                {
                    VideoModel videoModel = response.getResult().get(i);
                    if(videoModel.getId() != currentVideo.getId())
                    {
                        temp.add(videoModel);
                    }
                }

                adapter.addData(temp);
                adapter.loadMoreComplete();

                if(currentPage == 0)
                    adapter.setOnLoadMoreListener(this, recyclerView);
            }
        }
        else
        {
            if (currentPage > 0) {
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
//        currentPage = 0;
//        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage += dataSize;
                loadVideoRelate();
            }

        }, 1000);
    }
}
