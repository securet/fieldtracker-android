package com.oppo.sfamanagement.parsers;

import android.util.Log;

import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
                result = "Success";
                JSONArray users = new JSONArray(parentobject.getString("user"));
                for(int i=0;i<users.length();i++) {
                    JSONObject obj = users.getJSONObject(i);
                    preferences.saveString(Preferences.USERNAME, obj.getString("username"));
                    preferences.saveString(Preferences.USERFULLNAME, obj.getString("userFullName"));
                    preferences.saveString(Preferences.USERFIRSTNAME, obj.getString("firstName"));
                    preferences.saveString(Preferences.USERLASTNAME, obj.getString("lastName"));
                    preferences.saveString(Preferences.USERID, obj.getString("userId"));
                    preferences.saveString(Preferences.USEREMAIL, obj.getString("emailAddress"));
                    preferences.saveString(Preferences.ROLETYPEID, obj.getString("roleTypeId"));
 /*For testing*/  //preferences.saveString(Preferences.ROLETYPEID, "SalesExecutive");
                    preferences.saveString(Preferences.PARTYID, obj.getString("partyId"));
                    preferences.commit();;
                }
            }else if(parentobject.has("errors")){
                result =parentobject.getString("errors");
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
        } finally {
            return result;
        }
    }

}
