package com.oppo.sfamanagement.parsers;

import android.text.TextUtils;

import com.oppo.sfamanagement.model.LeaveApply;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveApplyParser {
    private String response = "";
    private String result = "";
    private LeaveApply leaveApply;

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
            e.printStackTrace();
            result = "failed";
        } finally {
            return result;
        }
    }
}
