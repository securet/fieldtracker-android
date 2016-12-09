package com.oppo.sfamanagement.parsers;

import com.oppo.sfamanagement.model.Promoter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 09-12-2016.
 */

public class PromoterListParser {
    String response = "";
    String result = "";
    ArrayList<Promoter> list = new ArrayList<>();
    Promoter promoter;
    public PromoterListParser(String response) {
        this.response = response;
    }

    public ArrayList<Promoter> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("requestList")) {
                JSONArray jsonArray = parentObject.getJSONArray("requestList");
                for(int i = 0 ; i < jsonArray.length() ; i++) {
                    JSONObject childObject = jsonArray.getJSONObject(i);
                    if(childObject.has("requestJson")){
                        JSONObject childArray = childObject.getJSONObject("requestJson");
                         if(childArray.has("requestInfo")) {


                                JSONArray array = childArray.getJSONArray("requestInfo");
                                for(int k = 0 ; k < array.length() ; k++) {
                                    JSONObject o = array.getJSONObject(k);
                                    promoter = new Promoter();
                                    promoter.setFirstName(o.getString("firstName"));
                                    promoter.setLastName(o.getString("lastName"));
                                    promoter.setPhoneNum(o.getString("phone"));
                                    promoter.setEmailAddress(o.getString("emailId"));
                                    promoter.setAddress(o.getString("address"));
                                    list.add(promoter);
                                }

                        }
                    }
                }
            }
        } catch (JSONException e) {

        } finally {
            return list;
        }
    }
}
