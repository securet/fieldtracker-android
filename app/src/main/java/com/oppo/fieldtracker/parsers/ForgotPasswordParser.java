package com.oppo.fieldtracker.parsers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 07-02-2017.
 */

public class ForgotPasswordParser {
    private String response;
    private String result;

    public ForgotPasswordParser(String response) {
        this.response = response;
    }

    public String Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("messages")) {
                result = "success";
            } else if(parentObject.has("errors")) {
                result = parentObject.getString("errors");
            } else {
                result = "error";
            }

        } catch (JSONException e) {
            result = "error";
        } finally {
            return result;
        }
    }
}
