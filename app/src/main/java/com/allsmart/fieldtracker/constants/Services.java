package com.allsmart.fieldtracker.constants;

import com.allsmart.fieldtracker.constants.AppsConstant;

/**
 * Created by AllSmart-LT008 on 7/29/2016.
 */
public class Services{


    public static String domain= AppsConstant.isProduction?"ft.allsmart.in":"ft.allsmart.in";
    public static String DomainUrl = "http://"+domain+"/rest/s1/ft/";
    public static String DomainUrlImage = "http://"+domain+"/apps/ft/Requests/uploadImage";
    public static String DomainUrlServerImage = "http://ft.allsmart.in/uploads/uid/";

    public static String USER_LOGIN	 = "user";
    public static String STORE_DETAIL	 = "stores";
    public static String STORE_LIST = "stores/user/list";
    public static String LOGOUT	 = "logout";
    public static String STORE_UPDATE = "stores";
    public static String ADD_STORE = "stores";
    public static String PROMOTER_LIST = "request/promoter/list";
    public static String UPDATE_PROMOTER = "request/promoter";
    public static String ADD_PROMOTER = "request/promoter";
    public static String TIME_IN_OUT = "attendance/log";
    public static String HISTORY_LIST = "attendance/log";
    public static String LEAVE_LIST = "leaves/my/list";
    public static String LEAVE_TYPES = "leaves/types";
    public static String APPLY_LEAVES = "leaves";
    public static String UPDATE_LEAVES = "leaves";
    public static String CHANGE_PASSWORD = "user/changePassword";
}
