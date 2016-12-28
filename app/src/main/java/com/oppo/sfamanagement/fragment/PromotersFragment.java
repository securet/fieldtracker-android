package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.EventsFragment;
import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewPromoterListAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class PromotersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    ListViewPromoterListAdapter adapter;
    Button btAddPromoter;
    ListView listView;
    ArrayList<Promoter> list;
    ProgressDialog pd;
    private int preLast;
    Preferences preferences;
    private int pageIndex = 0;
    private int pageSize = 10;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoters,container,false);
        preferences = new Preferences(getContext());
        listView = (ListView) view.findViewById(R.id.lvPromotersList);
        btAddPromoter = (Button) view.findViewById(R.id.btAddPromoter);

        btAddPromoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new AddPromoterFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        adapter = new ListViewPromoterListAdapter(getActivity(), R.layout.promoter_list_item,new ArrayList<Promoter>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.PROMOTER_LIST,String.valueOf(pageIndex),String.valueOf(pageSize)));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_LIST,b,PromotersFragment.this).forceLoad();
        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.PROMOTER_LIST:
                return new LoaderServices(getContext(), LoaderMethod.PROMOTER_LIST,args);
            default:
                return null;
        }
       // listView.addFooterView();
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        if(data != null && data instanceof ArrayList){

        }  else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
        if (list == null) {
            list = (ArrayList<Promoter>)data;
        } else {
            list.addAll((ArrayList<Promoter>) data);
        }
        adapter.refresh(list);

        getActivity().getLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Promoter p = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("promoter",p);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = new EditPromoterFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
        fm.executePendingTransactions();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        switch(view.getId())
        {
            case R.id.lvPromotersList:

                final int lastItem = firstVisibleItem + visibleItemCount;
                boolean isLast = preferences.getBoolean(Preferences.PROMOTERISLAST,false);

                if(lastItem == totalItemCount && !isLast)
                {
                    preferences.saveBoolean(Preferences.PROMOTERISLAST,true);
                    preferences.commit();

//				if(!isLoading && (i+i1 >=i2-3)&& !isLast){
//					isLoading = true;
//					increase Page Index;
//					Call ApI
//				}
                        pageIndex++;
                        System.out.println("index   "  + pageIndex);
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.PROMOTER_LIST,String.valueOf(pageIndex),String.valueOf(pageSize)));
                        b.putString(AppsConstant.METHOD, AppsConstant.GET);
                        getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_LIST,b,PromotersFragment.this).forceLoad();

                }
        }
    }
}
