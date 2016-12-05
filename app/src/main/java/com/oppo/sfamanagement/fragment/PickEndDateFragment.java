package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class PickEndDateFragment extends PickStartDateFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_a_date_fragment,container,false);
        TextView tv = (TextView) view.findViewById(R.id.tvPickADate);
        tv.setText("Pick an End Date");
        return view;
    }
}
