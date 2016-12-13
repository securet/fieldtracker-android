package com.oppo.sfamanagement.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraHandler2 extends Fragment {

    Bitmap bmp;
    Button confirm,cancel;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_handler_2,container,false);
        confirm = (Button) view.findViewById(R.id.btConfirm);
        cancel = (Button) view.findViewById(R.id.btRetake);
        imageView = (ImageView) view.findViewById(R.id.ivRetake);
        final String imagePath = getArguments().getString("image_taken");
        bmp = rotateBmp(BitmapFactory.decodeFile(imagePath));
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("response",imagePath);
                getActivity().setResult(2,i);
                getActivity().finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment f = new CameraHandler1();
                fm.beginTransaction().replace(R.id.flCapture,f).commit();
                fm.executePendingTransactions();
            }
        });
        return view;
    }
    public Bitmap rotateBmp(Bitmap bmp){
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(270);
        //supply the original width and height, if you don't want to change the height and width of bitmap.
        bmp = Bitmap.createBitmap(bmp, 0, 0, 1080, 1080, matrix, true);
        return bmp;
    }
}
