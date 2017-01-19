package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.database.AppsConstant;
import com.allsmart.fieldtracker.database.Logger;
import com.allsmart.fieldtracker.database.Preferences;

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
                result = "Success";
                    preferences.saveString(Preferences.SITE_ADDRESS, parentobject.getString("address"));
                    preferences.saveString(Preferences.LATITUDE, parentobject.getDouble("latitude")+"");
                    preferences.saveString(Preferences.SITE_ENTITY, parentobject.getString("_entity"));
                    preferences.saveString(Preferences.SITENAME, parentobject.getString("storeName"));
                    preferences.saveString(Preferences.PARTYID, parentobject.getString("productStoreId"));
                    preferences.saveString(Preferences.LONGITUDE, parentobject.getDouble("longitude")+"");
                    preferences.saveString(Preferences.SITE_RADIUS,parentobject.getInt("proximityRadius")+"");
                    preferences.commit();
            }
        } catch (JSONException e) {
            preferences.saveString(Preferences.SITE_ADDRESS,"");
            preferences.saveString(Preferences.LATITUDE,"0.0");
            preferences.saveString(Preferences.SITE_ENTITY,"");
            preferences.saveString(Preferences.SITENAME,"Off Site" );
            preferences.saveString(Preferences.PARTYID,"");
            preferences.saveString(Preferences.LONGITUDE,"0.0");
            preferences.saveString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS);
            preferences.commit();
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return result;
        }
    }

}
