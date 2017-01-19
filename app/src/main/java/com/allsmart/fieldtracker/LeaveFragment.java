package com.allsmart.fieldtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LeaveFragment extends Fragment{

	private TextView email,phone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_leaves, container,false);
        email = (TextView) rootView.findViewById(R.id.tvContactEmail);
        phone = (TextView) rootView.findViewById(R.id.tvContactPhone);

        return rootView;
	}
}
