package com.allsmart.fieldtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.adapter.AttendenceManagerAdapter;
import com.allsmart.fieldtracker.model.Manager;
import com.allsmart.fieldtracker.storage.Preferences;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 25-01-2017.
 */

public class ManagerAttendence extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private AttendenceManagerAdapter adapter;
    private ArrayList<Manager> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_atendence_list,container,false);
        listView = (ListView) view.findViewById(R.id.lvManager);
        adapter = new AttendenceManagerAdapter(getContext(),R.layout.manager_attendence_item,getHardCodeData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    private ArrayList<Manager> getHardCodeData() {
        ArrayList<Manager> list = new ArrayList<>();
        Manager m1 = new Manager("Agent Name 1","Store Name 1","Status 1");
        Manager m2 = new Manager("Agent Name 2","Store Name 2","Status 2");
        Manager m3 = new Manager("Agent Name 3","Store Name 3","Status 3");
        Manager m4 = new Manager("Agent Name 4","Store Name 4","Status 4");
        Manager m5 = new Manager("Agent Name 5","Store Name 5","Status 5");
        Manager m6 = new Manager("Agent Name 6","Store Name 6","Status 6");
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        list.add(m6);
        arrayList = list;
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            default:
                if(arrayList != null) {
                    Manager m = arrayList.get(position);
                    Bundle b = new Bundle();
                    b.putParcelable("manager",m);
                    Fragment f = new HistoryListFragment();
                    FragmentManager fm = getFragmentManager();
                    f.setArguments(b);
                    fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                    fm.executePendingTransactions();
                }

        }
    }
}
