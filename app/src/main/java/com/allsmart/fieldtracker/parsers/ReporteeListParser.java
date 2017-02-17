package com.allsmart.fieldtracker.parsers;

import com.allsmart.fieldtracker.model.Manager;
import com.allsmart.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 17-02-2017.
 */

public class ReporteeListParser {
    private String response;
    private String result;
    private ArrayList<Manager> list = new ArrayList<>();
    private Manager manager;

    public ReporteeListParser(String response) {
        this.response = response;
    }

    public ArrayList<Manager> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);

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
