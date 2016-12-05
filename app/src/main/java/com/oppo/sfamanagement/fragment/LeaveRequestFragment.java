package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveRequestFragment extends Fragment implements View.OnClickListener {

    EditText etStart,etEnd;
    ImageView ivStart,ivEnd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_request,container,false);
        etStart = (EditText) view.findViewById(R.id.etStartDate);
        etEnd = (EditText) view.findViewById(R.id.etEndDate);
        ivStart = (ImageView) view.findViewById(R.id.ivDatePicker);
        ivEnd = (ImageView) view.findViewById(R.id.ivDatePicker2);
        etStart.setOnClickListener(this);
        etEnd.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivEnd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.etStartDate || v.getId() == R.id.ivDatePicker) {
           Fragment fragment = new PickStartDateFragment();
           FragmentManager fm = getFragmentManager();
           fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
       } else if(v.getId() == R.id.etEndDate || v.getId() == R.id.ivDatePicker2) {
           Fragment fragment = new PickEndDateFragment();
           FragmentManager fm = getFragmentManager();
           fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
       }
    }
}
