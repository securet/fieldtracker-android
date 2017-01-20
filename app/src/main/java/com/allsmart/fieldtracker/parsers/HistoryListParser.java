package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.utils.DynamicElement;
import com.allsmart.fieldtracker.model.HistoryChild;
import com.allsmart.fieldtracker.model.HistoryNew;

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
    private ArrayList<HistoryNew> parentArraylist = new ArrayList<>();
    public HistoryListParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<HistoryNew> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if (parentObject.has("userTimeLog")) {
                if (parentObject.has("totalEntries")) {
                    int count = parentObject.getInt("totalEntries");
                    preferences.saveInt(Preferences.HISTORY_COUNT, count);
                    preferences.commit();
                }
                JSONArray arrayParent = parentObject.getJSONArray("userTimeLog");
                for (int i = 0; i < arrayParent.length(); i++) {
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
                                        childArrayList.add(historyChild);
                                        history.setHistoryChildren(childArrayList);
                                    }

                                }

                            }
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
}
