package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.adapter.PromoterApprovalsListAdapter;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.model.PromoterApprovals;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 31-01-2017.
 */

public class PromoterApprovalsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AdapterView.OnItemClickListener {
    private ArrayList<PromoterApprovals> list = new ArrayList<>();
    private RelativeLayout rlPromoter;
    private ListView listView;
    private int pageIndex = 0;
    private int pageSize = 10;
    private PromoterApprovalsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoters,container,false);
        rlPromoter = (RelativeLayout) view.findViewById(R.id.rlPromoter);
        rlPromoter.setVisibility(View.GONE);
        listView = (ListView) view.findViewById(R.id.lvPromotersList);

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.PROMOTER_APPROVALS_LIST,pageIndex+"",pageSize+""));
        b.putString(AppsConstant.METHOD,AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_APPROVALS_LIST,b,PromoterApprovalsFragment.this).forceLoad();
        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity) getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.PROMOTER_APPROVALS_LIST:
                return new LoaderServices(getContext(), LoaderMethod.PROMOTER_APPROVALS_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity) getActivity()).showHideProgressForLoder(true);
        switch (loader.getId()) {
            case LoaderConstant.PROMOTER_APPROVALS_LIST:
                if (data != null && data instanceof ArrayList) {
                    list = (ArrayList<PromoterApprovals>) data;
                    adapter = new PromoterApprovalsListAdapter(getContext(),R.layout.promoter_approvals_item,(ArrayList<PromoterApprovals>) data);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(this);
                } else {
                    Toast.makeText(getContext(),"Error in response",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment f = new PromoterApproveFragment();
        Bundle b = new Bundle();

            PromoterApprovals p = list.get(position);

        b.putParcelable("promoter_approve",p);
        f.setArguments(b);
        FragmentManager fm =getFragmentManager();
        fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
        fm.executePendingTransactions();
    }
}
