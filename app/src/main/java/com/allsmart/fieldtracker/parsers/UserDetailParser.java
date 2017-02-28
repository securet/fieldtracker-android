package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AllSmart-LT008 on 12/7/2016.
 */

public class UserDetailParser {

    String response ="";// fnp.jks
    String result = "";
    Preferences preferences;
    public UserDetailParser(String response,Preferences preferences)
    {
        this.response =  response;
        this.preferences =  preferences;
    }
    public String Parse()
    {
        try {
            JSONObject parentobject = new JSONObject(response);
            if(parentobject.has("user")){
                result = "success";
                JSONArray users = new JSONArray(parentobject.getString("user"));
                for(int i=0;i<users.length();i++) {
                    JSONObject obj = users.getJSONObject(i);
                    preferences.saveString(Preferences.USERNAME, obj.getString("username"));
                    preferences.saveString(Preferences.USERFULLNAME, obj.getString("userFullName"));
                    preferences.saveString(Preferences.USERFIRSTNAME, obj.getString("firstName"));
                    preferences.saveString(Preferences.USERLASTNAME, obj.getString("lastName"));
                    preferences.saveString(Preferences.USERID, obj.getString("userId"));
                    preferences.saveString(Preferences.USERPHONE,obj.getString("contactNumber"));
                    preferences.saveString(Preferences.USER_EMAIL,obj.getString("emailAddress"));
                    preferences.saveString(Preferences.USER_ADDRESS,obj.getString("address1"));

                    if(obj.has("onPremise")) {
                        if(obj.getString("onPremise").equalsIgnoreCase("Y")){
                            preferences.saveBoolean(Preferences.ISONPREMISE,true);
                        } else {
                            preferences.saveBoolean(Preferences.ISONPREMISE,false);
                        }
                    }
                    if(obj.has("userPhotoPath")) {
                        preferences.saveString(Preferences.USER_PHOTO, obj.getString("userPhotoPath"));
                    } else {
                        preferences.saveString(Preferences.USER_PHOTO,"");
                    }
                    if(obj.has("reportingPerson")) {
                        JSONObject object = obj.getJSONObject("reportingPerson");
                        preferences.saveString(Preferences.REPORTEE_MANAGER_NAME,object.getString("userFullName"));
                        preferences.saveString(Preferences.REPORTEE_MANAGER_EMAIL,object.getString("emailAddress"));
                        preferences.saveString(Preferences.REPORTEE_MANAGER_PHONE,object.getString("contactNumber"));
                    }
                    preferences.saveString(Preferences.USEREMAIL, obj.getString("emailAddress"));
                    preferences.saveString(Preferences.ROLETYPEID, obj.getString("roleTypeId"));
 /*For testing*/ // preferences.saveString(Preferences.ROLETYPEID, "FieldExecutiveOffPremise");
                    if(obj.has("productStoreId")) {
                        preferences.saveString(Preferences.PARTYID, obj.getString("productStoreId"));
                    }
                    preferences.commit();;
                }
            }else if(parentobject.has("errors")){
                result =parentobject.getString("errors");
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
            result = "error";
        } finally {
            return result;
        }
    }

}
