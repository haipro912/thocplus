package com.vttm.mochaplus.feature.mvp.contact;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.PhoneNumber;
import com.vttm.mochaplus.feature.utils.CommonUtils;

import java.util.List;

public class ContactAdapter extends BaseQuickAdapter<PhoneNumber, BaseViewHolder> {

    private Context context;
    private AbsInterface.OnItemListener onItemListener;

    public ContactAdapter(Context context, int id, List<PhoneNumber> datas, AbsInterface.OnItemListener onItemListener) {
        super(id, datas);
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final PhoneNumber model) {

        if (model != null) {
            if (holder.getView(R.id.tvName) != null && model.getName() != null)
            {
                holder.setText(R.id.tvName, model.getName());

                RoundedLetterView letterView = holder.getView(R.id.imvAvatar);
                String[] listChar = model.getName().split(" ");
                if(listChar.length > 1)
                {
                    letterView.setTitleText(listChar[0].toUpperCase().charAt(0) + "" + listChar[1].toUpperCase().charAt(0));
                }
                else if(listChar.length > 0)
                {
                    letterView.setTitleText(listChar[0].toUpperCase().charAt(0) + "");
                }
                letterView.setBackgroundColor(CommonUtils.getRandomColor());
            }

            if (holder.getView(R.id.tvPhone) != null && model.getJidNumber() != null)
                holder.setText(R.id.tvPhone, model.getJidNumber());
        }
    }


}
