package com.vttm.mochaplus.feature.mvp.contact;

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
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.data.db.model.ContactConstant;
import com.vttm.mochaplus.feature.interfaces.AbsInterface;
import com.vttm.mochaplus.feature.model.PhoneNumber;
import com.vttm.mochaplus.feature.mvp.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ContactFragment extends BaseFragment implements AbsInterface.OnItemListener, SwipeRefreshLayout.OnRefreshListener, IContactView {

    private View loadingView;
    private RecyclerView recyclerView;

    private ContactAdapter adapter;
    private  LinearLayoutManager layoutManager;
    private View notDataView, errorView;
    private ArrayList<PhoneNumber> datas = new ArrayList<>();

    @Inject
    IContactPresenter<IContactView> presenter;

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_base, container, false);

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

        adapter = new ContactAdapter(getBaseActivity(), R.layout.item_contact, datas, this);
        adapter.setEnableLoadMore(false);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), getResources().getDimensionPixelOffset(R.dimen.padding10), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseActivity(), layoutManager.getOrientation());
//        dividerItemDecoration.setDrawable(getBaseActivity().getResources().getDrawable(R.drawable.divider_vertical));
//        recyclerView.addItemDecoration(dividerItemDecoration);

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

        loadData();
    }

    private void loadData()
    {
        loadingView.setVisibility(View.VISIBLE);
        List<ContactConstant> listContact = presenter.loadContact();

        datas.clear();
        for(int i = 0; i < listContact.size(); i++)
        {
            PhoneNumber contact = new PhoneNumber();
            ContactConstant phoneNumber = listContact.get(i);
            contact.setId(phoneNumber.getNumber_id());
            contact.setContactId(phoneNumber.getContact_id());
            contact.setName(phoneNumber.getName());
            contact.setFavorite(phoneNumber.getFavorite());
            contact.setJidNumber(phoneNumber.getNumber());

            datas.add(contact);
        }

        int mCurrentCounter = datas.size();
        if (mCurrentCounter == 0) {
            adapter.setEmptyView(notDataView);
        } else {
            adapter.notifyDataSetChanged();
        }

        loadingView.setVisibility(View.GONE);
        hideLoading();
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemMoreClick(int pos) {

    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
