package com.oppo.sfamanagement.webmethods;

import com.oppo.sfamanagement.database.AppsConstant;

/**
 * Created by AllSmart-LT008 on 7/29/2016.
 */
public class Services{


    public static String domain= AppsConstant.isProduction?"ft.allsmart.in":"ft.allsmart.in";
    public static String DomainUrl = "http://"+domain+"/rest/s1/ft/";
    public static String DomainUrlImage = "http://"+domain+"/apps/ft/Requests/uploadImage";

    public static String USER_LOGIN	 = "user";
    public static String STORE_DETAIL	 = "stores";
    public static String STORE_LIST = "stores/user/list";
    public static String LOGOUT	 = "logout";
    public static String STORE_UPDATE = "stores";
    public static String ADD_STORE = "stores";
    public static String PROMOTER_LIST = "request/promoter/list";
    public static String UPDATE_PROMOTER = "request/promoter";
    public static String ADD_PROMOTER = "request/promoter";
 //   public static String UPLOAD_IMAGE = "/apps/ft/Requests/uploadImage";
}
