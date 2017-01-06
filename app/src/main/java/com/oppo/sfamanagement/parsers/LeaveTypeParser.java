package com.oppo.sfamanagement.parsers;

import com.oppo.sfamanagement.model.LeaveReason;
import com.oppo.sfamanagement.model.LeaveReasonApply;
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
    private LeaveReason leaveReason;
    private ArrayList<LeaveType> list = new ArrayList<>();
    private ArrayList<LeaveReason> list2 = new ArrayList<>();
    private ArrayList<LeaveReasonApply> list3 = new ArrayList<>();
    private LeaveReasonApply leaveReasonApply = new LeaveReasonApply();

    public LeaveTypeParser(String response) {
        this.response = response;
    }

    public ArrayList<LeaveReasonApply> Parse() {

        try {
            JSONObject parentObject = new JSONObject(response);
            if (parentObject.has("leaveTypeEnumId")) {
                JSONArray array = parentObject.getJSONArray("leaveTypeEnumId");
                for (int i = 0; i < array.length() ; i++) {
                    JSONObject childObject = array.getJSONObject(i);
                    if(childObject.has("description")) {
                        leaveType = new LeaveType();
                        leaveType.setTypeDescription(childObject.getString("description"));
                        if(childObject.has("enumId")) {
                            leaveType.setEnumType(childObject.getString("enumId"));
                            list.add(leaveType);
                            leaveReasonApply.setTypeList(list);
                        }
                    }
                }
            }if (parentObject.has("leaveReasonEnumId")) {
                JSONArray array = parentObject.getJSONArray("leaveReasonEnumId");
                for (int i = 0; i < array.length() ; i++) {
                    JSONObject childObject = array.getJSONObject(i);
                    if(childObject.has("description")) {
                        leaveReason = new LeaveReason();
                        leaveReason.setReasonDescription(childObject.getString("description"));
                        if(childObject.has("enumId")) {
                            leaveReason.setEnumTypeReason(childObject.getString("enumId"));
                            list2.add(leaveReason);
                            leaveReasonApply.setReasonList(list2);
                            list3.add(leaveReasonApply);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return list3;
        }
    }
}
