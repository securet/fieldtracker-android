package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 09-12-2016.
 */

public class AddStoreParser {
    String response = "";
    String result = "";
    public AddStoreParser(String response) {
        this.response = response;
    }
    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("productStore")) {
                result = "success";
            } else {
                result = "error";
            }

        } catch (JSONException e) {
            result = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }
}
