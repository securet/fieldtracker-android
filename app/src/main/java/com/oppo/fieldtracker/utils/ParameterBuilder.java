package com.oppo.fieldtracker.utils;

import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.model.TimeInOutDetails;

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
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return urlParameters;
        }
    }

    public static String getApplyLeave(String leaveTypeEnumId, String leaveReasonEnumId, String description, String fromDate, String thruDate, String organizationId) {
        String urlParameters = "";
        try {
            urlParameters = "leaveTypeEnumId=" + URLEncoder.encode(leaveTypeEnumId,"UTF-8") +
                    "&leaveReasonEnumId=" + URLEncoder.encode(leaveReasonEnumId, "UTF-8") +
                    "&description=" + URLEncoder.encode(description, "UTF-8") +
                    "&fromDate=" + URLEncoder.encode(fromDate, "UTF-8") +
                    "&thruDate=" + URLEncoder.encode(thruDate, "UTF-8") +
                    "&organizationId=" + URLEncoder.encode(organizationId, "UTF-8");
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return urlParameters;
        }
    }
    public static String getUpdateLeave(String leaveTypeEnumId, String leaveReasonEnumId, String description, String fromDate, String thruDate, String partyRelationshipId) {
        String urlParameters = "";
        try {
            urlParameters = "leaveTypeEnumId=" + URLEncoder.encode(leaveTypeEnumId,"UTF-8") +
                    "&leaveReasonEnumId=" + URLEncoder.encode(leaveReasonEnumId, "UTF-8") +
                    "&description=" + URLEncoder.encode(description, "UTF-8") +
                    "&fromDate=" + URLEncoder.encode(fromDate, "UTF-8") +
                    "&thruDate=" + URLEncoder.encode(thruDate, "UTF-8") +
                    "&partyRelationshipId=" + URLEncoder.encode(partyRelationshipId, "UTF-8");
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return urlParameters;
        }
    }

    public static String getUserId(String userId) {
        String url = "";
        try {
            url = "userId=" + URLEncoder.encode(userId,"UTF-8");
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String getStoreUpdate(String storeId,String storeAddress) {
        String url = "" ;
        try {
            url = "productStoreId=" + URLEncoder.encode(storeId,"UTF-8") +
                  //  "&storeName=" + URLEncoder.encode(storeName,"UTF-8") +
                    "&address=" + URLEncoder.encode(storeAddress, "UTF-8") ;
                  //  "&latitude=" + URLEncoder.encode(lattitude, "UTF-8") +
                  //  "&longitude=" + URLEncoder.encode(longitude, "UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        }finally {
            return url;
        }
    }

    public static String getAddStore(String storeName, String storeAddress, String latitude, String longitude, String proximity) {
        String url = "";
        try {
            url = "storeName=" + URLEncoder.encode(storeName,"UTF-8") +
                    "&address=" + URLEncoder.encode(storeAddress,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(latitude,"UTF-8") +
                    "&longitude=" + URLEncoder.encode(longitude,"UTF-8")+
                    "&proximityRadius=" + URLEncoder.encode(proximity+"","UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String updateStore(String storeName, String storeAddress, String latitude, String longitude, String proximity,String storeImage) {
        String url = "";
        try {
            url = "storeName=" + URLEncoder.encode(storeName,"UTF-8") +
                    "&address=" + URLEncoder.encode(storeAddress,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(latitude,"UTF-8") +
                    "&longitude=" + URLEncoder.encode(longitude,"UTF-8")+
                    "&proximityRadius=" + URLEncoder.encode(proximity+"","UTF-8")+
                    "&loationImagePath=" + URLEncoder.encode(storeImage,"UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String getChangePassword(String oldPassword, String newPass, String confirmPass) {
        String url = "";
        try {
            url = "oldPassword=" + URLEncoder.encode(oldPassword,"UTF-8") +
                    "&newPassword=" + URLEncoder.encode(newPass,"UTF-8") +
                    "&newPasswordVerify=" + URLEncoder.encode(confirmPass,"UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String getAddPromoter(String rqtAddPromoter,String orgId,String description, String fName, String lName, String sPh, String email, String sAdd, String s, String reqSubmitted, String addPromoter, String s1, String s2, String s3) {
        String url = "";
        try {
            url = "requestType=" + URLEncoder.encode(rqtAddPromoter,"UTF-8") +
                    "&description=" + URLEncoder.encode(description,"UTF-8") +
                    "&organizationId=" + URLEncoder.encode(orgId,"UTF-8") +
                    "&firstName=" + URLEncoder.encode(fName,"UTF-8") +
                    "&lastName=" + URLEncoder.encode(lName,"UTF-8") +
                    "&phone=" + URLEncoder.encode(sPh,"UTF-8") +
                    "&address=" + URLEncoder.encode(sAdd,"UTF-8") +
                    "&emailId=" + URLEncoder.encode(email,"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(s,"UTF-8") +
                    "&statusId=" + URLEncoder.encode(reqSubmitted,"UTF-8") +
                    "&requestTypeEnumId=" + URLEncoder.encode(addPromoter,"UTF-8") +
                    "&aadharIdPath=" + URLEncoder.encode(s2,"UTF-8") +
                    "&userPhoto=" + URLEncoder.encode(s1,"UTF-8") +
                    "&addressIdPath=" + URLEncoder.encode(s3,"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
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
                    "&productStoreId=" + URLEncoder.encode(preferences.getString(Preferences.PARTYID,""),"UTF-8") +
                    "&actionType=" + URLEncoder.encode(actionType,"UTF-8") +
                    "&actionImage=" + URLEncoder.encode(actionImage,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(preferences.getString(Preferences.USERLATITUDE,""),"UTF-8") +
                    "&longitude=" + URLEncoder.encode(preferences.getString(Preferences.USERLONGITUDE,""),"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        }finally {
            return url;
        }
    }

    public static String getTimeinOut(Preferences preferences, TimeInOutDetails details, String imagePath) {
        String url = "";
        try {
            String clockDate = CalenderUtils.getTimeZoneDate(CalenderUtils.DateFormateWithZone, details.getClockDate());
            System.out.println("This is formatted date "  +clockDate);
            url = "username=" + URLEncoder.encode(details.getUsername(),"UTF-8") +
                    "&workEffortTypeEnumId=" + URLEncoder.encode("WetAvailable","UTF-8") +
                    "&clockDate=" + URLEncoder.encode(clockDate,"UTF-8") +
                    "&purposeEnumId=" + URLEncoder.encode("WepAttendance","UTF-8") +
                    "&comments=" + URLEncoder.encode(details.getComments(),"UTF-8") +
                    "&productStoreId=" + URLEncoder.encode(preferences.getString(Preferences.PARTYID,""),"UTF-8") +
                    "&actionType=" + URLEncoder.encode(details.getActionType(),"UTF-8") +
                    "&actionImage=" + URLEncoder.encode(imagePath,"UTF-8") +
                    "&latitude=" + URLEncoder.encode(details.getLatitude(),"UTF-8") +
                    "&longitude=" + URLEncoder.encode(details.getLongitude(),"UTF-8");


        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
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
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String getPromoterApprove(String requestId) {
        String url = "";
        try {
            url = "requestId=" + URLEncoder.encode(requestId,"UTF-8");
        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }

    public static String getLeaveApprove(String partyRelationshipId,String fromDate,
                                         String thruDate,String leaveTypeEnumId,
                                         String leaveReasonEnumId,String leaveApproved,String description) {
        String url = "";
        try {
            url = "partyRelationshipId=" + URLEncoder.encode(partyRelationshipId,"UTF-8") +
                    "&fromDate=" + URLEncoder.encode(fromDate,"UTF-8") +
                    "&description=" + URLEncoder.encode(description,"UTF-8") +
                    "&thruDate=" + URLEncoder.encode(thruDate,"UTF-8") +
                    "&leaveTypeEnumId=" + URLEncoder.encode(leaveTypeEnumId,"UTF-8") +
                    "&leaveReasonEnumId=" + URLEncoder.encode(leaveReasonEnumId,"UTF-8") +
                    "&leaveApproved=" + URLEncoder.encode(leaveApproved,"UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
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
            Crashlytics.log(1,"ParameterBuilder","ParameterBuilder");
            Crashlytics.logException(e);
        } finally {
            return url;
        }
    }



}
