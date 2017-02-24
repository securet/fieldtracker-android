package com.oppo.fieldtracker.service;

import android.content.Context;
import android.os.Bundle;

import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.parsers.ContactSupportParser;
import com.oppo.fieldtracker.parsers.ForcedUpdateParser;
import com.oppo.fieldtracker.parsers.ForgotPasswordParser;
import com.oppo.fieldtracker.parsers.LeaveApproveParser;
import com.oppo.fieldtracker.parsers.LeaveRequisitionParser;
import com.oppo.fieldtracker.parsers.PromoterApprovalListParser;
import com.oppo.fieldtracker.parsers.PromoterApproveParser;
import com.oppo.fieldtracker.parsers.ReporteeHistoryParser;
import com.oppo.fieldtracker.parsers.ReporteeListParser;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.parsers.AddPromoterParser;
import com.oppo.fieldtracker.parsers.AddStoreParser;
import com.oppo.fieldtracker.parsers.ChangePasswordParser;
import com.oppo.fieldtracker.parsers.HistoryListParser;
import com.oppo.fieldtracker.parsers.ImageUploadParser;
import com.oppo.fieldtracker.parsers.LeaveApplyParser;
import com.oppo.fieldtracker.parsers.LeaveListParser;
import com.oppo.fieldtracker.parsers.LeaveTypeParser;
import com.oppo.fieldtracker.parsers.PromoterListParser;
import com.oppo.fieldtracker.parsers.PromoterUpdateParser;
import com.oppo.fieldtracker.parsers.StoreDetailParser;
import com.oppo.fieldtracker.parsers.StoreListParser;
import com.oppo.fieldtracker.parsers.StoreUpdateParser;
import com.oppo.fieldtracker.parsers.TimeInOutParser;
import com.oppo.fieldtracker.parsers.TimeLineParser;
import com.oppo.fieldtracker.parsers.UserDetailParser;
import com.oppo.fieldtracker.parsers.UserStoreDetailParser;
import com.oppo.fieldtracker.webmethods.CustomAsyncTask;
import com.oppo.fieldtracker.webmethods.RestHelper;


/**
 * Created by AllSmart-LT008 on 8/5/2016.
 */
public class LoaderServices extends CustomAsyncTask
{
    private Context context;
    LoaderMethod strType;
    Bundle args;
    Preferences preferences;
    public LoaderServices(Context context, LoaderMethod strType, Bundle args) {
        super(context);
        this.context = context;
        this.args = args;
        this.strType = strType;
        preferences = new Preferences(context);

    }
    @Override
    public Object loadInBackground()
    {
//        Log.e("URL"+strType,args.getString(AppsConstant.URL));
        String response ="";// Base64
        if(strType==LoaderMethod.USER_LOGIN)
            response = new RestHelper().makeRestCallAndGetResponseLogin(args.getString(AppsConstant.URL),args.getString(AppsConstant.USER), args.getString(AppsConstant.PASSWORD),preferences);
//        else  if(strType==LoaderMethod.IMAGE_UPLOAD)
//            response = new RestHelper().makeRestCallAndGetResponseImageUpload(args.getString(AppsConstant.URL), args.getString(AppsConstant.FILE),preferences);
        else  if(strType==LoaderMethod.IMAGE_UPLOAD)
            response = new RestHelper().makeRestCallAndGetResponseImageUpload(args.getString(AppsConstant.URL), args.getString(AppsConstant.FILE), args.getString(AppsConstant.FILEPURPOSE),preferences);
        else
            response = new RestHelper().makeRestCallAndGetResponse(args.getString(AppsConstant.URL),args.getString(AppsConstant.METHOD), args.getString(AppsConstant.PARAMS),preferences);
       switch (strType)
        {
            case USER_LOGIN:
                return new UserDetailParser(response,preferences).Parse();
            case USER_STORE_DETAIL:
                return new UserStoreDetailParser(response,preferences).Parse();
            case STORE_DETAIL:
                return new StoreDetailParser(response,preferences).Parse();
            case STORE_LIST:
                return new StoreListParser(response,preferences).Parse();
            case STORE_UPDATE:
                return new StoreUpdateParser(response).Parse();
            case ADD_STORE:
                return new AddStoreParser(response).Parse();
            case PROMOTER_LIST:
                return new PromoterListParser(response,preferences).Parse();
            case ADD_PROMOTER:
                return new AddPromoterParser(response).Parse();
            case UPDATE_PROMOTER:
                return new PromoterUpdateParser(response).Parse();
            case IMAGE_UPLOAD:
                return new ImageUploadParser(response).Parse();
            case HISTORY_LIST:
                return new HistoryListParser(response,preferences).Parse();
            case TIMEINOUT:
                return new TimeInOutParser(response,AppsConstant.REASON).Parse();
            case TIMELINE_LIST:
                return new TimeLineParser(response,preferences).Parse();
            case LEAVE_LIST:
                return new LeaveListParser(response,preferences).Parse();
            case LEAVE_TYPES:
                return new LeaveTypeParser(response).Parse();
            case APLLY_LEAVES:
                return new LeaveApplyParser(response).Parse();
            case CHANGE_PASSWORD:
                return new ChangePasswordParser(response).Parse();
            case LEAVE_REQUISITION:
                return new LeaveRequisitionParser(response,preferences).Parse();
            case PROMOTER_APPROVALS_LIST:
                return new PromoterApprovalListParser(response,preferences).Parse();
            case APPROVE_PROMOTER:
                return new PromoterApproveParser(response).Parse();
            case APPROVE_LEAVE:
                return new LeaveApproveParser(response).Parse();
            case FORGOT_PASSWORD:
                return new ForgotPasswordParser(response).Parse();
            case FORCED_UPDATE:
                return new ForcedUpdateParser(response,preferences).Parse();
            case REPORTEE_HISTORY:
                return new ReporteeHistoryParser(response,preferences).Parse();
            case REPORTEE_LIST:
                return new ReporteeListParser(response,preferences).Parse();
            case CONTACT_SUPPORT:
                return new ContactSupportParser(response).Parse();

            default:
                return  null;

        }

    }
}