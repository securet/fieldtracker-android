package com.oppo.fieldtracker.activity;

/**
 * Created by AllSmart-LT008 on 10/18/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.utils.ParameterBuilder;
import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.utils.Logger;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.utils.Utils;
import com.oppo.fieldtracker.constants.LoaderConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.service.LoaderServices;
import com.oppo.fieldtracker.constants.Services;
import com.oppo.fieldtracker.utils.UrlBuilder;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends Activity implements LoaderManager.LoaderCallbacks , View.OnClickListener {

    Button loginBtn,resetBtn;
    EditText emailET, passwordET, newPass, confirmPass;
    TextView forgotPass;
//    ProgressDialog pd;
    public Preferences preferences;
    private String emailPattern = "^[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$ ";
    private Pattern pattern = Pattern.compile(emailPattern);
    private Matcher matcher;
    private boolean isLogin = false;
    private boolean isForgotPassScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        preferences = new Preferences(LoginActivity.this);
        setContentView(R.layout.activity_login);

        isLogin = preferences.getBoolean(Preferences.ISLOGIN, false);
        Utils.setAppVersion(this);
        if (isLogin) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // resources
        loginBtn = (Button) findViewById(R.id.loginBtn);
        resetBtn = (Button) findViewById(R.id.resetButton);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        newPass = (EditText) findViewById(R.id.etNewPass);
        confirmPass = (EditText) findViewById(R.id.etConfirmPass);
        forgotPass = (TextView) findViewById(R.id.tvForgotPass);

        // lictener
        loginBtn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

    }
    private void showResetViews() {
        forgotPass.setVisibility(View.INVISIBLE);
        emailET.setVisibility(View.INVISIBLE);
        passwordET.setVisibility(View.INVISIBLE);
        loginBtn.setVisibility(View.INVISIBLE);
        isForgotPassScreen = true;
        newPass.setVisibility(View.VISIBLE);
        confirmPass.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.VISIBLE);
    }

    private void showLoginViews() {
        forgotPass.setVisibility(View.VISIBLE);
        emailET.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
      //  isForgotPassScreen = false;
        newPass.setVisibility(View.INVISIBLE);
        confirmPass.setVisibility(View.INVISIBLE);
        resetBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        if(isForgotPassScreen) {
            showLoginViews();
            isForgotPassScreen = false;
            return;
        } else {
            finish();
        }
        //return;
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
                    if(!TextUtils.isEmpty(emailET.getText().toString())){
                        if(emailValidator(emailET.getText().toString())) {

                            if(checkLocationPermision()) {
                                LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                                boolean gps_enabled = false;
                                boolean network_enabled = false;

                                try {
                                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                } catch (Exception e) {
                                    Logger.e("Log", e);
                                    Crashlytics.log(1, getClass().getName(), "Error in Map Fragment");
                                    Crashlytics.logException(e);
                                }

                                try {
                                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                                } catch (Exception e) {
                                    Logger.e("Log", e);
                                    Crashlytics.log(1, getClass().getName(), "Error in Map Fragment");
                                    Crashlytics.logException(e);
                                }

                                if (!gps_enabled && !network_enabled) {
                                    // notify user
                                    final Dialog dialog = new Dialog(LoginActivity.this,R.style.DialogTheme);

                                    dialog.setContentView(R.layout.location_button);
                                    Button enable = (Button) dialog.findViewById(R.id.enable);
                                    Button cancel = (Button) dialog.findViewById(R.id.cancelGps);
                                    enable.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(myIntent);
                                        }
                                    });
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }  else {
                                    Bundle b = new Bundle();
                                    b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.USER_LOGIN));
                                    b.putString(AppsConstant.USER, emailET.getText().toString().trim() );
                                    b.putString(AppsConstant.PASSWORD, passwordET.getText().toString().trim());
                                    getLoaderManager().initLoader(LoaderConstant.USER_LOGIN,b,LoginActivity.this).forceLoad();
                                }
                            } else {
                                askLocationPermision();
                            }

                        } else {
                            displayMessage("Enter proper email");
                        }
                    } else {
                        displayMessage("Username is required");
                    }
                }

                break;
            case R.id.tvForgotPass:

                if(!TextUtils.isEmpty(emailET.getText().toString())){

                    if(emailValidator(emailET.getText().toString())) {
                        showResetViews();
                    } else {
                        displayMessage("Enter proper email");
                    }
                } else {
                    displayMessage("Username is required");
                }
                break;
            case R.id.resetButton:
                String nPass = newPass.getText().toString();
                String cPass = confirmPass.getText().toString();
                if(!TextUtils.isEmpty(nPass) ) {
                    if(!TextUtils.isEmpty(cPass)) {
                        if(nPass.equals(cPass)) {
                  //          showLoginViews();
                            if(preferences == null) {
                                preferences = new Preferences(LoginActivity.this);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.FORGOT_PASSWORD));
                            bundle.putString(AppsConstant.METHOD,AppsConstant.POST);
                            bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getUserId(preferences.getString(Preferences.USERNAME,"")));
                            getLoaderManager().initLoader(LoaderConstant.FORGOT_PASSWORD,bundle,LoginActivity.this).forceLoad();
                        } else {
                            displayMessage("Password does not match");
                        }
                    } else {
                        displayMessage("Type Confirm Password");
                    }
                } else {
                    displayMessage("Type New Password");
                }
                break;
        }
    }
    private boolean emailValidator(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        showHideProgressForLoder(false);
        switch (id)
        {
            case LoaderConstant.USER_LOGIN:
                return new LoaderServices(LoginActivity.this, LoaderMethod.USER_LOGIN, args);
            case LoaderConstant.FORGOT_PASSWORD:
                return new LoaderServices(LoginActivity.this,LoaderMethod.FORGOT_PASSWORD,args);
            default:
                return null;
        }
    }


    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private final static int REQ_LOCATION_PERMISSION = 1004;


    private boolean checkLocationPermision() {
        return (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askLocationPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQ_LOCATION_PERMISSION);
        }
    }


    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLoaderReset (Loader loader){

    }
    @Override
    public void onLoadFinished(Loader loader, Object data)
    {
        showHideProgressForLoder(true);
        switch (loader.getId())
        {
            case LoaderConstant.USER_LOGIN:
                if(data!=null && data instanceof String && ((String)data).equalsIgnoreCase("success")) {
                    preferences.saveBoolean(Preferences.ISLOGIN, true);
                    preferences.commit();


                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();




                }else if(data!=null && data instanceof String && !((String) data).equalsIgnoreCase("error") && !((String) data).equalsIgnoreCase("success")){
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
            case LoaderConstant.FORGOT_PASSWORD:
                if(data != null && data instanceof String) {
                    String result = (String) data;
                    if(result.equals("success")) {
                        displayMessage("Forgot reset link is send to your mail");
                        showLoginViews();
                    } else if(!result.equals("success") && !result.equals("error")) {
                        displayMessage(result);
                    } else {
                        displayMessage("Error in response");
                    }
                } else {
                    displayMessage("Error in response. Please try again.");
                }
        }
        getLoaderManager().destroyLoader(loader.getId());
    }
    String SHOW_HIDE_LOADER = "SHOW_HIDE_LOADER";
    public void showHideProgressForLoder(boolean isForHide)
    {
        synchronized (SHOW_HIDE_LOADER)
        {
            if(isForHide)
            {
                if(AppsConstant.RunningLoaderCount>0)
                    AppsConstant.RunningLoaderCount = AppsConstant.RunningLoaderCount-1;
                if(AppsConstant.RunningLoaderCount==0)
                    hideLoader();
            }
            else
            {
                AppsConstant.RunningLoaderCount++;
                showLoader("");
            }

        }
    }
    public void hideLoader()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
    public void showLoader(String strMessage){
        runOnUiThread(new RunShowLoaderCustom());
    }
    class RunShowLoaderCustom implements Runnable
    {
        public RunShowLoaderCustom()
        {
        }
        @Override
        public void run()
        {
            try
            {
                if(dialog == null|| (dialog != null && !dialog.isShowing()))
                {
                    /*dialog = new Dialog(LoginActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.custom_loader);
                    dialog.setCancelable(false);
                    dialog.show();

                    ImageView ivOutsideImage;
                    ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
                    Animation rotateXaxis = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate_x_axis);
                    rotateXaxis.setInterpolator(new LinearInterpolator());
                    ivOutsideImage.setAnimation(rotateXaxis);*/

                    dialog = new Dialog(LoginActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.loader_animation);
                    dialog.setCancelable(false);
                    dialog.show();
                    AVLoadingIndicatorView avl = (AVLoadingIndicatorView) dialog.findViewById(R.id.avlView);
                    avl.show();
                }
            }
            catch(Exception e)
            {
                dialog = null;
                Logger.e("Log",e);
                Crashlytics.log(1,getClass().getName(),"Error in Parsing the response");
                Crashlytics.logException(e);
            }
        }
    }
    private Dialog dialog;
}