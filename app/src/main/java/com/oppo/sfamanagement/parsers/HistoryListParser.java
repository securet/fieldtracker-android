package com.oppo.sfamanagement.parsers;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.DynamicElement;
import com.oppo.sfamanagement.model.HistoryChild;
import com.oppo.sfamanagement.model.HistoryNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryListParser {
    private String result = "";
    private String response = "";
    private Preferences preferences;
    private long fromDateMill = 0;
    private long thruDateMill = 0;
    //private HistoryNew history;
    private ArrayList<HistoryNew> parentArraylist = new ArrayList<>();

    //private HistoryChild historyChild;
//    private ArrayList<HistoryChild> childArrayList = new ArrayList<>();
    public HistoryListParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<HistoryNew> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            // size 0 or <10 make  IsLast = true
            if (parentObject.has("userTimeLog")) {
                if (parentObject.has("totalEntries")) {
                    int count = parentObject.getInt("totalEntries");
                    preferences.saveInt(Preferences.HISTORY_COUNT, count);
                    preferences.commit();
                }
                JSONArray arrayParent = parentObject.getJSONArray("userTimeLog");
                //Log.d("a",String.valueOf(arrayParent.length()));
                /*if (arrayParent.length() == 0 || arrayParent.length() < 10) {
                    preferences.saveBoolean(Preferences.ISLAST, true);
                    preferences.commit();
                }else {
                    preferences.saveBoolean(Preferences.ISLAST, false);
                    preferences.commit();
                }*/
                for (int i = 0; i < arrayParent.length(); i++) {
                    // System.out.println(arrayParent.length() + "     main array        " + i);
                    JSONObject childOject = arrayParent.getJSONObject(i);
                    if (childOject.has("estimatedStartDate")) {
                        HistoryNew history = new HistoryNew();
                        Date timeIn = null;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

                        String timeStamp = childOject.getString("estimatedStartDate");
                        Calendar mCalendar = Calendar.getInstance();
                        try {
                            timeIn = simpleDateFormat.parse(timeStamp.replaceAll("Z$", "+0000"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mCalendar.setTime(timeIn);
                        history.setDate(DateFormat.format("dd-MMM-yy", mCalendar).toString());
                        ArrayList<HistoryChild> childArrayList = new ArrayList<>();
                        if (childOject.has("timeEntryList")) {
                            JSONArray childArray = childOject.getJSONArray("timeEntryList");
                            for (int j = 0; j < childArray.length(); j++) {

                                JSONObject childObject2 = childArray.getJSONObject(j);

                                if (childObject2.has("fromDate")) {
                                    if (childObject2.has("thruDate")) {

                                        HistoryChild historyChild = new HistoryChild();
                                        String fromDate = childObject2.getString("fromDate");
                                        String thruDate = childObject2.getString("thruDate");
                                        Date fDate = null;
                                        Date tDate = null;
                                        Calendar c1 = Calendar.getInstance();
                                        Calendar c2 = Calendar.getInstance();
                                        try {
                                            fDate = simpleDateFormat.parse(fromDate.replaceAll("Z$", "+0000"));
                                            if (!TextUtils.isEmpty(thruDate) && !thruDate.equalsIgnoreCase("null")) {
                                                tDate = simpleDateFormat.parse(thruDate.replaceAll("Z$", "+0000"));
                                            }
                                        } catch (Exception e) {
                                            Logger.e("Log", e);
                                            Crashlytics.log(1, getClass().getName(), "Error in Parsing the response");
                                            Crashlytics.logException(e);
                                        }
                                        c1.setTime(fDate);
                                        historyChild.setFromDate(DateFormat.format("hh:mm aa", c1).toString());
                                        if (j == 0) {
                                            fromDateMill = fDate.getTime();
                                        }
                                        if (tDate != null) {
                                            c2.setTime(tDate);
                                            historyChild.setThruDate(DateFormat.format("hh:mm aa", c2).toString());
                                            historyChild.setTimeSpace(DynamicElement.findMarginTop(DateFormat.format("hh:mm", c1).toString(), DateFormat.format("hh:mm", c2).toString()));

                                            if (j == childArray.length() - 1) {
                                                thruDateMill = tDate.getTime();
                                                long millis = thruDateMill - fromDateMill;
                                                history.setHours(String.format("%2dh %2dm", TimeUnit.MILLISECONDS.toHours(millis),
                                                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
                                                System.out.println(String.format("%2dh %2dm", TimeUnit.MILLISECONDS.toHours(millis),
                                                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
                                            }

                                            historyChild.setTimeSpace(DynamicElement.findMarginTop(DateFormat.format("hh:mm", c1).toString(), DateFormat.format("hh:mm", c2).toString()));
                                        } else {
                                            historyChild.setThruDate("");
                                            historyChild.setTimeSpace(0);
                                        }
                                            /*if (j == childArray.length() - 1 && thruDateMill > 0 && fromDateMill > 0) {
                                                Calendar hours = Calendar.getInstance();

                                                hours.setTime(new Date(thruDateMill - fromDateMill));
                                                history.setHours(DateFormat.format("h'h' m'm'", hours).toString());
                                                System.out.println(DateFormat.format("h'h' m'm'", hours).toString());
                                            }*/
                                         /*else {
                                            history.setHours("- : -");
                                        }*/


                                        childArrayList.add(historyChild);

                                                /*if ()

                                                long millis = tDate.getTime() - fDate.getTime();
                                                history.setHours(String.format("%02dh %02dm", TimeUnit.MILLISECONDS.toHours(millis),
                                                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));*/
                                        history.setHistoryChildren(childArrayList);
                                        // }
                                    }

                                }

                            }
                            /*if (thruDateMill > 0 && fromDateMill > 0) {
                                Calendar hours = Calendar.getInstance();

                                hours.setTime(new Date(thruDateMill - fromDateMill));
                                history.setHours(DateFormat.format("h'h' m'm'", hours).toString());
                                System.out.println(DateFormat.format("h'h' m'm'", hours).toString());
                            } else {
                                history.setHours("- : -");
                            }*/

                            parentArraylist.add(history);
                        }

                    }


                }
            } else {
                preferences.saveBoolean(Preferences.ISLAST, true);
                preferences.commit();
            }
        } catch (JSONException e) {
            Logger.e("Log", e);
            Crashlytics.log(1, getClass().getName(), "Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return parentArraylist;
        }

    }

    /*private String getTimeStamp(String timeStampMain) {
        String timeStamp = timeStampMain.replace('T',' ');
        return timeStamp;
    }*/
}
