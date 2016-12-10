package com.oppo.sfamanagement.parsers;

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
        } catch (JSONException e){

        }finally {
            return result;
        }
    }
}

