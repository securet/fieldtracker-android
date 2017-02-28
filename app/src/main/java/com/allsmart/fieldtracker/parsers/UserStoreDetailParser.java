package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;

import com.allsmart.fieldtracker.model.Store;
import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AllSmart-LT008 on 12/7/2016.
 */

public class UserStoreDetailParser {

    String response ="";// fnp.jks
    String result = "";
    Preferences preferences;
    public UserStoreDetailParser(String response, Preferences preferences)
    {
        this.response =  response;
        this.preferences =  preferences;
    }
    public String Parse()
    {
        try {
            JSONObject parentobject = new JSONObject(response);
            if(parentobject.has("address")){
                result = "success";
                    preferences.saveString(Preferences.SITE_ADDRESS, parentobject.getString("address"));
                if(parentobject.has("latitude") && !TextUtils.isEmpty(parentobject.getString("latitude")) &&
                        !parentobject.getString("latitude").equals("null")) {
                    preferences.saveString(Preferences.LATITUDE, parentobject.getDouble("latitude")+"");
                }

                if(parentobject.has("longitude") && !TextUtils.isEmpty(parentobject.getString("longitude")) &&
                        !parentobject.getString("longitude").equals("null")) {
                    preferences.saveString(Preferences.LONGITUDE, parentobject.getDouble("longitude")+"");
                }
    //                preferences.saveString(Preferences.LATITUDE, parentobject.getDouble("latitude")+"");
               //     preferences.saveString(Preferences.SITE_ENTITY, parentobject.getString("_entity"));
                    preferences.saveString(Preferences.SITENAME, parentobject.getString("storeName"));
                Store.storeMap.put(parentobject.getString("productStoreId"),parentobject.getString("storeName"));
                    preferences.saveString(Preferences.PARTYID, parentobject.getString("productStoreId"));
         //           preferences.saveString(Preferences.LONGITUDE, parentobject.getDouble("longitude")+"");
                if(parentobject.has("proximityRadius") && !TextUtils.isEmpty(parentobject.getString("proximityRadius")) &&
                        !parentobject.getString("proximityRadius").equals("null")) {
                    preferences.saveString(Preferences.SITE_RADIUS,parentobject.getInt("proximityRadius")+"");
                }
          //          preferences.saveString(Preferences.SITE_RADIUS,parentobject.getInt("proximityRadius")+"");
                    preferences.commit();
            } else if(parentobject.has("errors")) {
                result = parentobject.getString("errors");
            } else {
                result = "error";
            }
        } catch (JSONException e) {
            /*preferences.saveString(Preferences.SITE_ADDRESS,"");
            preferences.saveString(Preferences.LATITUDE,"0.0");
            preferences.saveString(Preferences.SITE_ENTITY,"");
            preferences.saveString(Preferences.SITENAME,"Off Site" );
            preferences.saveString(Preferences.PARTYID,"");
            preferences.saveString(Preferences.LONGITUDE,"0.0");
            preferences.saveString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS);
            preferences.commit();*/
            result = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }

}
