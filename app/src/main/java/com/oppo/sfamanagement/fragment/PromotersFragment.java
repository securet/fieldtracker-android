package com.oppo.sfamanagement.fragment;

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

import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewPromoterListAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class PromotersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AdapterView.OnItemClickListener {

    ListViewPromoterListAdapter adapter;
    Button btAddPromoter;
    ListView listView;
    ArrayList<Promoter> list;
    ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoters,container,false);
   /*     ArrayList<String> promoterList = new ArrayList<>();
        promoterList.add("Rahul Kumar");
        promoterList.add("Abhishek Lal");
        promoterList.add("Aditya Sharma");
        promoterList.add("Amith Shah");
        promoterList.add("Mahesh Jai");
        promoterList.add("Rohith Pai"); */

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

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.PROMOTER_LIST));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_LIST,b,PromotersFragment.this).forceLoad();
        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        new LoginActivity().showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.PROMOTER_LIST:
                return new LoaderServices(getContext(), LoaderMethod.PROMOTER_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        new LoginActivity().showHideProgressForLoder(true);
        if(data != null && data instanceof ArrayList){

        }  else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
        adapter = new ListViewPromoterListAdapter(getActivity(), R.layout.promoter_list_item,(ArrayList) data);
        list = (ArrayList<Promoter>)data;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
}
