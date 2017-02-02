package com.allsmart.fieldtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;

public class MoreFragment extends Fragment {

    private TextView store,promoter,myAccount,changePassword, leaveRequisition,leave,tvPromoterApprovals;
    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_option, container, false);
        TextView tvLogout = (TextView) rootView.findViewById(R.id.tvLogout);
        myAccount = (TextView) rootView.findViewById(R.id.tvMyAccount);
        changePassword = (TextView) rootView.findViewById(R.id.tvChangePassword);
        leaveRequisition = (TextView) rootView.findViewById(R.id.tvLeaveRequisition);
        tvPromoterApprovals = (TextView) rootView.findViewById(R.id.tvPromoterApprovals);
        leave = (TextView) rootView.findViewById(R.id.tvLeave);
        if(((MainActivity)getActivity()).isManager()) {
            leaveRequisition.setVisibility(View.VISIBLE);
            tvPromoterApprovals.setVisibility(View.VISIBLE);
        } else {
            leaveRequisition.setVisibility(View.GONE);
            tvPromoterApprovals.setVisibility(View.GONE);
        }
        tvPromoterApprovals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new PromoterApprovalsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                fragmentManager.executePendingTransactions();
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).Logout();
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LeaveStatusFragment();
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt("leave_requisition",2);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });
        leaveRequisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new LeaveRequisitionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("leave_requisition",1);
                f.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                fm.executePendingTransactions();
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
                Fragment f = new ContactSupportFragment();
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
