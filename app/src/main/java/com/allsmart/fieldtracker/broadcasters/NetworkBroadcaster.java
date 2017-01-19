package com.allsmart.fieldtracker.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.allsmart.fieldtracker.database.NetworkUtils;
import com.allsmart.fieldtracker.service.UploadTransactions;

/**
 * Created by allsmartlt218 on 22-12-2016.
 */

public class NetworkBroadcaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(NetworkUtils.isNetworkConnectionAvailable(context)) {
            Intent uploadTraIntent=new Intent(context,UploadTransactions.class);
            context.startService(uploadTraIntent);
        }
    }
}
