package com.oppo.fieldtracker.fragment;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.activity.MainActivity;
import com.oppo.fieldtracker.adapter.AttendenceManagerAdapter;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.constants.LoaderConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.constants.Services;
import com.oppo.fieldtracker.model.Manager;
import com.oppo.fieldtracker.service.LoaderServices;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 25-01-2017.
 */

public class ManagerAttendence extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object>, AbsListView.OnScrollListener {

    private ListView listView;
    private Preferences preferences;
    private AttendenceManagerAdapter adapter;
    private ArrayList<Manager> arrayList = new ArrayList<>();
    private int pageIndex = -1;
    private ImageView ivLoader;
    private int pageSize = 10;
    private boolean isLoading = false;
    private View footerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
        pageIndex = 0;
        System.out.println(pageIndex + "  before scroll");
        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.REPORTEE_LIST,pageIndex+"",String.valueOf(pageSize)));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_LIST,b,ManagerAttendence.this).forceLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_atendence_list,container,false);
        listView = (ListView) view.findViewById(R.id.lvManager);
        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);
        adapter = new AttendenceManagerAdapter(getContext(),R.layout.manager_attendence_item,arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        /*Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.REPORTEE_LIST,pageIndex+"",String.valueOf(pageSize)));
     //   b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.REPORTEE_LIST));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_LIST,b,ManagerAttendence.this).forceLoad();*/
        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            default:
                if(arrayList != null) {
                    Manager m = arrayList.get(position);
                    Bundle b = new Bundle();
                    b.putParcelable("manager",m);
                    Fragment f = new ReporteeHistory();
                    FragmentManager fm = getFragmentManager();
                    f.setArguments(b);
                    fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                    fm.executePendingTransactions();
                }

        }
    }

    /*@Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;
        int count = preferences.getInt(Preferences.PROMOTER_COUNT,0);
        if(count> totalItemCount && totalItemCount>0 && !isLoading && ( lastItem >= totalItemCount - 3) )
        {
            ((MainActivity)getActivity()).isLoading = true;
            isLoading = true;
            pageIndex++;
            System.out.println("index   " + pageIndex);
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.REPORTEE_LIST,pageIndex+"", pageSize+""));
            b.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_LIST, b, ManagerAttendence.this).forceLoad();
        }
    }*/

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

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        isLoading = true;
        ((MainActivity)getActivity()).isLoading = true;
        if (pageIndex == 0 ) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }else{
            showLoader();
        }
        switch (id) {
            case LoaderConstant.REPORTEE_LIST:
                return new LoaderServices(getContext(), LoaderMethod.REPORTEE_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(pageIndex==0 ){
            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHideProgressForLoder(true);
            }
        }else{
            hideloader();
        }

        switch (loader.getId()) {
            case LoaderConstant.REPORTEE_LIST:
                if(data != null && data instanceof ArrayList) {
                    if (arrayList == null) {
                        arrayList = (ArrayList<Manager>)data;
                    } else {
                        arrayList.addAll((ArrayList<Manager>) data);
                    }
                   /* arrayList = (ArrayList<Manager>) data;
                    adapter = new AttendenceManagerAdapter(getContext(),R.layout.manager_attendence_item,arrayList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(this);*/

                } else {
                    ((MainActivity)getActivity()).displayMessage("Error in response, please try again!");
                }
                isLoading = false;
                ((MainActivity)getActivity()).isLoading = false;
                adapter.refresh(arrayList);
                break;
        }

        if(getActivity() != null && getActivity() instanceof MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = preferences.getInt(Preferences.REPORTEE_LIST_COUNT,0);
        if(count> totalItemCount && totalItemCount>0 && !isLoading && (firstVisibleItem + visibleItemCount >= totalItemCount - 3) )
        {
            ((MainActivity)getActivity()).isLoading = true;
            isLoading = true;
            pageIndex++;
            System.out.println("index   " + pageIndex);
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.REPORTEE_LIST,pageIndex+"", pageSize+""));
            b.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.REPORTEE_LIST, b, ManagerAttendence.this).forceLoad();
        }
    }
}
