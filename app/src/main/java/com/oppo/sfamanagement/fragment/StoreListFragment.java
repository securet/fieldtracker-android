package com.oppo.sfamanagement.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewStoreListAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Store;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class StoreListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    protected ListViewStoreListAdapter adapter;
    ArrayList<Store> list;
    protected Button btnAddStore;
    ProgressDialog pd;
    Preferences preferences;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores,container,false);
        btnAddStore = (Button)view.findViewById(R.id.btAddStore);
        listView = (ListView) view.findViewById(R.id.lvStoreList);

      //  listView = (ListView) view.findViewById(R.id.lvStoreList);
     /*   adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);*/
        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.Fragment fragment = new AddStoreFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();

            }
        });

        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.STORE_LIST));
        b.putString(AppsConstant.METHOD, AppsConstant.GET );
        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,b, StoreListFragment.this).forceLoad();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public void displayMessage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        pd.show();
        switch (id) {
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getActivity(),LoaderMethod.STORE_LIST,args);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        pd.dismiss();
        switch (loader.getId())
        {
            case LoaderConstant.STORE_LIST:
                if(data!=null && data instanceof ArrayList ){

                }
                 else {

                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,((ArrayList)data));


                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
                list = (ArrayList<Store>) data;
                adapter.refresh(list);
                break;
        }
        if (isAdded()) {
            getLoaderManager().destroyLoader(loader.getId());
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
