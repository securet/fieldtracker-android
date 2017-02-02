package com.allsmart.fieldtracker.parsers;

import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Promoter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 09-12-2016.
 */

public class PromoterListParser {
    private String response = "";
    private String result = "";
    private ArrayList<Promoter> list = new ArrayList<>();
    private Promoter promoter;
    private Preferences preferences;
    public PromoterListParser(String response, Preferences preferences) {
        this.response = response;
        this.preferences = preferences;
    }

    public ArrayList<Promoter> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);

            if(parentObject.has("requestList")) {
                if(parentObject.has("totalEntries")) {
                    int count = parentObject.getInt("totalEntries");
                    preferences.saveInt(Preferences.PROMOTER_COUNT,count);
                    preferences.commit();
                }

                JSONArray jsonArray = parentObject.getJSONArray("requestList");
                for(int i = 0 ; i < jsonArray.length() ; i++) {
                    JSONObject childObject = jsonArray.getJSONObject(i);
                    if(childObject.has("requestId")) {
                        promoter = new Promoter();
                        promoter.setRequestId(childObject.getString("requestId"));
                    }
                    if(childObject.has("statusId")) {
/*For testing*/         promoter.setStatusId(childObject.getString("statusId"));
                  //      promoter.setStatusId("ReqCompleted");
                    }
                    if(childObject.has("requestJson")){
                        String s = childObject.getString("requestJson");
                        JSONObject object = new JSONObject(s);
                        if(object.has("requestType")) {


                            promoter.setRequestType(object.getString("requestType"));
                            if(object.has("requestInfo")) {
                                JSONObject o = object.getJSONObject("requestInfo");
                                promoter.setFirstName(o.getString("firstName"));
                                promoter.setLastName(o.getString("lastName"));
                                promoter.setPhoneNum(o.getString("phone"));
                                promoter.setEmailAddress(o.getString("emailId"));
                                promoter.setAddress(o.getString("address"));
                                promoter.setUserPhoto(o.getString("userPhoto"));
                                promoter.setAadharIdPath(o.getString("aadharIdPath"));
                                promoter.setAddressIdPath(o.getString("addressIdPath"));
                                promoter.setProductStoreId(o.getString("productStoreId"));
                                list.add(promoter);
                            }
                        }
                    }
                }
            } else {
                preferences.saveBoolean(Preferences.PROMOTERISLAST,true);
                preferences.commit();
            }
        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return list;
        }
    }
}
