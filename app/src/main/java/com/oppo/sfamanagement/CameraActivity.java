package com.oppo.sfamanagement;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.oppo.sfamanagement.fragment.CameraPreviewClass;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private CameraPreviewClass previewClass;
    private Camera.PictureCallback pictureCallback;
    private ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.camera_layout);
        imageButton = (ImageButton) findViewById(R.id.ibPhotoCapture);
        FrameLayout fl = (FrameLayout) findViewById(R.id.flLivePreview);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,pictureCallback);
            }
        });

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }
        };

    }
}
