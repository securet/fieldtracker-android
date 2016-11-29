package com.oppo.sfamanagement.database;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oppo.sfamanagement.LeaveFragment;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.MapFragment;
import com.oppo.sfamanagement.R;

public class MoreFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_option, container, false);
        TextView tvLogout = (TextView) rootView.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).Logout();
            }
        });

        TextView tvLeaveRequest = (TextView) rootView.findViewById(R.id.tvLeaveRequest);
        tvLeaveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = new LeaveFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flMiddle, f).addToBackStack("Leave").commit();
                fragmentManager.executePendingTransactions();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
