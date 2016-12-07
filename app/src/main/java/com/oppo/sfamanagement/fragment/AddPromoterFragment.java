package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddPromoterFragment extends Fragment implements View.OnClickListener {

    protected static final int FRONT_CAMREA_OPEN = 1;
    protected static final int BACK_CAMREA_OPEN = 2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_promoter,container,false);
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        ImageView ivAdhar = (ImageView) view.findViewById(R.id.ivAadhar);
        ImageView ivAddressProof = (ImageView) view.findViewById(R.id.ivAddressProof);
        ivPhoto.setOnClickListener(this);
        ivAdhar.setOnClickListener(this);
        ivAddressProof.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhoto:
                Fragment fragment = new CameraFragment();
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt("camera_key",FRONT_CAMREA_OPEN);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
                break;
            case R.id.ivAadhar:
                Fragment fragment2 = new CameraFragment();
                FragmentManager fm2 = getFragmentManager();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("camera_key",BACK_CAMREA_OPEN);
                fragment2.setArguments(bundle2);
                fm2.beginTransaction().replace(R.id.flMiddle,fragment2).addToBackStack(null).commit();
                fm2.executePendingTransactions();
                break;
            case R.id.ivAddressProof:
                Fragment fragment3 = new CameraFragment();
                FragmentManager fm3 = getFragmentManager();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("camera_key",BACK_CAMREA_OPEN);
                fragment3.setArguments(bundle3);
                fm3.beginTransaction().replace(R.id.flMiddle,fragment3).addToBackStack(null).commit();
                fm3.executePendingTransactions();
                break;
        }
    }
}
