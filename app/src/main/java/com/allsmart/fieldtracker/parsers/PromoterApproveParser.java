package com.allsmart.fieldtracker.parsers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 31-01-2017.
 */

public class PromoterApproveParser {
    private String response;
    private String result;


    public PromoterApproveParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("request")) {
                result = "success";
            } else {
                result = parentObject.getString("errors");
            }
        } catch (JSONException e) {
            result = "error";
        } finally {
            return result;
        }
    }
}
