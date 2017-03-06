package com.allsmart.fieldtracker.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;


import com.allsmart.fieldtracker.service.GeolocationService;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by AllSmart-LT008 on 10/21/2016.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Intent intent = new Intent(getApplicationContext(), GeolocationService.class);
        startService(intent);
    }
}