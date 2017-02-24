package com.oppo.fieldtracker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.utils.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import id.zelory.compressor.Compressor;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class RetakeFragment extends Fragment {

    Bitmap bmp;
    Button confirm,cancel;
    ImageView imageView;
    String imagePurpose = "" ;
    String imagePath = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retake_fragment,container,false);
        confirm = (Button) view.findViewById(R.id.btConfirm);
        cancel = (Button) view.findViewById(R.id.btRetake);
        imageView = (ImageView) view.findViewById(R.id.ivRetake);
        imagePath = getArguments().getString("image_taken");
        imagePurpose = getArguments().getString("image_purpose");
        final File path = new File(imagePath);
        if(!imagePurpose.equals(null) && imagePurpose.equals("ForPhoto")) {
            bmp = rotateBmpFront(path);
        } else {
            bmp = rotateBmpBack(path);
        }
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sImagePath = "";
                /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sImagePath = getImageUri(getContext(),bmp).getPath();
                }else {
                    sImagePath = persistImage(bmp, imagePurpose);
                }*/
                sImagePath = persistImage(bmp,imagePurpose);
                Intent i = new Intent();
                i.putExtra("response", sImagePath);
                i.putExtra("image_purpose", imagePurpose);
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flCapture);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(currentFragment);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.popBackStackImmediate();
            }
        });
        return view;
    }

    private Bitmap rotateBmpBack(File path) {
        Matrix m = new Matrix();
        m.postRotate(90);
        Bitmap bitmap = new Compressor.Builder(getContext()).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG).setMaxWidth(480)
                .setMaxHeight(640).build().compressToBitmap(path);
        bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);

        return bmp;
    }
    private String persistImage(Bitmap bitmap, String name) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM),name + ".jpg");
     //   File imageFile = new File(mediaStorageDir, name + ".jpg");

        OutputStream os = null;
        try {
            os = new FileOutputStream(mediaStorageDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Writing image file to local device");
            Crashlytics.logException(e);
        } finally {
            return mediaStorageDir.getAbsolutePath();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,null, null);
        return Uri.parse(path);
    }

    public Bitmap rotateBmpFront(File path){
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap bitmap = new Compressor.Builder(getContext()).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG).setMaxWidth(480)
                .setMaxHeight(640).build().compressToBitmap(path);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }

}
