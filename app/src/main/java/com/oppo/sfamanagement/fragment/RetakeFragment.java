package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.BaseBundle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class RetakeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    ImageView ivRetake;
    Button retake,confirm;
    String imagePath;
    Bitmap bmp;
    String imagePurpose ;
    Preferences preferences;

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
        imagePurpose = getArguments().getString("image_purpose");
        ivRetake.setImageBitmap(bmp);
      //  i++;
      //  Log.d("Count",String.valueOf(i));
       // setPic(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                bundle.putString(AppsConstant.FILE, imagePath);
                bundle.putString(AppsConstant.FILEPURPOSE,imagePurpose);
                getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,RetakeFragment.this).forceLoad();


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

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(), LoaderMethod.IMAGE_UPLOAD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);

        String imagePath = (String) data;

        Fragment fragment = new AddPromoterFragment();
        FragmentManager fm = getFragmentManager();
        Bundle b = new Bundle();
        b.putString("image_server_path",imagePath);
        fragment.setArguments(b);
        getLoaderManager().destroyLoader(loader.getId());
        fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
        fm.executePendingTransactions();

    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().remove(this).commit();
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}

