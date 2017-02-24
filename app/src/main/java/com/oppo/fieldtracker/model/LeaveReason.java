package com.oppo.fieldtracker.model;

import java.util.HashMap;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveReason {
    private String reasonDescription;
    private String enumTypeReason;
    public static HashMap<String,String> reason = new HashMap<>();

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public String getEnumTypeReason() {
        return enumTypeReason;
    }

    public void setEnumTypeReason(String enumTypeReason) {
        this.enumTypeReason = enumTypeReason;
    }
}
