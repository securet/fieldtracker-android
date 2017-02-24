package com.oppo.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.activity.MainActivity;
import com.oppo.fieldtracker.adapter.ListViewHistoryAdapter;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.constants.LoaderConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.constants.Services;
import com.oppo.fieldtracker.model.HistoryNew;
import com.oppo.fieldtracker.model.Manager;
import com.oppo.fieldtracker.service.LoaderServices;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.utils.Logger;
import com.oppo.fieldtracker.utils.UrlBuilder;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 17-02-2017.
 */

public class ReporteeHistory extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    protected ListViewHistoryAdapter adapter;
    protected ListView listView;
    ArrayList<HistoryNew> list= new ArrayList<>();
    private int pageIndex = -1;
    private ImageView ivLoader;
    private View footerView;
    private boolean isLoading = false;
    private Preferences preferences;
    private String username="";
    private Manager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            manager = getArguments().getParcelable("manager");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("Log", e);
            Crashlytics.log(1, getClass().getName(), "Error in getting Arguments at ReporteeHistory");
            Crashlytics.logException(e);
        }

        pageIndex = 0;
        preferences = new Preferences(getContext());
        Bundle bundle = new Bundle();
        if(manager != null) {
           // Log.d(MainActivity.TAG,manager.getUsername()  +  "   " + manager.getAgentName());
            bundle.putString(AppsConstant.URL, UrlBuilder.getReporteeHistory(Services.REPORTEE_HISTORY,manager.getUsername(),pageIndex+"","10"));
            bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_HISTORY,bundle,ReporteeHistory.this).forceLoad();
        } else {
            ((MainActivity)getActivity()).displayMessage("No username for the reportee");
        }

    }

    public void showLoader() {

        Animation rotateXaxis = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_x_axis);
        rotateXaxis.setInterpolator(new LinearInterpolator());
        ivLoader.setAnimation(rotateXaxis);
        listView.addFooterView(footerView);
        footerView.setVisibility(View.VISIBLE);

    }
    public void hideloader() {
        listView.removeFooterView(footerView);
        footerView.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events,container,false);
        listView = (ListView) rootView.findViewById(R.id.expandableList);

        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);

        adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        hideloader();
        listView.setOnScrollListener(this);
        return rootView;
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
            case LoaderConstant.REPORTEE_HISTORY:
                return new LoaderServices(getContext(), LoaderMethod.REPORTEE_HISTORY,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(pageIndex==0 ){
            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).showHideProgressForLoder(true);
            }
        }else{
            hideloader();
        }
        if(data != null && data instanceof ArrayList) {
            if(preferences.getInt(Preferences.REPORTEE_HISTORY_COUNT,0) == 0) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("No Entries");
                dialog.setMessage("No Entries for " + manager.getAgentName());
                dialog.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fm = getFragmentManager();
                        fm.popBackStack();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                if(list == null) {
                    list = (ArrayList<HistoryNew>) data;
                }else{
                    list.addAll((ArrayList<HistoryNew>) data);
                }
            }

        } else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }

        isLoading = false;
        ((MainActivity)getActivity()).isLoading = false;
        adapter.Refresh(list);
        if(getActivity() != null) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            default:
                Fragment f = new HistoryListTrackFragment();
                FragmentManager fm = getFragmentManager();
                Bundle b = new Bundle();
                HistoryNew hn = list.get(position);
                b.putParcelable("sub_history",hn);
                b.putInt("position",position);
                f.setArguments(b);
                fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                fm.executePendingTransactions();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = preferences.getInt(Preferences.REPORTEE_HISTORY_COUNT,0);
        //System.out.println(count + "   count onScroll");
//				if(!isLoading && (i+i1 >=i2-3)&& !isLast){
//					isLoading = true;
//					increase Page Index;
//					Call ApI
//				}
        if (count > totalItemCount && totalItemCount > 0 && !isLoading && (firstVisibleItem + visibleItemCount >= totalItemCount - 3)) {
            ((MainActivity) getActivity()).isLoading = true;
            isLoading = true;
            pageIndex++;
            System.out.println(pageIndex + "   onScroll");
            Bundle bundle = new Bundle();
            if(manager != null) {
                bundle.putString(AppsConstant.URL, UrlBuilder.getReporteeHistory(Services.REPORTEE_HISTORY,
                        manager.getUsername(),pageIndex+"","10"));
                bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
                getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_HISTORY,bundle,ReporteeHistory.this);
            } else {
                ((MainActivity)getActivity()).displayMessage("No username for the reportee");
            }
        }
    }
}
