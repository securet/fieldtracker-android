package com.oppo.sfamanagement.model;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-01-2017.
 */

public class LeaveType {
    private String description;
    private String enumId;
    private String enumTypeId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnumTypeId() {
        return enumTypeId;
    }

    public void setEnumTypeId(String enumTypeId) {
        this.enumTypeId = enumTypeId;
    }

    public String getEnumId() {
        return enumId;
    }

    public void setEnumId(String enumId) {
        this.enumId = enumId;
    }
}
