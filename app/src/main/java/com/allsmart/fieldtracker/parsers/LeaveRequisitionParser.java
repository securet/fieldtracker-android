package com.allsmart.fieldtracker.parsers;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.model.LeaveRequisition;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.utils.CalenderUtils;
import com.allsmart.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by allsmartlt218 on 25-01-2017.
 */

public class LeaveRequisitionParser {
    private String response = "";
    private String result = "";
    private Preferences preferences;
    private LeaveRequisition leave;
    private ArrayList<LeaveRequisition> list = new ArrayList<>();

    public LeaveRequisitionParser(String response, Preferences preferences) {
        this.preferences = preferences;
        this.response = response;
    }
    public ArrayList<LeaveRequisition> Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("employeeLeavesList")) {

                if(parentObject.has("totalEntries")) {
                    int count = parentObject.getInt("totalEntries");
                    preferences.saveInt(Preferences.LEAVE_REQUISITION_COUNT,count);
                    preferences.commit();
                }

                JSONArray parentArray = parentObject.getJSONArray("employeeLeavesList");
                for(int i = 0 ; i < parentArray.length() ; i++) {
                    JSONObject childObject = parentArray.getJSONObject(i);
                    if(childObject.has("fromDate")) {
                        if(childObject.has("thruDate")) {
                            leave = new LeaveRequisition();
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
                              //  leave.setDays(getDays((String) DateFormat.format("dd/MM/yyyy",mCalender),(String) DateFormat.format("dd/MM/yyyy",mCalender2)));
                                leave.setDays(CalenderUtils.getDifferenceDate((String) DateFormat.format(CalenderUtils.YearMonthDashedFormate,mCalender),
                                        (String) DateFormat.format(CalenderUtils.YearMonthDashedFormate,mCalender2)));
                                Log.d(MainActivity.TAG,leave.getDays()+" this is leave days");
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
                                    } else {
                                        leave.setStatus("Pending");
                                    }
                                        if(childObject.has("leaveTypeEnumId")) {
                                            String enumType = childObject.getString("leaveTypeEnumId");
                                            if(enumType.equals("EltLossOfPay")){
                                                leave.setEnumType("Loss of Pay");
                                            } else if (enumType.equals("EltHoliday")) {
                                                leave.setEnumType("Holiday");
                                            } else if(enumType.equals("EltEarned")) {
                                                leave.setEnumType("Earned Leave");
                                            } else if(enumType.equals("EltSpecialDayOff")) {
                                                leave.setEnumType("Special Day Off");
                                            } else {
                                                leave.setEnumType("Casual Leave");
                                            }
                                        }
                                        if(childObject.has("leaveReasonEnumId")) {
                                            String reasonType = childObject.getString("leaveReasonEnumId");
                                            if(reasonType.equals("ElrMedical")) {
                                                leave.setReasonType("Medical");
                                            } else {
                                                leave.setReasonType("Personal");
                                            }
                                        }
                                        if(childObject.has("firstName")) {
                                            if(childObject.has("lastName")) {
                                                String agentName = childObject.getString("firstName") + " " + childObject.getString("lastName");
                                                if(!TextUtils.isEmpty(agentName)) {
                                                    leave.setAgentName(agentName);
                                                } else {
                                                    Log.d(AppsConstant.TAG,"No Name at Leave Parser");
                                                }
                                            }
                                        }
                                        list.add(leave);

                                }
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
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
