package com.oppo.fieldtracker.model;

import java.util.HashMap;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveType {

    private String typeDescription;
    private String enumType;
    public static HashMap<String,String> type = new HashMap<>();


    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getEnumType() {
        return enumType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

}
