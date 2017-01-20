package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.adapter.ListViewLeaveStatusListAdapter;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Leave;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveStatusFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private ArrayList<Leave> list = new ArrayList<>();
    private Button btLeaveRequest;
    private int pageIndex = -1;
    private Preferences preferences;
    private int pageSize = 10;
    private View footerView;
    private ImageView ivLoader;
    private ListView lvLeave;
    private boolean isLoading = false;
    private ListViewLeaveStatusListAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
        pageIndex = 0;
        System.out.println(pageIndex + "  before scroll");
        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getLeaveList(Services.LEAVE_LIST,pageIndex+"",String.valueOf(pageSize)));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.LEAVE_LIST,b,LeaveStatusFragment.this).forceLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leave_status_list,container,false);
        lvLeave = (ListView) view.findViewById(R.id.lvLeaveStatus);
        btLeaveRequest = (Button) view.findViewById(R.id.btLeaveRequest);
        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);
        adapter = new ListViewLeaveStatusListAdapter(getActivity(),R.layout.leave_status_list_item,list);
        lvLeave.setAdapter(adapter);
        lvLeave.setOnItemClickListener(this);

        lvLeave.setOnScrollListener(this);
        btLeaveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LeaveRequestFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        return view;
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).isLoading = true;
        isLoading = true;
        if (pageIndex == 0 ) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }else{
            showLoader();
        }
        switch (id) {
            case LoaderConstant.LEAVE_LIST:
                return new LoaderServices(getContext(), LoaderMethod.LEAVE_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(pageIndex==0 ){
            ((MainActivity)getActivity()).showHideProgressForLoder(true);
        }else{
            hideloader();
        }
        switch (loader.getId()) {
            case LoaderConstant.LEAVE_LIST:
                if(data != null && data instanceof ArrayList) {
                    if (list == null) {
                        list = (ArrayList<Leave>) data;
                    } else {
                        list.addAll((ArrayList<Leave>) data);
                    }

                }else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
                ((MainActivity)getActivity()).isLoading = false;
                adapter.refresh(list);
                break;

        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());
    }

    public void showLoader() {

        Animation rotateXaxis = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_x_axis);
        rotateXaxis.setInterpolator(new LinearInterpolator());
        ivLoader.setAnimation(rotateXaxis);
        lvLeave.addFooterView(footerView);
        footerView.setVisibility(View.VISIBLE);
    }
    public void hideloader() {
        lvLeave.removeFooterView(footerView);
        footerView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch(view.getId())
        {
            case R.id.lvLeaveStatus:

                final int lastItem = firstVisibleItem + visibleItemCount;
                int count = preferences.getInt(Preferences.LEAVE_COUNT,0);
                if(count>totalItemCount&& totalItemCount>0 && !isLoading && (lastItem >= totalItemCount-3) )
                {
                    isLoading = true;
                    ((MainActivity)getActivity()).isLoading = true;
                    pageIndex++;
                    System.out.println("index   " + pageIndex);
                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL, UrlBuilder.getLeaveList(Services.LEAVE_LIST, String.valueOf(pageIndex), String.valueOf(pageSize)));
                    b.putString(AppsConstant.METHOD, AppsConstant.GET);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.LEAVE_LIST, b, LeaveStatusFragment.this).forceLoad();
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Leave leave = list.get(position);
        Bundle b = new Bundle();
        b.putParcelable("leave_key",leave);
        if(leave.getStatus().equalsIgnoreCase("Pending") || leave.getStatus().equalsIgnoreCase("Rejected")) {
            Fragment f = new EditLeaveFragment();
            f.setArguments(b);

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
            fm.executePendingTransactions();
        }

    }

}
