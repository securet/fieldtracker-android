package com.oppo.fieldtracker.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.utils.Logger;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.fragment.CameraFragment;
import com.oppo.fieldtracker.fragment.CameraFragment2;
import com.wang.avi.AVLoadingIndicatorView;

import io.fabric.sdk.android.Fabric;


/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraActivity extends AppCompatActivity{

    private Preferences preferences;
    private TextView tvUser,tvUserSername,tvSiteName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.camera_activity);
        preferences = new Preferences(getApplicationContext());
        tvUser = (TextView) findViewById(R.id.tvUserName);
        tvUserSername = (TextView) findViewById(R.id.tvUserSerName);
        tvSiteName = (TextView) findViewById(R.id.tvSiteName);
        tvUser.setText(preferences.getString(Preferences.USERFIRSTNAME,""));
        tvUserSername.setText(preferences.getString(Preferences.USERLASTNAME,""));
        tvSiteName.setText(preferences.getString(Preferences.USER_CUREENTSITE,""));
        //FrameLayout fl = (FrameLayout) findViewById(R.id.flCapture);
        if (Build.VERSION.SDK_INT >= 22) {
            Fragment fragment = new CameraFragment2();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.flCapture,fragment).commit();
        } else {
            Fragment fragment = new CameraFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.flCapture, fragment).commit();
            fm.executePendingTransactions();
        }
    }
    String SHOW_HIDE_LOADER = "SHOW_HIDE_LOADER";
    public void showHideProgressForLoder(boolean isForHide)
    {
        synchronized (SHOW_HIDE_LOADER)
        {
            if(isForHide)
            {
                if(AppsConstant.RunningLoaderCount>0)
                    AppsConstant.RunningLoaderCount = AppsConstant.RunningLoaderCount-1;
                if(AppsConstant.RunningLoaderCount==0)
                    hideLoader();
            }
            else
            {
                AppsConstant.RunningLoaderCount++;
                showLoader("");
            }

        }
    }
    public void hideLoader()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
    public void showLoader(String strMessage){
        runOnUiThread(new CameraActivity.RunShowLoaderCustom());
    }
    class RunShowLoaderCustom implements Runnable
    {
        public RunShowLoaderCustom()
        {
        }
        @Override
        public void run()
        {
            try
            {
                if(dialog == null|| (dialog != null && !dialog.isShowing()))
                {
                    /*dialog = new Dialog(CameraActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.custom_loader);
                    dialog.setCancelable(false);
                    dialog.show();
                    ImageView ivOutsideImage;
                    ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
                    Animation rotateXaxis = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.rotate_x_axis);
                    rotateXaxis.setInterpolator(new LinearInterpolator());
                    ivOutsideImage.setAnimation(rotateXaxis);*/
                    dialog = new Dialog(CameraActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.loader_animation);
                    dialog.setCancelable(false);
                    dialog.show();
                    AVLoadingIndicatorView avl = (AVLoadingIndicatorView) dialog.findViewById(R.id.avlView);
                    avl.show();
                }
            }
            catch(Exception e)
            {
                dialog = null;
                Logger.e("Log",e);
                Crashlytics.log(1,getClass().getName(),"Error in CameraActivity");
                Crashlytics.logException(e);
            }
        }
    }
    private Dialog dialog;
}
