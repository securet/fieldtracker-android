package com.allsmart.fieldtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.storage.Preferences;

/**
 * Created by allsmartlt218 on 03-01-2017.
 */

public class MyAccountFragment extends Fragment {

    private TextView username,phone,email,address,storeName,managerName,managerPhone,managerEmail;
    private Preferences preferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment,container,false);
        username = (TextView) view.findViewById(R.id.userName);
        phone = (TextView) view.findViewById(R.id.phoneNum);
        email = (TextView) view.findViewById(R.id.emailAddress);
        address = (TextView) view.findViewById(R.id.Address);
        storeName = (TextView) view.findViewById(R.id.storeName);
        managerName = (TextView) view.findViewById(R.id.managerName);
        managerPhone = (TextView) view.findViewById(R.id.managerPhoneNum);
        managerEmail = (TextView) view.findViewById(R.id.managerEmailAddress);
        preferences = new Preferences(getContext());

        username.setText("Agent Name");
        phone.setText("Phone Number");
        email.setText("Email Address");
        address.setText("Address");
        storeName.setText(preferences.getString(Preferences.SITENAME,""));
        return view;
    }
}
