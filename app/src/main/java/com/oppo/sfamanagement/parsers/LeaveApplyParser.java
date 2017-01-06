package com.oppo.sfamanagement.parsers;

import com.oppo.sfamanagement.model.LeaveApply;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveApplyParser {
    private String response;
    private LeaveApply leaveApply;

    public LeaveApplyParser(String response) {
        this.response = response;
    }

    public LeaveApply Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return leaveApply;
        }
    }
}
