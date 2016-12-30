package com.oppo.sfamanagement;

import java.util.concurrent.TimeUnit;

/**
 * Created by allsmartlt218 on 30-12-2016.
 */

public class TimeDifferenceCalculator {
    private static int totalMin ;
    private static long dp ;
    private static int timeShift = 0;
    public static String findMarginTop(String timeIn,String timeOut) {
        if(timeIn.length() == 5) {
            if (timeIn.length() == timeOut.length()) {
                int timeInHour = Integer.parseInt(timeIn.substring(0, 2));
                int timeInMin = Integer.parseInt(timeIn.substring(3, 5));

                int timeOutHour = Integer.parseInt(timeOut.substring(0, 2));
                int timeOutMin = Integer.parseInt(timeOut.substring(3, 5));

                if (timeInHour > timeOutHour) {

                    int hour = (12 - timeInHour - 1) + timeOutHour;
                    int min = (60 - timeInMin) + timeOutMin;
                    if (min > 60) {
                        min = min - 60;
                        hour = hour + 1;
                        return hour + "h " + min + "m";
                    } else {
                        return hour + "h " + min + "m";
                    }

                } else if (timeInHour == timeOutHour) {
                    if (timeInMin == timeOutMin) {
                        return "0h" + " 0m";
                    } else if (timeInMin > timeOutMin) {
                        int hour = (12 - timeInHour - 1) + timeOutHour;
                        int min = (60 - timeInMin) + timeOutMin;
                        if (min > 60) {
                            min = min - 60;
                            hour = hour + 1;
                            return hour + "h " + min + "m";
                        } else {
                            return hour + "h " + min + "m";
                        }
                    } else {
                        return "0h " + (timeOutMin - timeInMin) + "m";
                    }
                } else {
                    if (timeOutMin > timeInMin) {
                        return timeOutHour - timeInHour + "h " + (timeOutMin - timeInMin) + "m";
                    } else if (timeOutMin < timeInMin) {
                        int hour = (12 - timeInHour - 1) + timeOutHour;
                        int min = (60 - timeInMin) + timeOutMin;
                        if (min > 60) {
                            min = min - 60;
                            hour = hour + 1;
                            return hour + "h " + min + "m";
                        } else {
                            return hour + "h " + min + "m";
                        }
                    } else {
                        return timeOutHour - timeInHour + "h " + "0m";
                    }
                }
            }
        }
        return "- : -";
    }
}
