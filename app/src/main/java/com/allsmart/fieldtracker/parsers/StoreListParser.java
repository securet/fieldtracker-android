package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;
import android.util.Log;

import com.allsmart.fieldtracker.activity.MainActivity;
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
                    if(obj.has("productStoreId") ) {
                        Log.d(MainActivity.TAG,obj.getString("productStoreId"));
                        store.setStoreId(obj.getString("productStoreId"));
                    }

                    if(obj.has("locationImagePath")) {
                        store.setStoreImage(obj.getString("locationImagePath"));
                    }

                    store.setAddress(obj.getString("address"));
                    if (obj.has("latitude") && obj.has("longitude") && obj.has("locationImagePath")) {
                        Log.d(MainActivity.TAG, obj.getString("latitude") + "    " + obj.getString("longitude"));
                        if (!TextUtils.isEmpty(obj.getString("latitude")) && !obj.getString("latitude").equals("null")
                                && !TextUtils.isEmpty(obj.getString("longitude")) && !obj.getString("longitude").equals("null")) {
                            if (!obj.getString("locationImagePath").equals("null")
                                    && !TextUtils.isEmpty(obj.getString("locationImagePath"))) {
                                store.setIsUpdated("Y");
                            }
                        } else {
                            store.setIsUpdated("N");
                        }
                    } else {
                        store.setIsUpdated("N");
                    }
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
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        }
        finally {
            return list;
        }

    }
}
