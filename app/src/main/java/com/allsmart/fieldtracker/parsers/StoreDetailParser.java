package com.allsmart.fieldtracker.parsers;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 08-12-2016.
 */

public class StoreDetailParser {
    private String response = "";
    private String result = "";
    private Preferences preferences;
    //ArrayList<String> list;

    public StoreDetailParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public String Parse() {

        Log.d("JSON",response);
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("storeName")) {
                    result = parentObject.getString("storeName");
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
