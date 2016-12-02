package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewPromoterListAdapter;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class PromotersFragment extends Fragment {

    ListViewPromoterListAdapter adapter;
    Button btAddPromoter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promoters,container,false);
        ArrayList<String> promoterList = new ArrayList<>();
        promoterList.add("Rahul Kumar");
        promoterList.add("Abhishek Lal");
        promoterList.add("Aditya Sharma");
        promoterList.add("Amith Shah");
        promoterList.add("Mahesh Jai");
        promoterList.add("Rohith Pai");
        ListView listView = (ListView) view.findViewById(R.id.lvPromotersList);
        btAddPromoter = (Button) view.findViewById(R.id.btAddPromoter);
        adapter = new ListViewPromoterListAdapter(getActivity(), R.layout.promoter_list_item,promoterList);
        listView.setAdapter(adapter);
        btAddPromoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new AddPromoterFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        return view;
    }
}
