package com.oppo.sfamanagement.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AllSmart-LT008 on 10/22/2016.
 */

public class ShiftTimeView extends TextView {

    Calendar mCalendar;
    private final static String m12 = "HH:mm:ss";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;
    private SharedPreferences myPrefs;
    private boolean mTickerStopped = false;

    String mFormat;

    public ShiftTimeView(Context context) {
        super(context);
        initClock(context);
    }

    public ShiftTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        myPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;

                if(!TextUtils.isEmpty(myPrefs.getString("LoginDate",""))){
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
                    Date loginDate = new Date();
                    try {
                         loginDate = simpleDateFormat.parse(myPrefs.getString("LoginDate", ""));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Date curentDate = new Date();
//                    mCalendar.setTimeInMillis(curentDate.getTime()-loginDate.getTime());
//                    setText(DateFormat.format(mFormat, mCalendar).toString().toUpperCase());
                    long millis = curentDate.getTime()-loginDate.getTime();
                    setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));

                }

                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
       return false;// return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

}
