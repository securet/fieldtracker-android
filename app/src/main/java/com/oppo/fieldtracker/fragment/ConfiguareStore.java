package com.oppo.fieldtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppo.fieldtracker.R;

/**
 * Created by allsmartlt218 on 23-02-2017.
 */

public class ConfiguareStore extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configure_store,container,false);
        return view;
    }
}
