package com.allsmart.fieldtracker.parsers;

import com.allsmart.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 07-02-2017.
 */

public class LeaveApproveParser {
    private String response;
    private String result;

    public LeaveApproveParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("messages")) {
                result = "success";
            } else if(parentObject.has("errors")) {
                result = parentObject.getString("errors");
            } else {
                result = "error";
            }
        } catch (JSONException e){
            result = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }
}
