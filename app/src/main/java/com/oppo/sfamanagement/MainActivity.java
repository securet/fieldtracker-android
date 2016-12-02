package com.oppo.sfamanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.oppo.sfamanagement.database.DigitalClockView;
import com.oppo.sfamanagement.database.MoreFragment;
import com.oppo.sfamanagement.fragment.LeaveStatusFragment;
import com.oppo.sfamanagement.fragment.PromotersFragment;
import com.oppo.sfamanagement.fragment.StoreListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
	public static String TAG = "lstech.aos.debug";

	static public boolean geofencesAlreadyRegistered = false;
	public SharedPreferences.Editor prefsEditor;
	public SharedPreferences myPrefs;
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
		myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
		pd = new ProgressDialog(MainActivity.this);
		pd.setMessage("Please wait...");
		pd.setCancelable(false);
		UserSiteTask task = new UserSiteTask();
		task.execute(new String[] {
				myPrefs.getString("UserName", ""),
				myPrefs.getString("Password", "") });
		prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean("inLocation", false);
		prefsEditor.commit();
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
		String strName = myPrefs.getString("EmployeeName","");
		if(!TextUtils.isEmpty(strName)&&strName.contains(" "))
		{
			String[] strNames = strName.split(" ");
			tvUserName.setText(strNames[0]);
			tvUserSerName.setText(strNames[1]);
		}else if(!TextUtils.isEmpty(strName)) {
			tvUserName.setText(strName);
			tvUserSerName.setText("");
		}
//		startService(new Intent(this, GeolocationService.class));
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
	private class UserSiteTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String response = "";
			try {
				response = API.GetSitesRest(params[0], params[1]);
			} catch (Exception e) {
				e.printStackTrace();
				response = "";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			if(API.DEBUG)
				System.out.println("The Message Is: " + result);
			if (!(result.equals("No Internet")) || !(result.equals(""))) {
				try {

					if(result.toString().contains("status") && (new JSONObject(result).getString("status").toString().equals("success")))
					{
						JSONObject data = new JSONObject(result);

						JSONArray objArray = data.getJSONArray("data");
						for(int i=0;i<objArray.length();i++)
						{
							JSONObject obj = objArray.getJSONObject(i);
							String siteId = obj.getString("siteId");
							String name = obj.getString("name");
							String latitude = obj.getString("latitude");
							String longitude = obj.getString("longitude");

							prefsEditor = myPrefs.edit();
							prefsEditor.putString("siteId", siteId); // value to store
							prefsEditor.commit();

							prefsEditor = myPrefs.edit();
							prefsEditor.putString("SiteName", name); // value to store
							prefsEditor.commit();
							//17.464755, 78.481333
							prefsEditor = myPrefs.edit();
//							prefsEditor.putString("latitude", "17.464755");
							prefsEditor.putString("latitude", latitude); // value to store
							prefsEditor.commit();

							prefsEditor = myPrefs.edit();
//							prefsEditor.putString("longitude", "78.481333");
							prefsEditor.putString("longitude", longitude); // value to store
							prefsEditor.commit();
						}
					}else{
						Toast.makeText(getApplicationContext(), ""+new JSONObject(result).getString("messages").toString(), Toast.LENGTH_SHORT).show();
					}



				} catch (Exception e) {
					if(API.DEBUG){
						e.printStackTrace();
					}

					Toast.makeText(getApplicationContext(),
							"Error in response. Please try again.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Error in response. Please try again.",
						Toast.LENGTH_SHORT).show();
			}
			tvSiteName.setText(myPrefs.getString("SiteName",""));
			Fragment f = new MapFragment();
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.flMiddle, f).commit();
			fragmentManager.executePendingTransactions();
		}

	}
	public void Logout()
	{


		/*LogoutTask task = new LogoutTask();
		task.execute(new String[]{myPrefs.getString("EmployeeID", "0"),myPrefs.getString("EmployeeToken", "Token")});*/

		prefsEditor = myPrefs.edit();
		prefsEditor.putBoolean("login", false); // value to store
		prefsEditor.commit();

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
}
