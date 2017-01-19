package com.allsmart.fieldtracker.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 7/17/2016.
 */
public class Preferences
{
    public static String LEAVE_COUNT = "LeaveCount";
    public static String PROMOTER_COUNT = "PromoterCount";
    public static String LATITUDE= "Latitude";
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    public static String USER_PHOTO = "userPhoto";
    public static String INLOCATION = "InLocation";
    public static String TIMEINOUTSTATUS = "TimeInOutStatus";
    public static String TIMEINTIME = "TimeInTime";
    public static String USERLATITUDE = "UserLatitude";
    public static String USERLONGITUDE = "UserLongitude";
    public static String LOCATIONSTATUS = "LocationStatus";
//    public static String SITEID = "SiteId";
    public static String HISTORY_COUNT = "HistoryCount";
    public static String SITENAME = "SiteName";

    public static String LONGITUDE = "Longitude";
    public static String SITE_ADDRESS = "SiteAddress";
    public static String SITE_ENTITY = "SiteEntity";
    public static String SITE_RADIUS = "SiteRadius";
    public static String ISLOGIN = "IsLogin";
    public static String USERNAME = "UserName";
    public static String USERFULLNAME = "UserFullName";
    public static String USERFIRSTNAME = "UserFirstName";
    public static String USERLASTNAME = "UserLastName";
    public static String USERID = "UserId";
    public static String USEREMAIL = "UserEmail";
    public static String ISEMPLOYEEVALID = "IsEmployeeValid";
    public static String USERBIRTHDAY = "userBirthDay";
    public static String BASIC_AUTH = "BasicAuth";
    public static String ROLETYPEID = "roleTypeId";
    public static String PARTYID = "partyId";
    public static String SHIFTTIME = "ShiftTime";
    public static String ISLAST = "isLast";
    public static String PROMOTERISLAST= "promoterIsLast";
    public static String STOREISLAST = "storeIsLast";
    public static String LEAVEISLAST = "leaveIsLast";
    public Preferences(Context context)
    {
        preferences = context.getSharedPreferences("AttendanceTracker", context.MODE_PRIVATE);
        edit			=	preferences.edit();
    }

    public void saveString(String strKey,String strValue)
    {
        edit.putString(strKey, strValue);
    }
    public void saveInt(String strKey,int value)
    {
        edit.putInt(strKey, value);
    }
    public void saveBoolean(String strKey,boolean value)
    {
        edit.putBoolean(strKey, value);
    }
    public void saveLong(String strKey,Long value)
    {
        edit.putLong(strKey, value);
    }
    public void saveDouble(String strKey,String value)
    {
        edit.putString(strKey, value);
    }

    public void remove(String strKey)
    {
        edit.remove(strKey);
    }
    public void commit()
    {
        edit.commit();
    }
    public String getString(String strKey,String defaultValue )
    {
        return preferences.getString(strKey, defaultValue);
    }
    public boolean getBoolean(String strKey,boolean defaultValue)
    {
        return preferences.getBoolean(strKey, defaultValue);
    }
    public int getInt(String strKey,int defaultValue)
    {
        return preferences.getInt(strKey, defaultValue);
    }
    public double getDouble(String strKey,double defaultValue)
    {
        return	Double.parseDouble(preferences.getString(strKey, ""+defaultValue));
    }
    public long getLong(String strKey)
    {
        return preferences.getLong(strKey, 0);
    }

    public void clearPreferences() {
        edit.clear();
        edit.commit();
    }
}
