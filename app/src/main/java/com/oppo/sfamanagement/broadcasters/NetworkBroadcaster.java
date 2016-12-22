package com.oppo.sfamanagement.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oppo.sfamanagement.database.NetworkUtils;
import com.oppo.sfamanagement.service.UploadTransactions;

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
