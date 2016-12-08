package com.oppo.sfamanagement.webmethods;

import com.oppo.sfamanagement.database.Logger;

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
            url = "productStoreId" + URLEncoder.encode(storeId,"UTF-8") +
                    "@storeName" + URLEncoder.encode(storeName,"UTF-8") +
                    "address" + URLEncoder.encode(storeAddress, "UTF-8") +
                    "latitude" + URLEncoder.encode(lattitude, "UTF-8") +
                    "longitude" + URLEncoder.encode(longitude, "UTF-8");

        } catch (Exception e) {
            Logger.e("Log",e);
        }finally {
            return url;
        }
    }

}
