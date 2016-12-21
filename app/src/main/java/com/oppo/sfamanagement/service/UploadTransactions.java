package com.oppo.sfamanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.preference.Preference;

import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.database.NetworkUtils;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.database.SqliteHelper;
import com.oppo.sfamanagement.model.Response;
import com.oppo.sfamanagement.model.TimeInOutDetails;
import com.oppo.sfamanagement.parsers.TimeInOutParser;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.RestHelper;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

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
            e.printStackTrace();
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
        String result = "";
        TimeInOutDetails details = null;
        // get data base
        EventDataSource dataSource = new EventDataSource(UploadTransactions.this);
        ArrayList<TimeInOutDetails> detailsArrayList = dataSource.getTimeInOutDetails();

        // upload to server // Asc time // first success than second
        if(detailsArrayList != (null) && detailsArrayList.size() > 0) {
            for (TimeInOutDetails d : detailsArrayList) {
                response = new RestHelper().makeRestCallAndGetResponse(UrlBuilder.getUrl(Services.TIME_IN_OUT),AppsConstant.POST, ParameterBuilder.getTimeinOut(preference,d),preference);
                Response responseMsg = new TimeInOutParser(response,d.getComments()).Parse();
                if(responseMsg.getResponceCode().equals("200")) {
                    dataSource.updateIsPushed(d);
                } else {
                    break;
                }
            }

        }
        // update database

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
