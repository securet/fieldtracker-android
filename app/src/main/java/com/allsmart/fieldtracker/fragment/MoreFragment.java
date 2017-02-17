package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

public class MoreFragment extends Fragment /*implements LoaderManager.LoaderCallbacks<Object>*/ {

    private TextView store,promoter,myAccount,changePassword, leaveRequisition,leave,tvPromoterApprovals,reportees;
    private Preferences preferences;
    private ArrayList<Store> list;

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
        reportees = (TextView) rootView.findViewById(R.id.tvReportees);
        if(((MainActivity)getActivity()).isManager()) {
            leaveRequisition.setVisibility(View.VISIBLE);
            tvPromoterApprovals.setVisibility(View.VISIBLE);
            reportees.setVisibility(View.VISIBLE);
        } else {
            leaveRequisition.setVisibility(View.GONE);
            tvPromoterApprovals.setVisibility(View.GONE);
            reportees.setVisibility(View.GONE);
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

        reportees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new ManagerAttendence();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flMiddle, f).addToBackStack(null).commit();
                fragmentManager.executePendingTransactions();
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Log Out")
                        .setMessage("Are you sure want to Log out")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(NetworkUtils.isNetworkConnectionAvailable(getContext())) {
                                    ((MainActivity)getActivity()).Logout();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("No Internet!");
                                    builder.setMessage("Please Connect Internet to Log Off");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

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
                Bundle b = new Bundle();
                Fragment fragment =(Fragment) new PromotersFragment();
                /*if(list != null) {
                    b.putParcelableArrayList("store_details",list);
                    fragment.setArguments(b);
                }*/
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

        /*Bundle bundle = new Bundle();
        bundle.putString(AppsConstant.METHOD,AppsConstant.GET);
        bundle.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.STORE_LIST));
        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,bundle,MoreFragment.this).forceLoad();*/
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


   /* @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getContext(), LoaderMethod.STORE_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(data != null && data instanceof ArrayList) {
            list = (ArrayList<Store>) data;
        } else {
            ((MainActivity)getActivity()).displayMessage("Error in response");
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }*/
}
