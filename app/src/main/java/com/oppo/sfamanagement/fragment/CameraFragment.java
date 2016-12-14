package com.oppo.sfamanagement.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.oppo.sfamanagement.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraFragment extends Fragment{

    private Camera camera;
    private CameraPreviewClass previewClass;
    private Camera.PictureCallback pictureCallback;
    private ImageButton imageButton;
    private static final int MEDIA_TYPE_IMAGE_FRONT = 1;
    private static final int MEDIA_TYPE_IMAGE_BACK = 2;
    private File pic;
    FrameLayout surfaceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_handler_1,container,false);
        surfaceView = (FrameLayout) view.findViewById(R.id.flLivePreview);
        imageButton = (ImageButton) view.findViewById(R.id.ibPhotoCapture);
        final int cameraForB = getActivity().getIntent().getIntExtra("camera_key",AddPromoterFragment.FRONT_CAMREA_OPEN);
        final String purpose = getActivity().getIntent().getStringExtra("purpose");
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if (cameraForB == AddPromoterFragment.FRONT_CAMREA_OPEN) {
                    pic = getOutputMediaFile(MEDIA_TYPE_IMAGE_FRONT);
                    if (pic != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(pic);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FragmentManager fm = getFragmentManager();
                    Fragment f = new RetakeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_taken",pic.getAbsolutePath());
                    bundle.putString("image_purpose",purpose);
                    f.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.flCapture,f).commit();
                    fm.executePendingTransactions();
                } else {
                    pic = getOutputMediaFile(MEDIA_TYPE_IMAGE_BACK);
                    if (pic != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(pic);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FragmentManager fm = getFragmentManager();
                    Fragment f = new RetakeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_taken",pic.getAbsolutePath());
                    bundle.putString("image_purpose",purpose);
                    f.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.flCapture,f).addToBackStack(null).commit();
                    fm.executePendingTransactions();
                }
            }
        };
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,pictureCallback);
            }
        });
        if (hasCamera(getContext())) {
            if(cameraForB == AddPromoterFragment.FRONT_CAMREA_OPEN) {
                camera = getCameraInstace();
            } else {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }

            previewClass = new CameraPreviewClass(getContext(),camera);
            surfaceView.addView(previewClass);
        }
        return view;
    }
    public boolean hasCamera(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
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

    private static Uri getOutputMediaFileUri (int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Oppo");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CAMERA", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE_FRONT){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_FRONT" + ".jpg");
        } else if(type == MEDIA_TYPE_IMAGE_BACK) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_BACK" + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
}
