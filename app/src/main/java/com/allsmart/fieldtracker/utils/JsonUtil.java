package com.allsmart.fieldtracker.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 01-02-2017.
 */

public class JsonUtil {

    public static String toJson(String leaveTypeEnumId,
                                String leaveReasonEnumId,
                                String description,
                                String fromDate,
                                String thruDate,
                                String partyRelationshipId) {
        String url = "";
        try {
            JSONObject object = new JSONObject();
            object.put("leaveTypeEnumId",leaveTypeEnumId);
            object.put("leaveReasonEnumId",leaveReasonEnumId);
            object.put("description",description);
            object.put("fromDate",fromDate);
            object.put("thruDate",thruDate);
            object.put("partyRelationshipId",partyRelationshipId);

            url = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return url;
        }
    }
}
