package com.oppo.sfamanagement.parsers;

import com.oppo.sfamanagement.model.LeaveType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveTypeParser {
    private String response;
    private LeaveType leaveType;
    private ArrayList<LeaveType> list = new ArrayList<>();

    public LeaveTypeParser(String response) {
        this.response = response;
    }

    public ArrayList<LeaveType> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if (parentObject.has("leaveTypeEnumId")) {
                JSONArray array = parentObject.getJSONArray("leaveTypeEnumId");
                for (int i = 0; i < array.length() ; i++) {
                    JSONObject childObject = array.getJSONObject(i);
                    if(childObject.has("description")) {
                        leaveType = new LeaveType();
                        leaveType.setDescription(childObject.getString("description"));
                        if(childObject.has("enumId")) {
                            leaveType.setEnumId(childObject.getString("enumId"));
                            if(childObject.has("enumTypeId")) {
                                leaveType.setEnumTypeId(childObject.getString("enumTypeId"));
                                list.add(leaveType);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }
}
