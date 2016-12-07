package com.oppo.sfamanagement;

/**
 * Created by AllSmart-LT008 on 10/18/2016.
 */

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oppo.sfamanagement.database.API;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.database.RestHelper;
import com.oppo.sfamanagement.database.Utils;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends Activity implements LoaderManager.LoaderCallbacks , View.OnClickListener {

    Button loginBtn;
    EditText emailET, passwordET;
    ProgressDialog pd;
    public Preferences preferences;
    private boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new Preferences(LoginActivity.this);
        isLogin = preferences.getBoolean(Preferences.ISLOGIN, false);
        Utils.setAppVersion(this);
        if (isLogin) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // resources
        loginBtn = (Button) findViewById(R.id.loginBtn);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);

        // lictener
        loginBtn.setOnClickListener(this);

        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if ((emailET.getText().toString().trim().equals(null))
                        || (emailET.getText().toString().trim().equals(""))
                        || (passwordET.getText().toString().trim().equals(""))
                        || (passwordET.getText().toString().trim().equals(null))) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid username and password.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.USER_LOGIN));
                    b.putString(AppsConstant.USER, emailET.getText().toString().trim() );
                    b.putString(AppsConstant.PASSWORD, passwordET.getText().toString().trim());
                    getLoaderManager().initLoader(LoaderConstant.USER_LOGIN,b,LoginActivity.this).forceLoad();
//                    LoginTask task = new LoginTask();
//                    task.execute(new String[] {
//                            emailET.getText().toString().trim(),
//                            passwordET.getText().toString().trim() });
                }

                break;
        }
    }
//    private class LoginTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.show();
//        };
//        @Override
//        protected String doInBackground(String... params) {
//            String response = "";
//            try {
//                response = API.LoginRest(params[0], params[1]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                response = "";
//            }
//            return response;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            pd.dismiss();
//            if(API.DEBUG)
//                System.out.println("The Message Is: " + result);
//            if (!(result.equals("No Internet")) || !(result.equals(""))) {
//                try {
//                    if(result.toString().contains("status") && (new JSONObject(result).getString("status").toString().equals("success"))){
//                        JSONObject data = new JSONObject(result);
//                        JSONObject obj = data.getJSONObject("data");
//                        JSONObject userLogin = obj.getJSONObject("userLogin");
//                        if(API.DEBUG){
//                            System.out.println("====" + obj.getString("userId"));
//                            System.out.println("====" + obj.getString("userId"));
//                            System.out.println("====" + obj.getString("fullName"));
//                            System.out.println("====" + userLogin.getString("enabled"));
//                            //System.out.println("====" + obj.getString("EmployeeToken"));
//                        }
//                        if (userLogin.getString("enabled").equals("false")) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Invalid Username/Password",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//							/*Toast.makeText(getApplicationContext(),
//									"You are successfully login.",
//									Toast.LENGTH_SHORT).show();*/
//                            // Storing data to Device
//
//                            // Save data to Shared Preferences
//                            saveAllDataToSharedPreferences(
//                                    obj.getString("userId").toString(),
//                                    obj.getString("userId").toString(),
//                                    obj.getString("fullName").toString(),
//                                    userLogin.getString("enabled").toString(),
//                                    //obj.getString("EmployeeToken")
//                                    emailET.getText().toString().trim(),
//                                    passwordET.getText().toString().trim()
//                            );
//
//                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                            finish();
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(), ""+new JSONObject(result).getString("messages").toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//                } catch (Exception e) {
//                    if(API.DEBUG){
//                        e.printStackTrace();
//                    }
//
//                    Toast.makeText(getApplicationContext(),
//                            "Error in response. Please try again.",
//                            Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Error in response. Please try again.",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }

//    private void saveAllDataToSharedPreferences(String EmployeeID, String EmployeeCode, String EmployeeName, String EmployeeValid, String UserName, String Password) {
//        preferences.saveBoolean(Preferences.ISLOGIN, true); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.EMPLOYEEID, EmployeeID); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.EMPLOYEECODE, EmployeeCode); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.EMPLOYEENAME, EmployeeName); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.ISEMPLOYEEVALID, EmployeeValid); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.USERNAME, UserName); // value to store
//        preferences.commit();
//        preferences.saveString(Preferences.USERPASSWORD, Password); // value to store
//        preferences.commit();
//
//    }
    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        pd.show();
        switch (id)
        {
            case LoaderConstant.USER_LOGIN:
                return new LoaderServices(LoginActivity.this, LoaderMethod.USER_LOGIN, args);
            default:
                return null;
        }
    }
    @Override
    public void onLoaderReset (Loader loader){

    }
    @Override
    public void onLoadFinished(Loader loader, Object data)
    {
        pd.dismiss();
        switch (loader.getId())
        {
            case LoaderConstant.USER_LOGIN:
                if(data!=null && data instanceof String && ((String)data).equalsIgnoreCase("Success")){
                    preferences.saveBoolean(Preferences.ISLOGIN,true);
                    preferences.commit();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();

                }else if(data!=null && data instanceof String && !TextUtils.isEmpty((String)data)){
                    preferences.saveBoolean(Preferences.ISLOGIN,false);
                    preferences.commit();
                    Toast.makeText(getApplicationContext(),
                            (String)data,
                            Toast.LENGTH_SHORT).show();
                } else {
                    preferences.saveBoolean(Preferences.ISLOGIN,false);
                    preferences.commit();
                                        Toast.makeText(getApplicationContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }


                break;
        }
        getLoaderManager().destroyLoader(loader.getId());
    }

}