package com.allsmart.fieldtracker.fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.adapter.ListViewStoreListAdapter;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class StoreListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks, AbsListView.OnScrollListener {

    protected ListViewStoreListAdapter adapter;
    private ArrayList<Store> list = new ArrayList<>();
    protected Button btnAddStore;
   // private ProgressDialog pd;
    private ImageView ivLoader;
    private Preferences preferences;
    private ListView listView;
    private boolean isLoading = false;
    private int pageIndex = -1;
    private int pageSize = 10;
    private View footerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());

        pageIndex = 0;
        System.out.println(pageIndex + "  before scroll");

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.STORE_LIST,pageIndex+"",pageSize+""));
        b.putString(AppsConstant.METHOD, AppsConstant.GET );
        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,b, StoreListFragment.this).forceLoad();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores,container,false);
        btnAddStore = (Button)view.findViewById(R.id.btAddStore);
        listView = (ListView) view.findViewById(R.id.lvStoreList);
        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null,false);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);


        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.Fragment fragment = new AddStoreFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();

            }
        });
        hideloader();

        adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Store s = list.get(position);
        Bundle b = new Bundle();
        b.putParcelable("Store",s);
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.Fragment fragment = new EditStoreFragment();
        fragment.setArguments(b);
        fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
        fm.executePendingTransactions();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        isLoading = true;
        ((MainActivity)getActivity()).isLoading = true;
        if (pageIndex == 0 ) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }else{
            showLoader();
        }
        switch (id) {
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getActivity(),LoaderMethod.STORE_LIST,args);
            default:
                return null;
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

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(pageIndex==0 ){
            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHideProgressForLoder(true);
            }
        }else{
            hideloader();
        }

        switch (loader.getId())
        {
            case LoaderConstant.STORE_LIST:
                if(data!=null && data instanceof ArrayList ){
                    if(preferences.getInt(Preferences.STORE_LIST_COUNT,0) == 0) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false);
                        dialog.setTitle("No Stores");
                        dialog.setMessage("No Stores to show");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        if (list == null) {
                            list = (ArrayList<Store>) data;
                        } else {
                            list.addAll((ArrayList<Store>)data);
                        }
                    }

                    if(list != null) {
                        if(adapter != null) {
                            adapter.refresh(list);
                        }
                    }

                }
                 else {

                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }



           //     list = (ArrayList<Store>) data;
                isLoading = false;
                ((MainActivity)getActivity()).isLoading = false;
          //      adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,list);
           //     listView.setAdapter(adapter);
          //      listView.setOnItemClickListener(this);


                break;
        }
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = preferences.getInt(Preferences.STORE_LIST_COUNT,0);
        if(count> totalItemCount && totalItemCount>0 && !isLoading && (firstVisibleItem + visibleItemCount >= totalItemCount - 3) )
        {
            ((MainActivity)getActivity()).isLoading = true;
            isLoading = true;
            pageIndex++;
            System.out.println("index   " + pageIndex);
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.STORE_LIST,pageIndex+"",pageSize+""));
            b.putString(AppsConstant.METHOD, AppsConstant.GET );
            getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,b, StoreListFragment.this).forceLoad();
        }
    }
}
