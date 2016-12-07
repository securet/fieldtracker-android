package com.oppo.sfamanagement;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oppo.sfamanagement.database.API;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.DigitalClockView;
import com.oppo.sfamanagement.database.MoreFragment;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.LeaveStatusFragment;
import com.oppo.sfamanagement.fragment.PromotersFragment;
import com.oppo.sfamanagement.fragment.RetakeFragment;
import com.oppo.sfamanagement.fragment.StoreListFragment;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
	public static String TAG = "lstech.aos.debug";

	static public boolean geofencesAlreadyRegistered = false;
	public Preferences preferences;
	ProgressDialog pd;
	public final static int  MAP =0,LIST=1,MORE=2;
	public int openPage = MAP;
	LinearLayout llAttendance,llHistory,llMore;
	ImageView ivCurrentLocation;
	DigitalClockView dtcLoginTime;
	TextView tvSiteName,tvUserName,tvUserSerName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.hide();
		}
		preferences = new Preferences(MainActivity.this);
		pd = new ProgressDialog(MainActivity.this);
		pd.setMessage("Please wait...");
		pd.setCancelable(false);

		Bundle b = new Bundle();
		b.putString(AppsConstant.URL, UrlBuilder.getStoreDetails(Services.STORE_DETAIL,preferences.getString(Preferences.PARTYID,"")));
		b.putString(AppsConstant.METHOD, AppsConstant.GET );
		b.putString(AppsConstant.PASSWORD, "");
		getLoaderManager().initLoader(LoaderConstant.USER_STORE_DETAIL,b,MainActivity.this).forceLoad();
//		UserSiteTask task = new UserSiteTask();
//		task.execute(new String[] { preferences.getString(Preferences.USERNAME, ""),preferences.getString("USERPASSWORD", "") });

		preferences.saveBoolean(Preferences.INLOCATION, false);
		preferences.commit();
		ivCurrentLocation= (ImageView) findViewById(R.id.ivCurrentLocation);
		tvSiteName = (TextView) findViewById(R.id.tvSiteName);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserSerName = (TextView) findViewById(R.id.tvUserSerName);
		dtcLoginTime = (DigitalClockView) findViewById(R.id.dtcLoginTime);
		ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				boolean gps_enabled = false;
				boolean network_enabled = false;

				try {
					gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
				} catch(Exception ex) {}

				try {
					network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				} catch(Exception ex) {}

				if(!gps_enabled && !network_enabled) {
					// notify user
					AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
					dialog.setMessage("Location is not enabled !");
					dialog.setPositiveButton("Open setting", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO Auto-generated method stub
							Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(myIntent);
							//get gps
						}
					});
					dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO Auto-generated method stub

						}
					});
					dialog.show();
				}else {
					Fragment f = getSupportFragmentManager().findFragmentById(R.id.flMiddle);
					if (f instanceof MapFragment) {
						((MapFragment) f).moveToCurentLocation();
					}
				}
			}
		});
		tvUserName.setText(preferences.getString(Preferences.USERFIRSTNAME,""));
		tvUserSerName.setText(preferences.getString(Preferences.USERLASTNAME,""));
		llAttendance = (LinearLayout) findViewById(R.id.llAttendance);
		llAttendance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=MAP) {
					openPage=MAP;
					Fragment f = new MapFragment();
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
					UpadateButtonStatus();
				}
			}
		});
		llHistory = (LinearLayout) findViewById(R.id.llHistory);
		llHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=LIST) {
					openPage=LIST;
					Fragment f = new EventsFragment();
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
					UpadateButtonStatus();
				}
			}
		});
		llMore = (LinearLayout) findViewById(R.id.llMore);
		llMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(openPage!=MORE) {
					openPage = MORE;
					Fragment f = new MoreFragment();
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
					UpadateButtonStatus();
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
				ivCurrentLocation.setVisibility(View.GONE);
				break;
			case MAP:
				llAttendance.setAlpha(1.0f);
				ivCurrentLocation.setVisibility(View.VISIBLE);
				break;
			case LIST:
				llHistory.setAlpha(1.0f);
				ivCurrentLocation.setVisibility(View.GONE);
				break;
		}
	}

