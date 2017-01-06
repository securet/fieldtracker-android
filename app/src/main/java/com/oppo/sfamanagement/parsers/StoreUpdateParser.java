package com.oppo.sfamanagement.parsers;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 08-12-2016.
 */

public class StoreUpdateParser {
    private String response;
    private String result = "";

    public StoreUpdateParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);

        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }
}
