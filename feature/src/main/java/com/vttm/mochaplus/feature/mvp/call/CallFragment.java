package com.vttm.mochaplus.feature.mvp.call;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camnter.easyrecyclerview.widget.EasyRecyclerView;
import com.camnter.easyrecyclerviewsidebar.EasyFloatingImageView;
import com.camnter.easyrecyclerviewsidebar.EasyRecyclerViewSidebar;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class CallFragment extends BaseFragment implements ICallView  {

    private EasyRecyclerViewSidebar imageSidebar;
    private TextView imageFloatingTv;
    private EasyFloatingImageView imageFloatingIv;

    public SectionAdapter adapter;
    private EasyRecyclerView imageSectionRv;

    @Inject
    ICallPresenter<ICallView> presenter;

    public static CallFragment newInstance() {
        CallFragment fragment = new CallFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        setUp(view);

        return view;
    }

    @Override
    protected void setUp(View view) {
        this.imageSectionRv = (EasyRecyclerView) view.findViewById(R.id.section_rv);
        this.imageSidebar = (EasyRecyclerViewSidebar) view.findViewById(R.id.section_sidebar);
        this.imageFloatingTv = (TextView) view.findViewById(R.id.section_floating_tv);
        this.imageFloatingIv = (EasyFloatingImageView) view.findViewById(R.id.section_floating_iv);
        RelativeLayout imageFloatingRl = (RelativeLayout) view.findViewById(
                R.id.section_floating_rl);

        this.initAdapter();
        if (this.imageSectionRv != null) {
            this.imageSectionRv.setAdapter(this.adapter);
        }

        this.imageSidebar.setFloatView(imageFloatingRl);
//        this.imageSidebar.setOnTouchSectionListener(this);

        initData();
    }

    private void initData() {
        this.adapter.setList(this.getData());
        this.adapter.notifyDataSetChanged();
        this.imageSidebar.setSections(this.adapter.getSections());
    }


    public void initAdapter() {
        this.adapter = new RoundImageSectionAdapter();
    }


    public List<Contacts> getData() {
        List<ContactConstant> listContact = presenter.loadContact();

        List<Contacts> datas = new ArrayList<>();

        String[] letterArray = {"A", "B", "C", "D", "F", "H", "K", "P", "Q", "R", "U", "X" };
        Map<String, ArrayList<Contacts>> mapsContact = new HashMap<>();

        for (String letter : letterArray) {
            ArrayList<Contacts> contactListByLetter = new ArrayList<>();
            for(int i = 0; i < listContact.size(); i++)
            {
                ContactConstant phoneNumber = listContact.get(i);
                if(phoneNumber.getName().toLowerCase().startsWith(letter.toLowerCase()))
                {
                    Contacts contact = new Contacts();
                    contact.resId = 0;
                    contact.name = phoneNumber.getName();
                    contact.pinyin = phoneNumber.getName().toLowerCase();
                    contactListByLetter.add(contact);
                }
            }
            mapsContact.put(letter, contactListByLetter);
        }

        datas.clear();
        for (String letter : letterArray) {
            ArrayList<Contacts> contactListByLetter = mapsContact.get(letter);
            datas.addAll(contactListByLetter);
        }

        return datas;
    }
}
