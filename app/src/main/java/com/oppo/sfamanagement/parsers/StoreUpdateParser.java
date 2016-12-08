package com.oppo.sfamanagement.parsers;

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

        } finally {
            return result;
        }
    }
}
