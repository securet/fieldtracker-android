package com.oppo.sfamanagement.webmethods;

import android.preference.Preference;

import com.oppo.sfamanagement.database.CalenderUtils;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.TimeInOutDetails;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by AllSmart-LT008 on 8/5/2016.
 */
public class ParameterBuilder {


    public static String getSignUp(String logintoken, String strUserId, String strUserName, String countryCode, String strUserMobile, String strPassWord) {
        String urlParameters = "";
        try {
            urlParameters = "logintoken=" + URLEncoder.encode(logintoken, "UTF-8") +
                    "&USERNAME=" + URLEncoder.encode(strUserId, "UTF-8") +
                    "&USER_FIRST_NAME=" + URLEncoder.encode(strUserName, "UTF-8") +
                    "&countryCode=" + URLEncoder.encode(countryCode, "UTF-8") +
                    "&signUpPassword=" + URLEncoder.encode(strPassWord, "UTF-8") +
                    "&CUSTOMER_MOBILE_CONTACT=" + URLEncoder.encode(strUserMobile, "UTF-8");
        } catch (Exception e) {
            Logger.e("Log", e);
        } finally {
            return urlParameters;
        }
    }

    public static String getStoreUpdate(String storeId, String storeName, String storeAddress, String lattitude, String longitude) {
        String url = "" ;
        try {
            url = "productStoreId=" + URLEncoder.encode(storeId,"UTF-8") +
                    "&storeName=" + URLEncoder.encode(storeName,"UTF-8") +
                    "&address=" + URLEncoder.encode(storeAddress, "UTF-8") +
                    "&latitude=" + URLEncoder.encode(lattitude, "UTF-8") +
                    "&longitude=" + URLEncoder.encode(longitude, "UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
        }finally {
            return url;
        }
    }

    public static String getAddStore(String storeName, String storeAddress,String latitude,String longitude) {
        String url = "";
        try {
            url = "storeName=" + URLEncoder.encode(storeName,"UTF-8") +
                    "&address=" + URLEncoder.encode(storeAddress,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(latitude,"UTF-8") +
                    "&longitude=" + URLEncoder.encode(longitude,"UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
        } finally {
            return url;
        }
    }

    public static String getAddPromoter(String rqtAddPromoter, String fName, String lName, String sPh, String email, String sAdd, String s, String reqSubmitted, String addPromoter, String s1, String s2, String s3) {
        String url = "";
        try {
            url = "requestType=" + URLEncoder.encode(rqtAddPromoter,"UTF-8") +
                    "&firstName=" + URLEncoder.encode(fName,"UTF-8") +
                    "&lastName=" + URLEncoder.encode(lName,"UTF-8") +
                    "&phone=" + URLEncoder.encode(sPh,"UTF-8") +
                    "&address=" + URLEncoder.encode(sAdd,"UTF-8") +
                    "&emailId=" + URLEncoder.encode(email,"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(s,"UTF-8") +
                    "&statusId=" + URLEncoder.encode(reqSubmitted,"UTF-8") +
                    "&requestTypeEnumId=" + URLEncoder.encode(addPromoter,"UTF-8") +
                    "&aadharIdPath=" + URLEncoder.encode(s1,"UTF-8") +
                    "&userPhoto=" + URLEncoder.encode(s2,"UTF-8") +
                    "&addressIdPath=" + URLEncoder.encode(s3,"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
        }finally {
            return url;
        }
    }

    public static String getTimeinOut(Preferences preferences, String comments, String actionType,String actionImage) {
        String url = "";
        try {
            String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateFormate);
            url = "username=" + URLEncoder.encode(preferences.getString(Preferences.USERNAME,""),"UTF-8") +
                    "&workEffortTypeEnumId=" + URLEncoder.encode("WetAvailable","UTF-8") +
                    "&clockDate=" + URLEncoder.encode(clockDate,"UTF-8") +
                    "&purposeEnumId=" + URLEncoder.encode("WepAttendance","UTF-8") +
                    "&comments=" + URLEncoder.encode(comments,"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(preferences.getString(Preferences.SITEID,""),"UTF-8") +
                    "&actionType=" + URLEncoder.encode(actionType,"UTF-8") +
                    "&actionImage=" + URLEncoder.encode(actionImage,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(preferences.getString(Preferences.USERLATITUDE,""),"UTF-8") +
                    "&longitude=" + URLEncoder.encode(preferences.getString(Preferences.USERLONGITUDE,""),"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
        }finally {
            return url;
        }
    }

    public static String getTimeinOut(Preferences preferences, TimeInOutDetails details, String imagePath) {
        String url = "";
        try {
            String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateFormate);
            url = "username=" + URLEncoder.encode(details.getUsername(),"UTF-8") +
                    "&workEffortTypeEnumId=" + URLEncoder.encode("WetAvailable","UTF-8") +
                    "&clockDate=" + URLEncoder.encode(details.getClockDate(),"UTF-8") +
                    "&purposeEnumId=" + URLEncoder.encode("WepAttendance","UTF-8") +
                    "&comments=" + URLEncoder.encode(details.getComments(),"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(preferences.getString(Preferences.SITEID,""),"UTF-8") +
                    "&actionType=" + URLEncoder.encode(details.getActionType(),"UTF-8") +
                    "&actionImage=" + URLEncoder.encode(imagePath,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(details.getLatitude(),"UTF-8") +
                    "&longitude=" + URLEncoder.encode(details.getLongitude(),"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
        }finally {
            return url;
        }
    }

    public static String getImageUpload(String snapShotFile) {
        String url = "";
        try {
            url = "snapShotFile=" + URLEncoder.encode(snapShotFile, "UTF-8");
        } catch (Exception e) {
            Logger.e("Log",e);
        } finally {
            return url;
        }
    }

    public static String getPromoterUpdate(String requestId,String requestType,String firstName,
                                           String lastName,String phone,String address,String emailId,
                                           String productStoreId,String statusId,String requestTypeEnumId,
                                           String description,String aadharIdPath,String userPhoto,String addressIdPath) {
        String url = "";
        try {
            url = "requestId=" + URLEncoder.encode(requestId,"UTF-8") +
                    "&requestType=" + URLEncoder.encode(requestType,"UTF-8") +
                    "&firstName=" + URLEncoder.encode(firstName,"UTF-8") +
                    "&lastName=" + URLEncoder.encode(lastName,"UTF-8") +
                    "&phone=" + URLEncoder.encode(phone,"UTF-8") +
                    "&address=" + URLEncoder.encode(address,"UTF-8") +
                    "&emailId=" + URLEncoder.encode(emailId,"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(productStoreId,"UTF-8") +
                    "&statusId=" + URLEncoder.encode(statusId,"UTF-8") +
                    "&requestTypeEnumId=" + URLEncoder.encode(requestTypeEnumId,"UTF-8") +
                    "&description=" + URLEncoder.encode(description,"UTF-8") +
                    "&aadharIdPath=" + URLEncoder.encode(aadharIdPath,"UTF-8") +
                    "&userPhoto=" + URLEncoder.encode(userPhoto,"UTF-8") +
                    "&addressIdPath=" + URLEncoder.encode(addressIdPath,"UTF-8") ;
        } catch (Exception e) {
            Logger.e("Log",e);
        } finally {
            return url;
        }
    }



}
