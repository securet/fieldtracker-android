package com.oppo.sfamanagement;

/**
 * Created by AllSmart-LT008 on 10/18/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Logger;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.database.Utils;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

public class LoginActivity extends Activity implements LoaderManager.LoaderCallbacks , View.OnClickListener {

    Button loginBtn;
    EditText emailET, passwordET;
//    ProgressDialog pd;
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
                }

                break;
        }
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        showHideProgressForLoder(false);
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
        showHideProgressForLoder(true);
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
                    dialog = new Dialog(LoginActivity.this, R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.custom_loader);
                    dialog.setCancelable(false);
                    dialog.show();
                    ImageView ivOutsideImage;
                    ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
                    Animation rotateXaxis = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate_x_axis);
                    rotateXaxis.setInterpolator(new LinearInterpolator());
                    ivOutsideImage.setAnimation(rotateXaxis);
                }
            }
            catch(Exception e)
            {
                dialog = null;
                Logger.e("Log",e);
            }
        }
    }
    private Dialog dialog;
}