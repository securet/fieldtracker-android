package com.oppo.fieldtracker.activity;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oppo.fieldtracker.fragment.HistoryListFragment;
import com.oppo.fieldtracker.fragment.MapFragment;
import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.utils.NetworkUtils;
import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.customviews.DigitalClockView;
import com.oppo.fieldtracker.utils.Logger;
import com.oppo.fieldtracker.fragment.MoreFragment;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.constants.LoaderConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.service.LoaderServices;
import com.oppo.fieldtracker.constants.Services;
import com.oppo.fieldtracker.utils.UrlBuilder;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{
	public static String TAG = "lstech.aos.debug";
	public static boolean geofencesAlreadyRegistered = false;
	public Preferences preferences;
	public final static int  MAP =1,LIST=2,MORE=3;
	public int openPage = MAP;


	LinearLayout llAttendance,llHistory,llMore,footerTabs;
	ImageView ivCurrentLocation,ivPhoto;
    public boolean isLoading = false;
	DigitalClockView dtcLoginTime;
	TextView tvSiteName,tvUserName,tvUserSerName;


	@Override
	protected void onResume() {
		if(preferences == null) {
			preferences = new Preferences(getApplicationContext());
		}
		super.onResume();
        checkForNewVersion();
	}

	@Override
	public void onBackPressed() {
		if(!isLoading) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		ivPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
		if (actionBar != null){
			actionBar.hide();
		}
		preferences = new Preferences(getApplicationContext());

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
		registerReceiver(changeUI, new IntentFilter(AppsConstant.LOCATION_UPDATE));

		tvUserName.setText(preferences.getString(Preferences.USERFIRSTNAME,""));
		tvUserSerName.setText(preferences.getString(Preferences.USERLASTNAME,""));
		llAttendance = (LinearLayout) findViewById(R.id.llAttendance);
		llAttendance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*if(openPage!=MAP) {
					openPage=MAP;*/
					if (!isLoading) {
                       // /*if(!isManager()) {
                            Fragment f = new MapFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                       /* } else {
                            Fragment f = new ManagerAttendence();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                        }*/
            //            UpadateButtonStatus();
                    }
			//	}
			}
		});
		llHistory = (LinearLayout) findViewById(R.id.llHistory);
		llHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*if(openPage!=LIST) {
					openPage=LIST;*/
					if (!isLoading) {
						Fragment f = new HistoryListFragment();
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
			//			UpadateButtonStatus();
					}
			//	}
			}
		});
		llMore = (LinearLayout) findViewById(R.id.llMore);
		llMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				/*if(openPage!=MORE) {
					openPage = MORE;*/
                    if (!isLoading) {
                        Fragment f = new MoreFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
						if(!(fragmentManager.findFragmentById(R.id.flMiddle) instanceof MoreFragment)) {
							fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
						} else {
							fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
						}


                      //  UpadateButtonStatus();
                    }
			//	}
			}
		});
		//UpadateButtonStatus();
	}

	@Override
	protected void onStart() {
		super.onStart();
//		checkForNewVersion();
	}

	private void checkForNewVersion() {
		Bundle bundle = new Bundle();
		bundle.putString(AppsConstant.METHOD,AppsConstant.GET);
		bundle.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.FORCED_UPDATE));
		getLoaderManager().initLoader(LoaderConstant.FORCED_UPDATE,bundle,MainActivity.this).forceLoad();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(changeUI);
	}

	/*public void UpadateButtonStatus()
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
	}*/

	public void Logout() {
		if(NetworkUtils.isNetworkConnectionAvailable(getApplicationContext())) {
            preferences.saveBoolean(Preferences.ISLOGIN, false); // value to store

            preferences.remove(Preferences.SITE_ADDRESS);
            preferences.remove(Preferences.LATITUDE);
   //         preferences.remove(Preferences.SITE_ENTITY);
            preferences.remove(Preferences.SITENAME);
            preferences.remove(Preferences.PARTYID);
			preferences.remove(Preferences.USER_CUREENTSITE);
			preferences.remove(Preferences.ROLETYPEID);

			preferences.remove(Preferences.ISONPREMISE);

			preferences.remove(Preferences.REPORTEE_MANAGER_NAME);
			preferences.remove(Preferences.REPORTEE_MANAGER_EMAIL);
			preferences.remove(Preferences.REPORTEE_MANAGER_PHONE);

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

    public boolean isManager() {
        if (preferences == null) {
            preferences = new Preferences(getApplicationContext());
        }
		if(preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FE")) {
			return false;
		} else {
			return true;
		}
    }

	@Override
	public Loader onCreateLoader(int id, Bundle args)
	{
		showHideProgressForLoder(false);
		switch (id)
		{
			case LoaderConstant.USER_STORE_DETAIL:
				return new LoaderServices(MainActivity.this, LoaderMethod.USER_STORE_DETAIL, args);
			case LoaderConstant.FORCED_UPDATE:
				return new LoaderServices(MainActivity.this,LoaderMethod.FORCED_UPDATE,args);
			default:
				return null;
		}
	}

	private void showUpdateDialog() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.forced_update_screen);
		dialog.setCancelable(false);
		Button button = (Button) dialog.findViewById(R.id.forcedUpdate);
        TextView textView = (TextView) dialog.findViewById(R.id.tvForcedUpdate);
        textView.setText(preferences.getString(Preferences.FORCEUPDATE_MESSAGE,"It is always better to have 'New'"));
      //  textView.setText("New " + preferences.getString(Preferences.APPVERSION,"") + " version is availabel in playstore");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String appUrl = "https://play.google.com/store/apps/details?id=com.Clairvoyant.FernsAndPetals&hl=en";
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl)));
					dialog.dismiss();
                    preferences.remove(Preferences.FORCEUPDATE_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
					Logger.e("Log", e);
					Crashlytics.log(1, getClass().getName(), "Error at Forced Update");
					Crashlytics.logException(e);
				}
			}
		});
		if(!isFinishing()) {
			dialog.show();
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
				if(data!=null && data instanceof String) {
					String result = (String) data;
					if(result.equals("success")) {

					} else if(!result.equals("success") && !result.equals("error")) {
						displayMessage(result);
					} else {
						displayMessage("Error in getting store detail.");
					}

				}else{
					Toast.makeText(getApplicationContext(),
							"Error in response. Please try again.",
							Toast.LENGTH_SHORT).show();
				}
                /*Intent intent = new Intent(MainActivity.this, GeolocationService.class);
                startService(intent);*/
                /*if(isManager()) {
                    Fragment f = new ManagerAttendence();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                    fragmentManager.executePendingTransactions();
                } else {*/
                    Fragment f = new MapFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
                    fragmentManager.executePendingTransactions();
                //}


				break;

			case LoaderConstant.FORCED_UPDATE:
				if(data != null && data instanceof String) {
					String result = (String) data;
					if(result.equals("success")) {
						if(preferences.getBoolean(Preferences.FORCEUPDATE,false)) {
							PackageManager manager = getApplicationContext().getPackageManager();
							try {
								PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(),0);
								String versionName = info.versionName;
								if(versionName.equals(preferences.getString(Preferences.APPVERSION,""))) {
									Log.d(MainActivity.TAG,"App has latest Version");
								} else {
									showUpdateDialog();
								}
								System.out.println("Version code   " + versionName);
							} catch (PackageManager.NameNotFoundException e){
								e.printStackTrace();
							}
						}
					} else if(!result.equals("success") && !result.equals("error")) {
						displayMessage(result);
					} else {
						Log.d(MainActivity.TAG,"Version check failed");
					}
				} else {
					displayMessage("");
				}
				break;
		}
        getLoaderManager().destroyLoader(loader.getId());
        footerTabs.setVisibility(View.VISIBLE);
	}
	String SHOW_HIDE_LOADER = "SHOW_HIDE_LOADER";
	/*public void showHideProgressForLoder(boolean isForHide)
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
	private Dialog dialog;*/
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

	private BroadcastReceiver changeUI = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String siteName = intent.getStringExtra(Preferences.SITENAME);
			if(!TextUtils.isEmpty(siteName)) {
				tvSiteName.setText(siteName);
				preferences.saveString(Preferences.USER_CUREENTSITE,siteName);
			} else {
				tvSiteName.setText(getString(R.string.off_site));
				preferences.saveString(Preferences.USER_CUREENTSITE,getString(R.string.off_site));
			}
			preferences.commit();
		}
	};



	public void displayMessage(String message) {
		Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
				Crashlytics.log(1,getClass().getName(),"Error in MainActivity while displaying animation loader");
				Crashlytics.logException(e);
			}
		}
	}
	private Dialog dialog;


}
