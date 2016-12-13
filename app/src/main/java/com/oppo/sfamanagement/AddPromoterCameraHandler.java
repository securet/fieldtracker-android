package com.oppo.sfamanagement;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.oppo.sfamanagement.fragment.CameraHandler1;
import com.oppo.sfamanagement.fragment.CameraHandler2;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class AddPromoterCameraHandler extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_handler);
        //FrameLayout fl = (FrameLayout) findViewById(R.id.flCapture);
        Fragment fragment = new CameraHandler1();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flCapture,fragment).commit();
        fm.executePendingTransactions();
    }
}
