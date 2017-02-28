package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 08-12-2016.
 */

public class StoreListParser {
    private Preferences preferences;
    private ArrayList<Store> list = new ArrayList<>();
    private Store store;
    private String response;
    public StoreListParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<Store> Parse() {

        try {
            JSONObject parentObject = new JSONObject(response);


            if(parentObject.has("userStores")) {

                if(parentObject.has("totalEntries")) {
                    preferences.saveInt(Preferences.STORE_LIST_COUNT,parentObject.getInt("totalEntries"));
                    preferences.commit();
                }
                JSONArray array = parentObject.getJSONArray("userStores");

                for (int i = 0 ; i < array.length() ; i++) {
                    JSONObject obj =  array.getJSONObject(i);
                    store = new Store();
                    store.setStoreName(obj.getString("storeName"));
                    store.setStoreId(obj.getInt("productStoreId"));

                    store.setAddress(obj.getString("address"));
                    store.setLattitude(obj.getString("latitude"));
                    store.setLongitude(obj.getString("longitude"));
                    if(obj.has("proximityRadius") && !TextUtils.isEmpty(obj.getString("proximityRadius")) && !"null".equals(obj.getString("proximityRadius"))) {
                        store.setSiteRadius(obj.getInt("proximityRadius")+"");
                    }

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
