package com.oppo.sfamanagement.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
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
    String imagePath;
    Bitmap bmp;

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
        imagePath = getArguments().getString("image_taken");
        bmp = rotateBmp(BitmapFactory.decodeFile(imagePath));
        ivRetake.setImageBitmap(bmp);
       // setPic(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddPromoterArrowFragment();
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                //bundle.putBoolean("confirm",true);
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();

            }
        });
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(imagePath);
                f.delete();
            /*    Fragment fragment = new CameraFragment();
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
              //  bundle.putBoolean("confirm",false);
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();*/
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
