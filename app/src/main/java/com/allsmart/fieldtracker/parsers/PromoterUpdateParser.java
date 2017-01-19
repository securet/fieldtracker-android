package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.database.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class PromoterUpdateParser {
    String result = "";
    String response = "";
    public PromoterUpdateParser(String response) {
        this.response = response;
    }
    public String Parse() {
        result = "";
        try {
            JSONObject parentObject = new JSONObject(response);
            result = "success";

        } catch (JSONException e) {
            Logger.e("Log",e);
            result = "error";
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }
}
