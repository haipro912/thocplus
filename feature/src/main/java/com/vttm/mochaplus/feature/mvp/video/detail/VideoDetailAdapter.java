package com.vttm.mochaplus.feature.mvp.video.detail;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.helper.image.ImageLoader;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.VideoModel;

import java.util.List;

public class VideoDetailAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {

    private Context context;
    private AbsInterface.OnItemListener onItemListener;

    public VideoDetailAdapter(Context context, int id, List<VideoModel> datas, AbsInterface.OnItemListener onItemListener) {
        super(id, datas);
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoModel model) {

        if (model != null) {
            ImageLoader.setImage(context, model.getImagePath(), (ImageView) holder.getView(R.id.imvImage));
            holder.setText(R.id.tvTitle, model.getName());
        }
    }
}