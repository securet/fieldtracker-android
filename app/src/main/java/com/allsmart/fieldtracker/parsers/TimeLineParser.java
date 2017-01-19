package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.database.Logger;
import com.allsmart.fieldtracker.database.Preferences;
import com.allsmart.fieldtracker.fragment.DynamicElement;
import com.allsmart.fieldtracker.model.TimeLine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by allsmartlt218 on 27-12-2016.
 */

public class TimeLineParser {
    private String response = "";
    private ArrayList<TimeLine> list = new ArrayList<>();
    private long count = 0;
    private Preferences preferences ;

    public TimeLineParser(String response,Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }
    public ArrayList<TimeLine> Parse(){
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("userTimeLog")) {
                JSONArray parentArray = parentObject.getJSONArray("userTimeLog");
                for (int i = 0 ; i < parentArray.length() ; i++) {
                    JSONObject childObject = parentArray.getJSONObject(i);
                    if (childObject.has("timeEntryList")) {
                        JSONArray childArray = childObject.getJSONArray("timeEntryList");
                        for (int j = 0 ; j < childArray.length() ; j++) {
                            JSONObject object = childArray.getJSONObject(j);
                            if(object.has("fromDate")) {
                                if(object.has("thruDate")){
                                    Date timeF = null;
                                    Date timeT = null;
                                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

                                    String timeStampF = object.getString("fromDate");
                                    String timeStampT = object.getString("thruDate");
                                    Calendar mCalendar = Calendar.getInstance();
                                    Calendar mCalendar2 = Calendar.getInstance();
                                    try
                                    {
                                        timeF = simpleDateFormat.parse(timeStampF.replaceAll("Z$","+0000"));
                                        if(!TextUtils.isEmpty(timeStampT)&&!timeStampT.equalsIgnoreCase("null")) {
                                            timeT = simpleDateFormat.parse(timeStampT.replaceAll("Z$", "+0000"));
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    TimeLine tl = new TimeLine();
                                    mCalendar.setTime(timeF);
                                    tl.setFromDate(DateFormat.format("hh:mm aa",mCalendar).toString());
                                    if(timeT!=null) {
                                        mCalendar2.setTime(timeT);
                                        tl.setThruDate(DateFormat.format("hh:mm aa",mCalendar2).toString());
                                        tl.setTimeSpace(DynamicElement.findMarginTop(tl.getFromDate(),tl.getThruDate()));
                                    }else{
                                        timeT = new Date();
                                        tl.setTimeSpace(0);
                                        tl.setThruDate("");
                                    }
                                    long millis = timeT.getTime()- timeF.getTime();
                                    count += millis;
                                    String timeShiftTotal = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(count),
                                            TimeUnit.MILLISECONDS.toMinutes(count) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(count)),
                                            TimeUnit.MILLISECONDS.toSeconds(count) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(count)));
                                    tl.setDifferenceMill(millis);
                                    if(j == childArray.length()-1) {
                                        preferences.saveString(Preferences.SHIFTTIME,timeShiftTotal);
                                        preferences.commit();
                                    }
                                    list.add(tl);
                                }
                            }
                        }
                    }
                }
            }
        }catch (JSONException e){
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            System.out.println(preferences.getString(Preferences.SHIFTTIME,""));
            return list;
        }
    }
}
