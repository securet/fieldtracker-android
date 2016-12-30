package com.oppo.sfamanagement;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.CameraFragment;
import com.oppo.sfamanagement.fragment.CameraFragment2;


/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraActivity extends AppCompatActivity{

    private Preferences preferences;
    private TextView tvUser,tvUserSername,tvSiteName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        preferences = new Preferences(CameraActivity.this);
        tvUser = (TextView) findViewById(R.id.tvUserName);
        tvUserSername = (TextView) findViewById(R.id.tvUserSerName);
        tvSiteName = (TextView) findViewById(R.id.tvSiteName);
        tvUser.setText(preferences.getString(Preferences.USERFIRSTNAME,"username"));
        tvUserSername.setText(preferences.getString(Preferences.USERLASTNAME,"lastname"));
        tvSiteName.setText(preferences.getString(Preferences.SITENAME,"sitename"));
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
                    dialog = new Dialog(CameraActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.custom_loader);
                    dialog.setCancelable(false);
                    dialog.show();
                    ImageView ivOutsideImage;
                    ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
                    Animation rotateXaxis = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.rotate_x_axis);
                    rotateXaxis.setInterpolator(new LinearInterpolator());
                    ivOutsideImage.setAnimation(rotateXaxis);
                }
            }
            catch(Exception e)
            {
                dialog = null;
                Logger.e("Log",e);
            }
        }
    }
    private Dialog dialog;
}
