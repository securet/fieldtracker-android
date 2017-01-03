package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.Preferences;

/**
 * Created by allsmartlt218 on 03-01-2017.
 */

public class MyAccountFragment extends Fragment {

    private TextView username,phone,email,address,storeName;
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
        preferences = new Preferences(getContext());

        username.setText(preferences.getString(Preferences.USERFULLNAME,""));
        phone.setText(preferences.getString(Preferences.PARTYID,""));
        email.setText(preferences.getString(Preferences.USEREMAIL,""));
        address.setText(preferences.getString(Preferences.SITE_ADDRESS,""));
        storeName.setText(preferences.getString(Preferences.SITENAME,""));
        return view;
    }
}
