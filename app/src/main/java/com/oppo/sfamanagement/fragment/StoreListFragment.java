package com.oppo.sfamanagement.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.oppo.sfamanagement.adapter.ListViewStoreListAdapter;
import com.oppo.sfamanagement.model.Store;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class StoreListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    protected ListViewStoreListAdapter adapter;
    protected Button btnAddStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores,container,false);
        btnAddStore = (Button)view.findViewById(R.id.btAddStore);
        ArrayList<String> list = new ArrayList<>();
        list.add("OPPOBHPL");
        list.add("OPPOAPNG");
        list.add("OPPOTTFL");
        list.add("OPPOTRRD");
        list.add("OPPOBBGF");
        list.add("OPPOHGTT");
        ListView listView = (ListView) view.findViewById(R.id.lvStoreList);
        adapter = new ListViewStoreListAdapter(getActivity(),R.layout.store_list_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.Fragment fragment = new AddStoreFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            default:
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.Fragment fragment = new EditStoreFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
        }
    }

    public void displayMessage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }
}
