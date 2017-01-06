package com.oppo.sfamanagement.parsers;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 08-12-2016.
 */

public class StoreListParser {
    Preferences preferences;
    ArrayList<Store> list = new ArrayList<>();
    Store store;
    String response;
    public StoreListParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<Store> Parse() {

        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("userStores")) {
                JSONArray array = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    array = parentObject.getJSONArray("userStores");
                }
                for (int i = 0 ; i < array.length() ; i++) {
                    JSONObject obj =  array.getJSONObject(i);
                    store = new Store(obj.getString("storeName"), obj.getInt("productStoreId"));
                    store.setAddress(obj.getString("address"));
                    store.setLattitude(obj.getString("latitude"));
                    store.setLongitude(obj.getString("longitude"));
                    list.add(store);
                }
            }
        } catch (JSONException e){
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return list;
        }

    }
}
