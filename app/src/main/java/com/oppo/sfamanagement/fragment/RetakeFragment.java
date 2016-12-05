package com.oppo.sfamanagement.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class RetakeFragment extends Fragment {

    ImageView ivRetake;
    Button retake,confirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_retake_request,container,false);
        ivRetake = (ImageView) view.findViewById(R.id.ivRetake);
        retake = (Button) view.findViewById(R.id.btRetake);
        confirm = (Button) view.findViewById(R.id.btConfirm);
        String imagePath = getArguments().getString("image_taken");
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(new File(imagePath)));
        ivRetake.setImageBitmap(bitmap);
        return view;
    }

}

