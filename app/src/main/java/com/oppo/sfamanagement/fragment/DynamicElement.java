package com.oppo.sfamanagement.fragment;

/**
 * Created by allsmartlt218 on 07-12-2016.
 * this class is created to generate marginTop dp at runtime depending upon time in time out difference
 */

public class DynamicElement {
    private static int totalMin ;
    private static double dp ;
    private static int timeShift = 0;
    public static int findMarginTop(String timeIn,String timeOut) {
        if(timeIn.length() == 5) {
            if (timeIn.length() == timeOut.length()) {
                int timeInHour = Integer.parseInt(timeIn.substring(0,2));
                int timeInMin = Integer.parseInt(timeIn.substring(3,5));

                int timeOutHour = Integer.parseInt(timeOut.substring(0,2));
                int timeOutMin = Integer.parseInt(timeOut.substring(3,5));

                if (timeInHour > timeOutHour) {
                    int timeOutMins = timeOutHour * 60 + timeOutMin;
                    int timeInMins = ((12 - timeInHour) - 1) * 60 + timeInMin;
                    totalMin = timeOutMins - timeInMins;
                } else if(timeInHour == timeOutHour) {
                    if (timeInMin == timeOutMin) {
                        totalMin = 0;
                    } else if(timeInMin > timeOutMin) {
                        totalMin = timeInMin + 12 * 60;
                    } else {
                        totalMin =timeOutMin - timeInMin;
                    }
                } else {
                    if(timeOutMin > timeInMin) {
                        int ansMin = (timeOutMin + 60) - timeInMin;
                        int ansHour = (timeOutHour - 1) - timeInHour;
                        totalMin = ansHour * 60 + ansMin;
                    }
                    else if (timeOutMin < timeInMin) {
                        int ansMin = timeOutMin - timeInMin;
                        int ansHour = timeOutHour - timeInHour;
                        totalMin = ansHour * 60 + ansMin;
                    }
                    else {
                        int ansHour = timeOutHour - timeInHour;
                        totalMin = ansHour * 60;
                    }
                }
            }
        }
        if (totalMin != 0 ) {
            dp =  (double) totalMin * 20 / 60;
        }
        return (int)Math.round(dp);
    }
}
