package com.allsmart.fieldtracker.model;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveReasonApply {
    private ArrayList<LeaveType> typeList;
    private ArrayList<LeaveReason> reasonList;

    public ArrayList<LeaveType> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<LeaveType> typeList) {
        this.typeList = typeList;
    }

    public ArrayList<LeaveReason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(ArrayList<LeaveReason> reasonList) {
        this.reasonList = reasonList;
    }
}
