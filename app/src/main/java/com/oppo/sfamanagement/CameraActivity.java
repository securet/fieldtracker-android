package com.oppo.sfamanagement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oppo.sfamanagement.fragment.AddPromoterFragment;
import com.oppo.sfamanagement.fragment.CameraPreviewClass;
import com.oppo.sfamanagement.fragment.RetakeFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class CameraActivity extends AppCompatActivity  {

    private Camera camera;
    private CameraPreviewClass previewClass;
    private Camera.PictureCallback pictureCallback;
    private ImageButton imageButton;
    private static final int MEDIA_TYPE_IMAGE_FRONT = 1;
    private static final int MEDIA_TYPE_IMAGE_BACK = 2;
    private File pic;
    FrameLayout surfaceView;
    SurfaceHolder surfaceHolder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        imageButton = (ImageButton) findViewById(R.id.ibPhotoCapture);
        surfaceView = (FrameLayout) findViewById(R.id.flLivePreview);
       // surfaceHolder = surfaceView.getHolder();
      //  surfaceHolder.addCallback(this);
    //    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        final int cameraForB = getIntent().getIntExtra("camera_key",AddPromoterFragment.FRONT_CAMREA_OPEN);
        final String purpose = getIntent().getStringExtra("purpose");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,pictureCallback);
            }
        });

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
                        Log.d("path",pic.getAbsolutePath());
                     //   refreshCamera();
                    }
                    Intent i = new Intent(CameraActivity.this,RetakeActivity.class);
                    String path = pic.getAbsolutePath();
                    i.putExtra("image_taken",path);
                    i.putExtra("image_purpose",purpose);
                    startActivityForResult(i,1);
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
                        Log.d("path",pic.getAbsolutePath());
                       // refreshCamera();
                    }

                    Intent i = new Intent(CameraActivity.this,RetakeActivity.class);
                    String path = pic.getAbsolutePath();
                    i.putExtra("image_taken",path);
                    i.putExtra("image_purpose",purpose);
                    startActivityForResult(i,1);
                }
            }
        };

        if (hasCamera(getApplicationContext())) {
            if(cameraForB == AddPromoterFragment.FRONT_CAMREA_OPEN) {
                camera = getCameraInstace();
            } else {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            previewClass = new CameraPreviewClass(getApplicationContext(),camera);
            surfaceView.addView(previewClass);
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

    public boolean hasCamera(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
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

  /*  @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        Camera.Size previewSize ;
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        previewSize = list.get(0);
        Log.d("list",(String.valueOf(list.size())) + previewSize.width +" "+previewSize.height);
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(previewSize.width,previewSize.height);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;

    }*/

    /*public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {

        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            String response = data.getStringExtra("response");
            if(!response.equals(null) && !response.equals("Cancel")) {
                Intent i = new Intent();
                i.putExtra("image_response",response);
                setResult(2,i);
                finish();
            } else {
                Intent i = new Intent();
                i.putExtra("image_response","cancel");
                setResult(2,i);
                finish();
            }
        }
    }
}



