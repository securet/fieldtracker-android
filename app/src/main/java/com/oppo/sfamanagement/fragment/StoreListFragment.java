package com.oppo.sfamanagement.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
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
    private ArrayList<Store> list ;
    protected Button btnAddStore;
    private ProgressDialog pd;
    ImageView ivLoader;
    private Preferences preferences;
    private ListView listView;
    private boolean isLoading = false;
    private View footerView;

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

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getActivity(),LoaderMethod.STORE_LIST,args);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        switch (loader.getId())
        {
            case LoaderConstant.STORE_LIST:
                if(data!=null && data instanceof ArrayList ){
                    if (list == null) {
                        /*list = (ArrayList<Store>) data;*/
                    } else {
                        list.addAll((ArrayList<Store>)data);
                    }
                }
                 else {

                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }



                list = (ArrayList<Store>) data;
                isLoading = false;
                adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
                adapter.refresh(list);
                break;
        }
            getActivity().getLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
