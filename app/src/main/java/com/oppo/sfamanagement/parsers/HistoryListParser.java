package com.oppo.sfamanagement.parsers;

import android.text.format.DateFormat;

import com.oppo.sfamanagement.model.History;
import com.oppo.sfamanagement.model.HistoryChild;
import com.oppo.sfamanagement.model.HistoryNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private String dateCreater(String timeStamp){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date timeIn = new Date();
        try
        {
            timeIn = simpleDateFormat.parse(timeStamp);
        }catch (Exception e){
            e.printStackTrace();
        }
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(timeIn.getTime());
        return DateFormat.format("dd-MMM-yy", mCalendar).toString();
    }
    public ArrayList Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("userTimeLog")){
                JSONArray arrayParent = parentObject.getJSONArray("userTimeLog");
                for (int i = 0 ; i < arrayParent.length() ; i++) {
                    JSONObject childOject = arrayParent.getJSONObject(i);
                    if (childOject.has("estimatedStartDate")) {
                        history = new HistoryNew();
                        String timeStamp = childOject.getString("estimatedStartDate");
                        String date = dateCreater(timeStamp);
                        history.setDate(date);
                        //history.setStartDate(childOject.getString("estimatedStartDate"));
                        if (childOject.has("estimatedCompletionDate")) {
                            history.setEndDate(childOject.getString("estimatedCompletionDate"));
                        }
                    }
                    if (childOject.has("timeEntryList")) {
                        JSONArray childArray = childOject.getJSONArray("timeEntryList");
                        for (int j = 0 ; j < childArray.length() ; j++) {
                            JSONObject childObject2 = childArray.getJSONObject(j);
                            if (childObject2.has("fromDate")) {
                                historyChild = new HistoryChild();
                                historyChild.setFromDate(childObject2.getString("fromDate"));
                                if(childObject2.has("comments")) {
                                    historyChild.setComments(childObject2.getString("comments"));
                                    childArrayList.add(historyChild);
                                }
                            }
                        }
                        history.setHistoryChildren(childArrayList);
                        parentArraylist.add(history);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return parentArraylist;
        }

    }
}
