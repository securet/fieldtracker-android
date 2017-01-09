package com.oppo.sfamanagement.parsers;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Leave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveListParser {
    private String response = "";
    private String result = "";
    private Preferences preferences;
    private Leave leave;
    private ArrayList<Leave> list = new ArrayList<>();

    public LeaveListParser(String response, Preferences preferences) {
        this.preferences = preferences;
        this.response = response;
    }
    public ArrayList<Leave> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("employeeLeavesList")) {

                JSONArray parentArray = parentObject.getJSONArray("employeeLeavesList");
                if (parentArray.length() == 0 || parentArray.length() < 10) {
                    preferences.saveBoolean(Preferences.LEAVEISLAST,true);
                    preferences.commit();
                } else {
                    preferences.saveBoolean(Preferences.LEAVEISLAST,false);
                    preferences.commit();
                }
                for(int i = 0 ; i < parentArray.length() ; i++) {
                    JSONObject childObject = parentArray.getJSONObject(i);
                    if(childObject.has("fromDate")) {
                        if(childObject.has("thruDate")) {
                            leave = new Leave();
                            String sFromDate = childObject.getString("fromDate");
                            String sThruDate = childObject.getString("thruDate");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            Date fromDate = null;
                            Date thruDate = null;
                            Calendar mCalender = Calendar.getInstance();
                            Calendar mCalender2 = Calendar.getInstance();
                            try {
                                fromDate = format.parse(sFromDate.replaceAll("Z$", "+0000"));
                                thruDate = format.parse(sThruDate.replaceAll("Z$", "+0000"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(mCalender != null && mCalender2 != null) {
                                mCalender.setTime(fromDate);
                                mCalender2.setTime(thruDate);
                                leave.setFromDate((String) DateFormat.format("dd/MM/yyyy",mCalender));
                                leave.setToDate((String) DateFormat.format("dd/MM/yyyy",mCalender2));
                                leave.setDays(getDays((String) DateFormat.format("dd/MM/yyyy",mCalender),(String) DateFormat.format("dd/MM/yyyy",mCalender2)));
                            } else {

                            }
                            if(childObject.has("description")) {
                                if (childObject.has("partyRelationshipId")) {
                                    leave.setPartyRelationShipId(childObject.getString("partyRelationshipId"));
                                    leave.setReason(childObject.getString("description"));
                                    if (childObject.has("leaveApproved")) {
                                        String approve = childObject.getString("leaveApproved");
                                        if (approve.equals("null")) {
                                            leave.setStatus("Pending");
                                        } else if (approve.equalsIgnoreCase("Y")) {
                                            leave.setStatus("Approved");
                                        } else {
                                            leave.setStatus("Rejected");
                                        }
                                        list.add(leave);
                                    }
                                }
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

    public String getDays(String fromDate,String thruDate) {
        if(fromDate.length() == thruDate.length() && !TextUtils.isEmpty(fromDate) && !TextUtils.isEmpty(thruDate) && fromDate.length() == 10) {
            int thru = Integer.parseInt(thruDate.substring(0,2));
            int from = Integer.parseInt(fromDate.substring(0,2));
            if (from == thru) {
                return  "1";
            } else {
                return ((thru-from) + 1) + "";
            }

        } else {
            return "-";
        }
    }
}
