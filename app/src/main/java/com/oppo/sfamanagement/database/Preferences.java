package com.oppo.sfamanagement.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 7/17/2016.
 */
public class Preferences
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    public static String INLOCATION = "InLocation";
    public static String SITEID = "SiteId";
    public static String SITENAME = "SiteName";
    public static String LATITUDE = "Latitude";
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
