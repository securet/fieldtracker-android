package com.oppo.sfamanagement.parsers;

import android.text.format.DateFormat;

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
    private HistoryNew history;
    private ArrayList<HistoryNew> parentArraylist = new ArrayList<>();
    private ArrayList<HistoryChild> childArrayList = new ArrayList<>();
    private HistoryChild historyChild;

    public HistoryListParser(String response) {
        this.response = response;
    }

    public ArrayList Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("userTimeLog")){
                JSONArray arrayParent = parentObject.getJSONArray("userTimeLog");
                for (int i = 0 ; i < arrayParent.length() ; i++) {
                    System.out.println(arrayParent.length() + "     main array        "   + i);
                    JSONObject childOject = arrayParent.getJSONObject(i);
                    if (childOject.has("estimatedStartDate")) {
                        history = new HistoryNew();
                        Date timeIn = null;
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

                        String timeStamp = childOject.getString("estimatedStartDate");
                        Calendar mCalendar = Calendar.getInstance();
                        try
                        {
                            timeIn = simpleDateFormat.parse(timeStamp.replaceAll("Z$","+0000"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mCalendar.setTime(timeIn);
                        history.setDate(DateFormat.format("dd-MMM-yy", mCalendar).toString());
                        history.setTimeIn(DateFormat.format("h:mm",mCalendar).toString());

                       // history.setStartDate(childOject.getString("estimatedStartDate"));
                        if (childOject.has("estimatedCompletionDate")) {
                            String lastUpdatedTimestamp = childOject.getString("estimatedCompletionDate");
                            Date timeOut = null;
                            try
                            {
                                timeOut = simpleDateFormat.parse(lastUpdatedTimestamp.replaceAll("$",""));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            mCalendar.setTime(timeOut);
                            history.setTimeOut(DateFormat.format("hh:mm", mCalendar).toString());
                            long millis = timeOut.getTime()- timeIn.getTime();
                            history.setHours(String.format("%02dh %02dm", TimeUnit.MILLISECONDS.toHours(millis),
                                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
                           // history.setEndDate(childOject.getString("estimatedCompletionDate"));
                            if (childOject.has("timeEntryList")) {
                                JSONArray childArray = childOject.getJSONArray("timeEntryList");
                                for (int j = 0 ; j < childArray.length() ; j++) {
                                    System.out.println(childArray.length()  + "  sub array" + j);
                                    JSONObject childObject2 = childArray.getJSONObject(j);
                                    if (childObject2.has("fromDate")) {
                                        historyChild = new HistoryChild();
                                        String fromDate = childObject2.getString("fromDate");
                                        Date fDate = null;
                                        Calendar c2 = Calendar.getInstance();
                                        try {
                                            fDate = simpleDateFormat.parse(fromDate.replaceAll("Z$","+0000"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        c2.setTime(fDate);
                                        historyChild.setFromDate(DateFormat.format("hh:mm",c2).toString());
                                        //historyChild.setFromDate(childObject2.getString("fromDate"));
                                        if(childObject2.has("comments")) {
                                            historyChild.setComments(childObject2.getString("comments"));
                                            childArrayList.add(historyChild);
                                        }
                                       // childArrayList.add(historyChild);
                                    }

                                }
                                history.setHistoryChildren(childArrayList);
                                parentArraylist.add(history);
                            }
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return parentArraylist;
        }

    }

    /*private String getTimeStamp(String timeStampMain) {
        String timeStamp = timeStampMain.replace('T',' ');
        return timeStamp;
    }*/
}
