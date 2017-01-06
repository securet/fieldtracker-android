package com.oppo.sfamanagement.parsers;

import android.text.TextUtils;

import com.oppo.sfamanagement.model.Leave;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveListParser {
    private String response = "";
    private String result = "";

    public LeaveListParser(String response) {
        this.response = response;
    }
    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);

        } catch (JSONException e) {
            e.printStackTrace();
            result = "failed";
        } finally {
            return result;
        }
    }
}
