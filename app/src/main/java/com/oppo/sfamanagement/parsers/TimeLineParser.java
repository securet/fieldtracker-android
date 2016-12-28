package com.oppo.sfamanagement.parsers;

import android.text.format.DateFormat;

import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.DynamicElement;
import com.oppo.sfamanagement.model.TimeLine;

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
                                        timeT = simpleDateFormat.parse(timeStampT.replaceAll("Z$","+0000"));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    mCalendar.setTime(timeF);
                                    mCalendar2.setTime(timeT);
                                    long millis = timeT.getTime()- timeF.getTime();
                                    count += millis;
                                    String timeShiftTotal = String.format("%02d:%02d:%02d",
                                            TimeUnit.MILLISECONDS.toHours(count),
                                            TimeUnit.MILLISECONDS.toMinutes(count) -
                                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(count)),
                                            TimeUnit.MILLISECONDS.toSeconds(count) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(count)));
                                    TimeLine tl = new TimeLine();
                                    tl.setFromDate(DateFormat.format("hh:mm",mCalendar).toString());
                                    tl.setThruDate(DateFormat.format("hh:mm",mCalendar2).toString());
                                    tl.setDifferenceMill(millis);
                                    tl.setTimeSpace(DynamicElement.findMarginTop(tl.getFromDate(),tl.getThruDate()));
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
            e.printStackTrace();
        } finally {
            return list;
        }
    }
}
