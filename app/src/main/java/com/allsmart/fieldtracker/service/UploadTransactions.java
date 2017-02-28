package com.allsmart.fieldtracker.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.storage.EventDataSource;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Response;
import com.allsmart.fieldtracker.model.TimeInOutDetails;
import com.allsmart.fieldtracker.parsers.ImageUploadParser;
import com.allsmart.fieldtracker.parsers.TimeInOutParser;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.webmethods.RestHelper;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 20-12-2016.
 */

public class UploadTransactions extends IntentService {

    private Preferences preference;
    private static TransactionProcessListner transactionProcessListner;
    private static Transactions currentTransaction=Transactions.NONE;
    private static TransactionSatus currentTransactionSatus=TransactionSatus.NONE;
    private String response = "";

    public enum Transactions{
        NONE,
        TIMEINOUT,
    }

    public enum TransactionSatus{
        START,
        ERROR_NO_INTERNETCONNECTION,
        ERROR_TIMEOUT,
        ERROR_EXCEPTION,
        SUCCESS,
        FAILURE,
        NODATA,
        END,
        NONE
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadTransactions() {
        super("UploadTransactions");
    }
    public interface TransactionProcessListner {
        public void transactionStatus(Transactions transactions,TransactionSatus transactionSatus);
        public void error(TransactionSatus transactionSatus);
        public void currentTransaction(Transactions transactions);
    }

    public static void setTransactionProcessListner(TransactionProcessListner transaction){
        transactionProcessListner=transaction;
        updateTransactionStatus();
    }
    public static void resetTransactionProcessListner(){
        transactionProcessListner=null;
        currentTransaction=Transactions.NONE;
        currentTransactionSatus=TransactionSatus.NONE;
    }
    private static void updateTransactionStatus(){
        try {
            if(transactionProcessListner!=null){
                transactionProcessListner.currentTransaction(currentTransaction);
                transactionProcessListner.transactionStatus(currentTransaction, currentTransactionSatus);
            }
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"UploadTransaction","UploadTransaction");
            Crashlytics.logException(e);
        }
    }

    private void uploadTransaction(Transactions transaction){
        try {

            //LogUtils.errorLog("UploadTransactions", "uploadTransaction "+transaction);
            switch (transaction) {
                case TIMEINOUT:
                    uploadTimeInOut();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadTimeInOut() {

        // get data base
        EventDataSource dataSource = new EventDataSource(getApplicationContext());
        ArrayList<TimeInOutDetails> detailsArrayList = dataSource.getTimeInOutDetails();

        // upload to server // Asc time // first success than second
        if(detailsArrayList != (null) && detailsArrayList.size() > 0) {
            for (TimeInOutDetails d : detailsArrayList) {
                // imgae server server path
                String imagePath = uploadImage(d.getActionImage());
                if((!TextUtils.isEmpty(d.getActionImage()) && !TextUtils.isEmpty(imagePath)) || TextUtils.isEmpty(imagePath)) {
                    response = new RestHelper().makeRestCallAndGetResponse(UrlBuilder.getUrl(Services.TIME_IN_OUT), AppsConstant.POST, ParameterBuilder.getTimeinOut(preference, d, imagePath), preference);
                    Response responseMsg = new TimeInOutParser(response, d.getComments()).Parse();
                    if (responseMsg.getResponceCode().equals("200")) {
                        dataSource.updateIsPushed(d);
                    //    Toast.makeText(getApplicationContext(),"200",Toast.LENGTH_SHORT).show();
                        Log.d(MainActivity.TAG,responseMsg.getResponceMessage());
                        Crashlytics.log(1,getClass().getName(),
                                "Error in Uploading Database Entry" + responseMsg.getResponceMessage());
                        break;
                    } else {

                        Crashlytics.log(1,getClass().getName(),
                                "Error in Uploading Database Entry" + responseMsg.getResponceMessage());
                        Log.d(MainActivity.TAG,responseMsg.getResponceMessage());
                    }
                }
                /*else {
                    Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_SHORT).show();
                    break;
                }*/
            }

        } else {
            //Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_SHORT).show();
            Log.d(MainActivity.TAG,"Failed to upload");
        }
        // update database

    }
    public String uploadImage(String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            String imageResponse = new RestHelper().makeRestCallAndGetResponseImageUpload(Services.DomainUrlImage, imgPath, "ForPhoto", preference);
            String serverPath = new ImageUploadParser(imageResponse).Parse();
            return serverPath;
        }
        else {
            return "";
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        preference = new Preferences(this);

        if(NetworkUtils.isNetworkConnectionAvailable(this)){
            currentTransaction=Transactions.NONE;
            currentTransactionSatus=TransactionSatus.START;
            updateTransactionStatus();
            uploadTransaction(Transactions.TIMEINOUT);
            //uploadTransaction(Transactions.BUTTON_UPDATE);
            currentTransaction=Transactions.NONE;
            currentTransactionSatus=TransactionSatus.END;
            updateTransactionStatus();
        }else{

            if(transactionProcessListner!=null)
                transactionProcessListner.error(TransactionSatus.ERROR_NO_INTERNETCONNECTION);
        }
    }
}
