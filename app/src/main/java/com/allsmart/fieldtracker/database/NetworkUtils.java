package com.allsmart.fieldtracker.database;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by allsmartlt218 on 21-12-2016.
 */

public class NetworkUtils {
    /*public static final int NOT_CONNECTED = 1;
    public static final int CONNECTED = 2;
    public static final int WIFI = 3;
    public static final int MOBILE = 4;*/



    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
