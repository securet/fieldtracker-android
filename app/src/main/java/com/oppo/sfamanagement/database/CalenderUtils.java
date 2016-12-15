package com.oppo.sfamanagement.database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 08-Dec-15.
 */
public class CalenderUtils {
    public static final String DateMonthDashedFormate = "dd-MM-yyyy";
    public static final String DateMonthSlashFormate = "dd/MM/yyyy";
    public static final String MonthDateFormate = "MM/dd/yyyy";
    public static final String DateFormate = "yyyy-MM-dd HH:mm:ss";
    public static final String TimeSecFormate = "HH:mm:ss";
    public static final String TimeMinFormate = "HH:mm";


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
            Logger.e("Log", e);

        }
        return days;

    }

    public static String getDayMonthWithSuffix(String strDate, String strFormate) {

        SimpleDateFormat sdf = new SimpleDateFormat(strFormate);
        Date d = new Date();
        try {
            d = sdf.parse(strDate);
        } catch (Exception e) {
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
            Logger.e("Log", e);
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
}
