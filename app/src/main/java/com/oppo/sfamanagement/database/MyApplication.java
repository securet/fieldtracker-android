package com.oppo.sfamanagement.database;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

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
    }
}