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
import com.oppo.sfamanagement.fragment.ChangePasswordFragment;
import com.oppo.sfamanagement.fragment.MyAccountFragment;
import com.oppo.sfamanagement.fragment.PromotersFragment;
import com.oppo.sfamanagement.fragment.StoreListFragment;

public class MoreFragment extends Fragment {

    private TextView store,promoter,myAccount,changePassword;
    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_option, container, false);
        TextView tvLogout = (TextView) rootView.findViewById(R.id.tvLogout);
        myAccount = (TextView) rootView.findViewById(R.id.tvMyAccount);
        changePassword = (TextView) rootView.findViewById(R.id.tvChangePassword);
        preferences = new Preferences(getContext());
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).Logout();
            }
        });

        store = (TextView)rootView.findViewById(R.id.store);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StoreListFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });

        promoter = (TextView) rootView.findViewById(R.id.tvPromoter);
        System.out.println(preferences.getString(Preferences.ROLETYPEID,"") + "        FieldExec");
        if(preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExecutiveOnPremise") ||
                preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExectiveOffPremise")) {
            promoter.setVisibility(View.GONE);
            store.setVisibility(View.GONE);
        }
        else {
            promoter.setVisibility(View.VISIBLE);
            store.setVisibility(View.VISIBLE);
        }
        promoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =(Fragment) new PromotersFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
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
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyAccountFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new ChangePasswordFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



}
