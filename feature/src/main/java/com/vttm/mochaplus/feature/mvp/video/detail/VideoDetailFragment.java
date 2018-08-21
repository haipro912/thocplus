package com.vttm.mochaplus.feature.mvp.video.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.api.response.VideoDetailResponse;
import com.vttm.mochaplus.feature.data.api.response.VideoResponse;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.VideoModel;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;
import com.vttm.mochaplus.feature.mvp.video.detail.utils.SnapTopLinearSmoothScroller;
import com.vttm.mochaplus.feature.utils.AppConstants;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import im.ene.toro.PlayerSelector;
import im.ene.toro.ToroPlayer;
import im.ene.toro.widget.Container;
import io.reactivex.Observable;

public class VideoDetailFragment extends BaseFragment implements AbsInterface.OnItemListener, IVideoDetailView {
    private Container container;
    private RecyclerView.LayoutManager layoutManager;
    private VideoDetailAdapter adapter;
    private ImageView btnBack;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<VideoModel> datas = new ArrayList<>();
    private String lastId = "";
    private int dataSize;
    private VideoModel currentVideo;
    private PlayerSelector selector;

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
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        btnBack = view.findViewById(R.id.btnBack);

        layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void smoothScrollToPosition(RecyclerView view, RecyclerView.State state, int position) {
                LinearSmoothScroller linearSmoothScroller =
                        new SnapTopLinearSmoothScroller(view.getContext());
                linearSmoothScroller.setTargetPosition(position);
                super.startSmoothScroll(linearSmoothScroller);
            }
        };

        container = view.findViewById(R.id.recycler_view);
        container.setLayoutManager(layoutManager);
        adapter = new VideoDetailAdapter(datas);
        container.setAdapter(adapter);
        container.setCacheManager(adapter);

        adapter.setOnCompleteCallback(new VideoDetailAdapter.OnCompleteCallback() {
            @SuppressLint("CheckResult")
            @Override
            void onCompleted(ToroPlayer player) {
                int position = adapter.findNextPlayerPosition(player.getPlayerOrder());
                //noinspection Convert2MethodRef,ResultOfMethodCallIgnored
                Observable.just(container)
                        .delay(250, TimeUnit.MILLISECONDS)
                        .filter(c -> c != null)
                        .subscribe(rv -> rv.smoothScrollToPosition(position));
            }
        });

//        // Backup active selector.
        selector = container.getPlayerSelector();

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
                container.postDelayed(new Runnable() {
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
                container.setVisibility(View.GONE);
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
        adapter.notifyDataSetChanged();

        if(shimmerFrameLayout != null)
        {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

        //Load video lien quan
        container.postDelayed(new Runnable() {
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
                //K co du lieu
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

                int index = datas.size();
                datas.addAll(temp);
                adapter.notifyItemInserted(index);
            }
        }
        else
        {
            if (currentPage > 0) {
                //load more fail
            }
        }
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemMoreClick(int pos) {

    }
}
