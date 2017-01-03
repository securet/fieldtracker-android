package com.oppo.sfamanagement.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.oppo.sfamanagement.CameraActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.zelory.compressor.Compressor;

import static android.R.attr.path;

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
        /*try {
            ExifInterface ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bmp = rotateImage(bitmap, -90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    bmp = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bmp = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //  bmp = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sImagePath = persistImage(bmp,imagePurpose);
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
        //bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2, m, true);
        Bitmap bitmap = new Compressor.Builder(getContext()).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG).setMaxWidth(480)
                .setMaxHeight(640).build().compressToBitmap(path);

       // bitmap = Bitmap.createScaledBitmap(bitmap,640,480,true);
        bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);

        return bmp;
    }
    private String persistImage(Bitmap bitmap, String name) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Oppo");
        File imageFile = new File(mediaStorageDir, name + ".jpg");

        OutputStream os = null;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return imageFile.getAbsolutePath();
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
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(270);
        //supply the original width and height, if you don't want to change the height and width of bitmap.
        /*Bitmap bitmap = Compressor.getDefault(getContext()).compressToBitmap(path);
        bitmap = Bitmap.createScaledBitmap(bitmap,640,480,true);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);*/
        Bitmap bitmap = new Compressor.Builder(getContext()).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG).setMaxWidth(480)
                .setMaxHeight(640).build().compressToBitmap(path);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
       // Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,480,640,true);
        return bmp;
    }

    /*@Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((CameraActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(), LoaderMethod.IMAGE_UPLOAD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((CameraActivity)getActivity()).showHideProgressForLoder(true);
        String imagePath = (String) data;
        Log.d("SERVERPATH",imagePath);
        Intent i = new Intent();
        i.putExtra("image_photo",imagePath);
        if (imagePurpose.equals("For Photo")) {
            getActivity().setResult(Activity.RESULT_OK,i);
        } else if (imagePurpose.equals("For Aadhar")) {
            getActivity().setResult(Activity.RESULT_OK,i);
        } else if(imagePurpose.equals("For Address Proof")) {
            getActivity().setResult(Activity.RESULT_OK,i);
        }
        getLoaderManager().destroyLoader(loader.getId());
        getActivity().finish();
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }*/
}
