package com.allsmart.fieldtracker.parsers;

import android.util.Log;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.model.Manager;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 17-02-2017.
 */

public class ReporteeListParser {
    private String response;
    private String result;
    private Preferences preferences;
    private ArrayList<Manager> list = new ArrayList<>();
    private Manager manager;

    public ReporteeListParser(String response,Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<Manager> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("totalReportees")) {
                preferences.saveInt(Preferences.REPORTEE_LIST_COUNT,parentObject.getInt("totalReportees"));
                preferences.commit();
            }

            if(parentObject.has("reporteeList")) {
                JSONArray array = parentObject.getJSONArray("reporteeList");
                for (int i = 0 ; i < array.length() ; i++) {
                    JSONObject childObject = array.getJSONObject(i);
                    if(childObject.has("userFullName")) {
                        manager = new Manager();
                        manager.setAgentName(childObject.getString("userFullName"));
                        manager.setUsername(childObject.getString("username"));
                        Log.d(MainActivity.TAG,/*manager.getUsername()*/ childObject.getString("username") +  "   " + manager.getAgentName());
                        if(childObject.has("locations")) {
                            JSONArray array2 = childObject.getJSONArray("locations");
                            manager.setStoreName(array2.getString(0));
                            list.add(manager);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            result = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return list;
        }
    }
}
