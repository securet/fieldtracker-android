package com.oppo.sfamanagement.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
        if(imagePurpose.equals("For Photo")) {
            bmp = rotateBmpFront(BitmapFactory.decodeFile(imagePath));
        } else {
            bmp = rotateBmpBack(BitmapFactory.decodeFile(imagePath));
        }
      //  bmp = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Bundle bundle = new Bundle();
                bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                bundle.putString(AppsConstant.FILE, imagePath);
                bundle.putString(AppsConstant.FILEPURPOSE,imagePurpose);
                getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,RetakeFragment.this).forceLoad();*/
                        Intent i = new Intent();
                        i.putExtra("response",imagePath);
                        i.putExtra("image_purpose",imagePurpose);
                        getActivity().setResult(Activity.RESULT_OK,i);
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



    private Bitmap rotateBmpBack(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.postRotate(90);
        //bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2, m, true);
         bitmap = Bitmap.createScaledBitmap(bitmap,640,480,true);
        bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
        return bmp;
    }

    public Bitmap rotateBmpFront(Bitmap bitmap){
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(270);
        //supply the original width and height, if you don't want to change the height and width of bitmap.
        bitmap = Bitmap.createScaledBitmap(bitmap,640,480,true);
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
