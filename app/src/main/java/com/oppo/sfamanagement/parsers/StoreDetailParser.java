package com.oppo.sfamanagement.parsers;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allsmartlt218 on 08-12-2016.
 */

public class StoreDetailParser {
    String response = "";
    String result = "";
    Preferences preferences;
    ArrayList<String> list;

    public StoreDetailParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public List Parse() {

        Log.d("JSON",response);
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("storeName")) {
                JSONArray stores = new JSONArray("storeName");
                for (int i = 0 ; i < stores.length() ; i++) {
                    JSONObject obj = stores.getJSONObject(i);
                    list.add(obj.getString("storeName"));
                }
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return null;
        }
    }
}
