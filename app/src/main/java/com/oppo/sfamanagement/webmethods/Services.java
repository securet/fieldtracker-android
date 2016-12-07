package com.oppo.sfamanagement.webmethods;

import com.oppo.sfamanagement.database.AppsConstant;

/**
 * Created by AllSmart-LT008 on 7/29/2016.
 */
public class Services{

    public static String domain= AppsConstant.isProduction?"ft.allsmart.in":"ft.allsmart.in";
    public static String DomainUrl = "http://"+domain+"/rest/s1/ft/";


    public static String USER_LOGIN	 = "user";
    public static String STORE_DETAIL	 = "stores";
    public static String LOGOUT	 = "logout";
}
