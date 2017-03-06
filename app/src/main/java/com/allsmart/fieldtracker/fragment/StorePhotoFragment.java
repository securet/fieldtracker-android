package com.allsmart.fieldtracker.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.utils.UrlBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;

/**
 * Created by allsmartlt218 on 01-03-2017.
 */

public class StorePhotoFragment extends DialogFragment{

    private Button btBack;
    private ImageView storePhoto;
    private String photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_photo,container,false);

        btBack = (Button) view.findViewById(R.id.btBack);
        storePhoto = (ImageView) view.findViewById(R.id.storePhoto);

        try {
            photo = getArguments().getString("store_photo");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(photo) && !photo.equals(null)) {
           /* RequestCreator var = Picasso.with(getContext()).load(UrlBuilder.getServerImage(photo));
            Bitmap bmp = null;
            try {
                bmp = var.get();
                Log.d(MainActivity.TAG,bmp.getHeight()+ "  " + bmp.getWidth());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Picasso.with(getContext()).load(UrlBuilder.getServerImage(photo)).resize(768,1024).into(storePhoto);

        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);


        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
