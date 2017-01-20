package com.allsmart.fieldtracker.utils;

import android.text.format.DateFormat;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 08-Dec-15.
 */
public class CalenderUtils {
    public static final String DateMonthDashedFormate = "dd-MM-yyyy";
    public static final String YearMonthDashedFormate = "yyyy-MM-dd";
    public static final String DateMonthSlashFormate = "dd/MM/yyyy";
    public static final String MonthDateFormate = "MM/dd/yyyy";
    public static final String DateFormate = "yyyy-MM-dd HH:mm:ss";
    public static final String TimeSecFormate = "HH:mm:ss";
    public static final String TimeMinFormate = "HH:mm";
    public static final String DateFormateWithZone = "yyyy-MM-dd'T'HH:mm:ssZ";


    public static long getDateDifference(String cureentDate, String finalDate, String dateFormat) {
        long mills, days = 0;
        String strTime = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date Date1 = format.parse(finalDate);
            Date Date2 = format.parse(cureentDate);
            mills = Date1.getTime() - Date2.getTime();
            days = mills / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"CalenderUtils","CalenderUtils");
            Crashlytics.logException(e);

        }
        return days;

    }

    public static String getDayMonthWithSuffix(String strDate, String strFormate) {

        SimpleDateFormat sdf = new SimpleDateFormat(strFormate);
        Date d = new Date();
        try {
            d = sdf.parse(strDate);
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.logException(e);
        }
        sdf.applyPattern("dd");
        int intDay = StringUtils.getInt(sdf.format(d));
        String suffix = "";
        if (intDay >= 11 && intDay <= 13) {
            suffix = "th";
        } else {
            int intCase = intDay % 10;
            switch (intCase) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
                    break;
            }
        }

        sdf.applyPattern("MMM");
        String StrMonth = sdf.format(d);

        return intDay + suffix + " " + StrMonth;

    }

    public static long getDateDifference(String currentDate, String finalDate) {
        return getDateDifference(currentDate, finalDate, "dd-MM-yyyy hh:mm:ss");
    }

    // 03-12-2016
    public static String getTimeDiffinMinute(String currentDate, String finalDate) {
        long mills, Hours = 0, Mins = 0, sec = 0;
        String strTime = "";

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date Date1 = format.parse(finalDate);
            Date Date2 = format.parse(currentDate);
            mills = Date1.getTime() - Date2.getTime();
            Hours = mills / (1000 * 60 * 60);
            Mins = (mills - (Hours * 1000 * 60 * 60)) / (1000 * 60);
            sec = (mills - (Hours * 1000 * 60 * 60) - (Mins * 1000 * 60)) / (1000);
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.logException(e);
        }

        if (Hours > 0) {
            strTime = (Hours >= 9 ? "" + Hours : "0" + Hours) + ":" + (Mins >= 9 ? "" + Mins : "0" + Mins) + ":" + (sec >= 9 ? "" + sec : "0" + sec);
        } else if (Mins > 0) {
            strTime = (Mins >= 9 ? "" + Mins : "0" + Mins) + ":" + (sec >= 9 ? "" + sec : "0" + sec);
        }
        return strTime;
    }

    public static String getCurrentDate(String formate) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(formate);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getLeaveFromDate(String date) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        String gmt = sdf.format(today);
        return date+"T00:00:00" + gmt;
    }

    public static String getLeaveThruDate(String date) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        String gmt = sdf.format(today);

        return date+"T23:59:59" + gmt;
    }

    public static String getTimeZoneDate(String formate,String time) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("Z");
            String gmt = sdf.format(today);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalenderUtils.DateFormate);
            Date date = null;
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar mCalendar = Calendar.getInstance();
            if (date != null) {
                mCalendar.setTime(date);
            }
            return DateFormat.format(formate.replaceAll("Z$", gmt), mCalendar).toString();
    }

    public static String getDateMethod(String timeStamp, String formate){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            date = simpleDateFormat.parse(timeStamp);
            calendar.setTime(date);
        } catch (ParseException e) {
            Logger.e("Log",e);
            Crashlytics.logException(e);
        }
        return (String) DateFormat.format(formate,calendar);
    }
}
