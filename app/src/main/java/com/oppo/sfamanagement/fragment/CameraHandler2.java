package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
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

import com.oppo.sfamanagement.AddPromoterCameraHandler;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraHandler2 extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    Bitmap bmp;
    Button confirm,cancel;
    ImageView imageView;
    String imagePurpose = "" ;
    String imagePath = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_handler_2,container,false);
        confirm = (Button) view.findViewById(R.id.btConfirm);
        cancel = (Button) view.findViewById(R.id.btRetake);
        imageView = (ImageView) view.findViewById(R.id.ivRetake);
        imagePath = getArguments().getString("image_taken");
        imagePurpose = getArguments().getString("image_purpose");
        bmp = rotateBmp(BitmapFactory.decodeFile(imagePath));
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                bundle.putString(AppsConstant.FILE, imagePath);
                bundle.putString(AppsConstant.FILEPURPOSE,imagePurpose);
                getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,CameraHandler2.this).forceLoad();
       /*         Intent i = new Intent();
                i.putExtra("response",imagePath);
                getActivity().setResult(2,i);
                getActivity().finish();*/
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

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((AddPromoterCameraHandler)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(), LoaderMethod.IMAGE_UPLOAD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((AddPromoterCameraHandler)getActivity()).showHideProgressForLoder(true);
        String imagePath = (String) data;
        Intent i = new Intent();
        i.putExtra("image_photo",imagePath);
        if (imagePurpose.equals("For Photo")) {
            getActivity().setResult(AppsConstant.IMAGE_PHOTO,i);
        } else if (imagePurpose.equals("For Aadhar")) {
            getActivity().setResult(AppsConstant.IMAGE_AADHAR,i);
        } else if(imagePurpose.equals("For Address Proof")) {
            getActivity().setResult(AppsConstant.IMAGE_ADDRESS_PROOF,i);
        }
        getLoaderManager().destroyLoader(loader.getId());
        getActivity().finish();
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
