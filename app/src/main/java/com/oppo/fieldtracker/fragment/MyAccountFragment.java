package com.oppo.fieldtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.activity.MainActivity;
import com.oppo.fieldtracker.storage.Preferences;

/**
 * Created by allsmartlt218 on 03-01-2017.
 */

public class MyAccountFragment extends Fragment {

    private TextView username,phone,email,address,storeName,managerName,managerPhone,managerEmail;
    private Preferences preferences;
    private LinearLayout layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment,container,false);
        username = (TextView) view.findViewById(R.id.userName);
        phone = (TextView) view.findViewById(R.id.phoneNum);
        email = (TextView) view.findViewById(R.id.emailAddress);
        layout = (LinearLayout) view.findViewById(R.id.llReportingManager);
        address = (TextView) view.findViewById(R.id.Address);
        storeName = (TextView) view.findViewById(R.id.storeName);
        managerName = (TextView) view.findViewById(R.id.managerName);
        managerPhone = (TextView) view.findViewById(R.id.managerPhoneNum);
        managerEmail = (TextView) view.findViewById(R.id.managerEmailAddress);
        preferences = new Preferences(getContext());

        username.setText(preferences.getString(Preferences.USERFULLNAME,""));
        phone.setText(preferences.getString(Preferences.USERPHONE,""));
        email.setText(preferences.getString(Preferences.USER_EMAIL,""));
        address.setText(preferences.getString(Preferences.USER_ADDRESS,""));
        storeName.setText(preferences.getString(Preferences.SITENAME,""));
        managerName.setText(preferences.getString(Preferences.REPORTEE_MANAGER_NAME,""));
        managerEmail .setText(preferences.getString(Preferences.REPORTEE_MANAGER_EMAIL,""));
        managerPhone.setText(preferences.getString(Preferences.REPORTEE_MANAGER_PHONE,""));

        if(((MainActivity)getActivity()).isManager()) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
