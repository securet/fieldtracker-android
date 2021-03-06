package com.allsmart.fieldtracker.constants;

/**
 * Created by AllSmart-LT008 on 7/29/2016.
 */
public class Services{


  //  public static String domain= AppsConstant.isProduction?"oppo.allsmart.in":"oppo.allsmart.in";
    public static String domain= "";
    public static String DomainUrl = "http://"+domain+"/rest/s1/ft/";
    public static String DomainUrlImage = "http://"+domain+"/apps/ft/Requests/uploadImage";
    public static String DomainUrlServerImage = "http://"+domain+"/uploads/uid/";

    public static String USER_LOGIN	 = "user";
    public static String STORE_DETAIL	 = "stores";
    public static String STORE_LIST = "stores/user/list";
    public static String LOGOUT	 = "logout";
    public static String STORE_UPDATE = "stores";
    public static String ADD_STORE = "stores";
    public static String PROMOTER_LIST = "request/promoter/list";
    public static String PROMOTER_APPROVALS_LIST = "request/promoter/approvalRequests";
    public static String UPDATE_PROMOTER = "request/promoter";
    public static String ADD_PROMOTER = "request/promoter";
    public static String TIME_IN_OUT = "attendance/log";
    public static String HISTORY_LIST = "attendance/log";
    public static String LEAVE_LIST = "leaves/my/list";
    public static String LEAVE_TYPES = "leaves/types";
    public static String APPLY_LEAVES = "leaves";
    public static String UPDATE_LEAVES = "leaves";
    public static String CHANGE_PASSWORD = "user/changePassword";
    public static String LEAVE_REQUISITION = "leaves/requisitions";
    public static String APPROVE_PROMOTER = "request/promoter/approve";
    public static String REJECT_PROMOTER = "request/promoter/reject";
    public static String APPROVE_LEAVE = "approveLeave";
    public static String REJECT_LEAVE = "rejectLeave";
    public static String FORGOT_PASSWORD = "resetPassword";
    public static String FORCED_UPDATE = "checkForceUpdate";
    public static String REPORTEE_LIST = "user/reportees";
    public static String CONTACT_SUPPORT = "customerSupportInfo";
    public static String REPORTEE_HISTORY = "attendance/reportee/log";

    public static void setDomain(String enterDomain){
      domain = enterDomain;
      DomainUrl = "http://"+domain+"/rest/s1/ft/";
      DomainUrlServerImage = "http://"+domain+"/uploads/uid/";
      DomainUrlImage = "http://"+domain+"/apps/ft/Requests/uploadImage";
    }

}
