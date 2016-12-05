package com.oppo.sfamanagement.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.oppo.sfamanagement.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class CameraFragment extends Fragment {

    private CameraPreviewClass mCameraPreview;
    private Camera camera;
    private Camera.PictureCallback pictureCallback;
    byte[] imageData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_live_preview,container,false);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.flLiveCameraPreview);
        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.ibPhotoCapture);
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos ;
                try {
                    fos = new FileOutputStream("/sdcard/oppo/your_photo.jpg");
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,pictureCallback);

                    FragmentManager fm = getFragmentManager();
                    Fragment f = new RetakeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_taken","/sdcard/oppo/your_photo.jpg");
                    f.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                    fm.executePendingTransactions();

            }
        });
        if (hasCamera(getContext())) {
            camera = getCameraInstace();
            mCameraPreview = new CameraPreviewClass(getContext(),camera);
            fl.addView(mCameraPreview);
        }
        return view;
    }

    private Camera getCameraInstace() {
        Camera c = null;
        try {
            if (Camera.getNumberOfCameras() >= 2) {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            else{
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public boolean hasCamera(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }

}
