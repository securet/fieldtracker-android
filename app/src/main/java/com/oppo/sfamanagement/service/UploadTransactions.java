package com.oppo.sfamanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.preference.Preference;

/**
 * Created by allsmartlt218 on 20-12-2016.
 */

public class UploadTransactions extends IntentService {

    private Preference preference;
    private static TransactionProcessListner transactionProcessListner;
    private static Transactions currentTransaction=Transactions.NONE;
    private static TransactionSatus currentTransactionSatus=TransactionSatus.NONE;
    private String date = "";

    public enum Transactions{
        NONE,
        TIMEINOUT
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

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
