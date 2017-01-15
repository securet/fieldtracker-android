package com.oppo.sfamanagement.parsers;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class AddPromoterParser {
    String response = "";
    String result = "";

    public AddPromoterParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            result  = "success";
        } catch (JSONException e){
            result  = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);

        }finally {
            return result;
        }
    }
}

