package com.oppo.sfamanagement.fragment;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class CameraFragment extends Fragment {

    private CameraPreviewClass mCameraPreview;
    private Camera camera;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_live_preview,container,false);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.flLiveCameraPreview);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.ibPhotoCapture);
        camera = getCameraInstace();
        mCameraPreview = new CameraPreviewClass(getContext(),camera);
        return view;
    }

    private Camera getCameraInstace() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


}
