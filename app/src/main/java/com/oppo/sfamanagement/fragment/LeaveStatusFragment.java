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
import com.oppo.sfamanagement.adapter.ListViewLeaveStatusListAdapter;
import com.oppo.sfamanagement.model.Leave;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveStatusFragment extends Fragment {

    private ArrayList<Leave> list;
    private Button btLeaveRequest;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leave_status_list,container,false);
        ListView lvLeave = (ListView) view.findViewById(R.id.lvLeaveStatus);
        btLeaveRequest = (Button) view.findViewById(R.id.btLeaveRequest);
        list = new ArrayList<>();
        hardCodeDataForLeaveStatus(list);
        ListViewLeaveStatusListAdapter adapter = new ListViewLeaveStatusListAdapter(getActivity(),R.layout.leave_status_list_item,list);
        lvLeave.setAdapter(adapter);
        btLeaveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LeaveRequestFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        return view;
    }

    public void hardCodeDataForLeaveStatus(ArrayList<Leave> list) {
        Leave a = new Leave();
        Leave b = new Leave();
        Leave c = new Leave();
        a.setDays("4 Days");
        a.setFromDate("11/10/2016");
        a.setToDate("14/10/2016");
        a.setStatus("Pending");
        a.setReason("Casual Leave");
        list.add(a);
        b.setDays("1 Day");
        b.setFromDate("26/09/2016");
        b.setToDate("26/09/2016");
        b.setStatus("Approved");
        b.setReason("Sick Leave");
        list.add(b);
        c.setDays("3 Days");
        c.setFromDate("16/06/2016");
        c.setToDate("18/06/2016");
        c.setStatus("Rejected");
        c.setReason("Casual Leave");
        list.add(c);
    }
}
