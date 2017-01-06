package com.oppo.sfamanagement.parsers;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class TimeInOutParser {
    String response = "";
    Response result ;
    String reason="";


    public TimeInOutParser(String response,String reason) {
        this.response = response;
        this.reason = reason;
    }

    public Response Parse() {
        try {
            result = new Response();
            result.setResponce(reason);
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("messages")){
                result.setResponceMessage(parentObject.getString("messages"));
                result.setResponceCode("200");
            }else if(parentObject.has("errors")){
                result.setResponceMessage(parentObject.getString("errors"));
                result.setResponceCode("400");
            }else {
                result.setResponceMessage("Unbale To parse Data");
            }

    } catch (JSONException e){
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        }finally {
            return result;
        }
    }
}

