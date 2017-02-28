package com.allsmart.fieldtracker.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class CameraPreviewClass extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private Camera mCamera;
  //  private List<Camera.Size> previewSize = parameters.getSupportedPreviewSizes();

    public CameraPreviewClass(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
        } catch (RuntimeException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
            Crashlytics.logException(e);
        }

        Camera.Size previewSize ;
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        previewSize = list.get(0);
        Log.d("list",(String.valueOf(list.size())) + previewSize.width +" "+previewSize.height);
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(previewSize.width,previewSize.height);
        if(mCamera != null) {
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch(Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
            Crashlytics.logException(e);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mHolder.getSurface() == null) {
            return;
        }
        if(mCamera != null) {
            mCamera.stopPreview();
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e ) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
            Crashlytics.logException(e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }
}
