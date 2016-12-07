package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddPromoterArrowFragment extends Fragment implements View.OnClickListener {



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_promoter_arrow,container,false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
