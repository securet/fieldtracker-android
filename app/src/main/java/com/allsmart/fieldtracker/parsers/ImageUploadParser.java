package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class ImageUploadParser {
    String result = "";
    String response = "";
    public ImageUploadParser(String response) {
        this.response = response;
    }

    public String Parse() {
        String filePath = "";
        try {
            JSONObject parentObject = new JSONObject(response);

            if(parentObject.has("savedFilename")) {
                filePath = parentObject.getString("savedFilename");
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return filePath;
        }
    }
}
