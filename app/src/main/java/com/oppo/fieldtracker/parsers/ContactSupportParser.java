package com.oppo.fieldtracker.parsers;

import com.oppo.fieldtracker.model.Contact;
import com.oppo.fieldtracker.utils.Logger;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allsmartlt218 on 17-02-2017.
 */

public class ContactSupportParser {
    private String response;
    private String result;
    private Contact contact;

    public ContactSupportParser(String response) {
        this.response = response;
    }

    public Contact Parse() {
        try {
            JSONObject parentObject = new JSONObject(response);
            if(parentObject.has("userObj")) {
                JSONObject childObject = parentObject.getJSONObject("userObj");
                contact = new Contact();
                contact.setAddress(childObject.getString("address1"));
                contact.setContactNum(childObject.getString("contactNumber"));
                contact.setEmailAddress(childObject.getString("emailAddress"));
            }
        }  catch (JSONException e) {
            result = "error";
            Logger.e("Log",e);
            Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
            Crashlytics.logException(e);
        } finally {
            return contact;
        }
    }
}
