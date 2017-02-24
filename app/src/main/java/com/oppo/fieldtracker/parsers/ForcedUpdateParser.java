package com.oppo.fieldtracker.parsers;

import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 16-02-2017.
 */

public class ForcedUpdateParser {
    private String response;
    private String result;
    private Preferences preferences;

    public ForcedUpdateParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("forceUpdate")) {
                result = "success";
                if(parentObject.getString("forceUpdate").equals("Y")) {
                    preferences.saveBoolean(Preferences.FORCEUPDATE,true);
                    if(parentObject.has("appVersion")) {
                        preferences.saveString(Preferences.APPVERSION,parentObject.getString("appVersion"));
                    }
                    if(parentObject.has("message")) {
                        preferences.saveString(Preferences.FORCEUPDATE_MESSAGE,parentObject.getString("message"));
                    }
                } else {
                    preferences.saveBoolean(Preferences.FORCEUPDATE,false);
                }
                preferences.commit();

            } else if(parentObject.has("errors")){
                result = parentObject.getString("errors");
            } else {
                result = "error";
            }
        } catch (JSONException e) {
            Logger.e("Log", e);
            result = "error";
            Crashlytics.log(1, getClass().getName(), "Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }
}
