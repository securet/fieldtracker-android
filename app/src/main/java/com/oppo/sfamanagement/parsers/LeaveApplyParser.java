package com.oppo.sfamanagement.parsers;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveApplyParser {
    private String response = "";
    private String result = "";

    public LeaveApplyParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(!TextUtils.isEmpty(response)){
                result = "success";
                /*if (parentObject.has("employeeLeave")) {
                    JSONObject employeeLeave = parentObject.getJSONObject("employeeLeave");
                    if(employeeLeave.has("partyRelationshipId")) {
                        String party = employeeLeave.getString("partyRelationshipId");
                        result = party;
                    }
                }*/
            } else {
                result = "failed";
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
            result = "failed";
        } finally {
            return result;
        }
    }
}
