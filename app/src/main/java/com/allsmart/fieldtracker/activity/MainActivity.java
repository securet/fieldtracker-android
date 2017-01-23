package com.allsmart.fieldtracker.activity;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.fragment.HistoryListFragment;
import com.allsmart.fieldtracker.fragment.MapFragment;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.service.GeolocationService;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.allsmart.fieldtracker.utils.StringUtils;
import com.crashlytics.android.Crashlytics;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.customviews.DigitalClockView;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.fragment.MoreFragment;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.fragment.LeaveStatusFragment;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;
import com.google.android.gms.location.LocationListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
	public static String TAG = "lstech.aos.debug";

	public static boolean geofencesAlreadyRegistered = false;
	public Preferences preferences;
	public final static int  MAP =0,LIST=1,MORE=2;
	public int openPage = MAP;

	LinearLayout llAttendance,llHistory,llMore,footerTabs;
	ImageView ivCurrentLocation,ivPhoto;
    public boolean isLoading = false;
	DigitalClockView dtcLoginTime;
	TextView tvSiteName,tvUserName,tvUserSerName,store;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		ivPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
		if (actionBar != null){
			actionBar.hide();
		}
		preferences = new Preferences(MainActivity.this);

		if(!preferences.getString(Preferences.USER_PHOTO,"").equals(null)) {
			Picasso.with(getApplicationContext()).load(UrlBuilder.getServerImage(preferences.getString(Preferences.USER_PHOTO,""))).placeholder(R.drawable.usericon).fit().into(ivPhoto);
		}

		Bundle b = new Bundle();
		b.putString(AppsConstant.URL, UrlBuilder.getStoreDetails(Services.STORE_DETAIL,preferences.getString(Preferences.PARTYID,"")));
		b.putString(AppsConstant.METHOD, AppsConstant.GET );
		b.putString(AppsConstant.PASSWORD, "");
		getLoaderManager().initLoader(LoaderConstant.USER_STORE_DETAIL,b,MainActivity.this).forceLoad();


		preferences.saveBoolean(Preferences.INLOCATION, false);
		preferences.commit();
		/*ivCurrentLocation= (ImageView) findViewById(R.id.ivCurrentLocation);*/
		tvSiteName = (TextView) findViewById(R.id.tvSiteName);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserSerName = (TextView) findViewById(R.id.tvUserSerName);
		dtcLoginTime = (DigitalClockView) findViewById(R.id.dtcLoginTime);
		footerTabs = (LinearLayout) findViewById(R.id.footerTabs);
        tvSiteName.setText(getResources().getString(R.string.off_site));

		tvUserName.setText(preferences.getString(Preferences.USERFIRSTNAME,""));
		tvUserSerName.setText(preferences.getString(Preferences.USERLASTNAME,""));
		llAttendance = (LinearLayout) findViewById(R.id.llAttendance);
		llAttendance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=MAP) {
					openPage=MAP;
					if (!isLoading) {
                        Fragment f = new MapFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                        UpadateButtonStatus();
                    }
				}
			}
		});
		llHistory = (LinearLayout) findViewById(R.id.llHistory);
		llHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=LIST) {
					openPage=LIST;
					if (!isLoading) {
						Fragment f = new HistoryListFragment();
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
						UpadateButtonStatus();
					}
				}
			}
		});
		llMore = (LinearLayout) findViewById(R.id.llMore);
		llMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=MORE) {
					openPage = MORE;
                    if (!isLoading) {
                        Fragment f = new MoreFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                        UpadateButtonStatus();
                    }
				}
			}
		});
		UpadateButtonStatus();
	}

	public void UpadateButtonStatus()
	{
		llAttendance.setAlpha(0.5f);
		llHistory.setAlpha(0.5f);
		llMore.setAlpha(0.5f);
		switch (openPage){
			case MORE:
				llMore.setAlpha(1.0f);
//				ivCurrentLocation.setVisibility(View.GONE);
				break;
			case MAP:
				llAttendance.setAlpha(1.0f);
//				ivCurrentLocation.setVisibility(View.VISIBLE);
				break;
			case LIST:
				llHistory.setAlpha(1.0f);
//				ivCurrentLocation.setVisibility(View.GONE);
				break;
		}
	}

	public void Logout()
	{
		if(NetworkUtils.isNetworkConnectionAvailable(getApplicationContext())) {
            preferences.saveBoolean(Preferences.ISLOGIN, false); // value to store

            preferences.remove(Preferences.SITE_ADDRESS);
            preferences.remove(Preferences.LATITUDE);
            preferences.remove(Preferences.SITE_ENTITY);
            preferences.remove(Preferences.SITENAME);
            preferences.remove(Preferences.PARTYID);

            preferences.commit();
            //	preferences.clearPreferences();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("No Internet!");
            builder.setMessage("Please Connect Internet to Log Off");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
	}

	public void onLeavesClick(View view) {
		Fragment fragment = new LeaveStatusFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
		fm.executePendingTransactions();
	}


	@Override
	public Loader onCreateLoader(int id, Bundle args)
	{
		showHideProgressForLoder(false);
		switch (id)
		{
			case LoaderConstant.USER_STORE_DETAIL:
				return new LoaderServices(MainActivity.this, LoaderMethod.USER_STORE_DETAIL, args);
			default:
				return null;
		}
	}

    public void changeSiteName(String text) {
        preferences.saveString(Preferences.USER_CUREENTSITE,text);
        preferences.commit();
        tvSiteName.setText(text);
    }
    public String getSiteName() {
        String siteName = "Off Site";
        if(!tvSiteName.getText().toString().equals(null) && !tvSiteName.getText().toString().equals(siteName)) {
            return tvSiteName.getText().toString();
        } else {
            return siteName;
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
			case LoaderConstant.USER_STORE_DETAIL:
				if(data!=null && data instanceof String && ((String)data).equalsIgnoreCase("Success"))
				{

				}else{
					Toast.makeText(getApplicationContext(),
							"Error in response. Please try again.",
							Toast.LENGTH_SHORT).show();
				}
                Intent intent = new Intent(MainActivity.this, GeolocationService.class);
                startService(intent);
                Fragment f = new MapFragment();
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
				fragmentManager.executePendingTransactions();

				break;
		}
        getLoaderManager().destroyLoader(loader.getId());
        footerTabs.setVisibility(View.VISIBLE);
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
		runOnUiThread(new MainActivity.RunShowLoaderCustom());
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
					dialog = new Dialog(MainActivity.this, R.style.Theme_Dialog_Translucent);
					dialog.setContentView(R.layout.custom_loader);
					dialog.setCancelable(false);
					dialog.show();
					ImageView ivOutsideImage;
					ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
					Animation rotateXaxis = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_x_axis);
					rotateXaxis.setInterpolator(new LinearInterpolator());
					ivOutsideImage.setAnimation(rotateXaxis);
				}
			}
			catch(Exception e)
			{
				dialog = null;
				Logger.e("Log",e);
				Crashlytics.log(1,getClass().getName(),"Error in MainActivity while displaying animation loader");
				Crashlytics.logException(e);
			}
		}
	}
	private Dialog dialog;

}