//
//	private class UserSiteTask extends AsyncTask<String, Void, String> {
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			pd.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			String response = "";
//			try {
//				response = API.GetSitesRest(params[0], params[1]);
//			} catch (Exception e) {
//				e.printStackTrace();
//				response = "";
//			}
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			pd.dismiss();
//			if(API.DEBUG)
//				System.out.println("The Message Is: " + result);
//			if (!(result.equals("No Internet")) || !(result.equals(""))) {
//				try {
//
//					if(result.toString().contains("status") && (new JSONObject(result).getString("status").toString().equals("success")))
//					{
//						JSONObject data = new JSONObject(result);
//
//						JSONArray objArray = data.getJSONArray("data");
//						for(int i=0;i<objArray.length();i++)
//						{
//							JSONObject obj = objArray.getJSONObject(i);
//							String siteId = obj.getString("siteId");
//							String name = obj.getString("name");
//							String latitude = obj.getString("latitude");
//							String longitude = obj.getString("longitude");
//
//							preferences.saveString(Preferences.SITEID, siteId); // value to store
//							preferences.commit();
//							preferences.saveString(Preferences.SITENAME, name); // value to store
//							preferences.commit();
//							//17.464755, 78.481333
//							preferences.saveString(Preferences.LATITUDE, latitude); // value to store
//							preferences.commit();
//							preferences.saveString(Preferences.LONGITUDE, longitude); // value to store
//							preferences.commit();
//						}
//					}else{
//						Toast.makeText(getApplicationContext(), ""+new JSONObject(result).getString("messages").toString(), Toast.LENGTH_SHORT).show();
//					}
//
//
//
//				} catch (Exception e) {
//					if(API.DEBUG){
//						e.printStackTrace();
//					}
//
//					Toast.makeText(getApplicationContext(),
//							"Error in response. Please try again.",
//							Toast.LENGTH_SHORT).show();
//				}
//			} else {
//				Toast.makeText(getApplicationContext(),
//						"Error in response. Please try again.",
//						Toast.LENGTH_SHORT).show();
//			}
//			tvSiteName.setText(preferences.getString(Preferences.SITENAME,""));
//			Fragment f = new MapFragment();
//			FragmentManager fragmentManager = getSupportFragmentManager();
//			fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
//			fragmentManager.executePendingTransactions();
//		}
//
//	}
	public void Logout()
	{
		preferences.saveBoolean(Preferences.ISLOGIN, false); // value to store
		preferences.commit();
		finish();
		startActivity(new Intent(MainActivity.this, LoginActivity.class));
	}

	public void onStoreClick(View view) {
		Fragment fragment =(Fragment) new StoreListFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
		fm.executePendingTransactions();

	}

	public void onPromotersClick(View view) {
		Fragment fragment =(Fragment) new PromotersFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
		fm.executePendingTransactions();
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
		pd.show();
		switch (id)
		{
			case LoaderConstant.USER_STORE_DETAIL:
				return new LoaderServices(MainActivity.this, LoaderMethod.USER_STORE_DETAIL, args);
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
			case LoaderConstant.USER_STORE_DETAIL:
				if(data!=null && data instanceof String && ((String)data).equalsIgnoreCase("Success"))
				{

				}else{
					Toast.makeText(getApplicationContext(),
							"Error in response. Please try again.",
							Toast.LENGTH_SHORT).show();
				}
				tvSiteName.setText(preferences.getString(Preferences.SITENAME,""));
				Fragment f = new MapFragment();
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
				fragmentManager.executePendingTransactions();
				break;
		}
		getLoaderManager().destroyLoader(loader.getId());
	}

}
