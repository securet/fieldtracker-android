package com.oppo.sfamanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oppo.sfamanagement.database.API;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.database.MultipartUtility;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.database.ShiftTimeView;
import com.oppo.sfamanagement.database.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback<Status> {
	protected SupportMapFragment mapFragment;
	protected GoogleMap map;
	protected Marker myPositionMarker;
	private GoogleApiClient mGoogleApiClient;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
	private PendingIntent mPendingIntent;
//	private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				//Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG).show();
//				int resultCode = bundle.getInt("done");
//				if (resultCode == 1) {
//					Double latitude = bundle.getDouble("latitude");
//					Double longitude = bundle.getDouble("longitude");
//					updateMarker(latitude, longitude);
//				}
////				if(((MainActivity)getActivity()).myPrefs.getBoolean("inLocation", false)){
////					Toast.makeText(getActivity(),"Enter",Toast.LENGTH_LONG).show();
////				}else{
////					Toast.makeText(getActivity(),"Exit",Toast.LENGTH_LONG).show();
////				}
//			}
//		}
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}
	LinearLayout llLogin_Logout,llShiftTime;
	TextView tvTimeInOut,tvTimeInOutLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,false);
		mapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map_container, mapFragment);
		fragmentTransaction.commit();
		buildGoogleApiClient();

		((MainActivity) getActivity()).preferences.saveString("locationstatus", ""); // value to store
		((MainActivity) getActivity()).preferences.commit();
		tvTimeInOut = (TextView) rootView.findViewById(R.id.tvTimeInOut);
		tvTimeInOutLocation = (TextView) rootView.findViewById(R.id.tvTimeInOutLocation);
		ShiftTimeView stvShiftTime = (ShiftTimeView) rootView.findViewById(R.id.stvShiftTime);
		llShiftTime = (LinearLayout) rootView.findViewById(R.id.llShiftTime);
		llLogin_Logout = (LinearLayout) rootView.findViewById(R.id.llLogin_Logout);
		llLogin_Logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(((MainActivity)getActivity()).preferences.getBoolean("inLocation", false)){

						if(checkCameraPermission()){
							if(checkStoragePermission())
								OpenCamera();
							else
								askStoragePermission();
						}else{
							askCameraPermission();
						}


				}else{
					AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					dialog.setTitle("Not at store location");
					dialog.setMessage("Please go to store location and try again!");
					dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						}
					});

					dialog.show();
				}
			}
		});
		UpdateLoginLogOut();
		UpdateLocationStatus();
		return rootView;
	}

	public void moveToCurentLocation()
	{
		if(checkPermission()) {
			if (map != null) {
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
						LatLng(StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString("userlat",""))
						, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString("userlong",""))),18));
			}
		}
	}
	public void UpdateLocationStatus()
	{
		if(TextUtils.isEmpty(((MainActivity)getActivity()).preferences.getString("locationstatus", "")))
		{
			if (((MainActivity) getActivity()).preferences.getBoolean("inLocation", false) ) {
				tvTimeInOutLocation.setText("at " + ((MainActivity) getActivity()).preferences.getString("SiteName", ""));
				((MainActivity) getActivity()).preferences.saveString("locationstatus", "False"); // value to store
				((MainActivity) getActivity()).preferences.commit();
			} else {
				tvTimeInOutLocation.setText("(Not at location)");
				((MainActivity) getActivity()).preferences.saveString("locationstatus", "True"); // value to store
				((MainActivity) getActivity()).preferences.commit();
			}
		}else {
			if (((MainActivity) getActivity()).preferences.getBoolean("inLocation", false) && !((MainActivity) getActivity()).preferences.getString("locationstatus", "").equalsIgnoreCase("False")) {
				tvTimeInOutLocation.setText("at " + ((MainActivity) getActivity()).preferences.getString("SiteName", ""));
				((MainActivity) getActivity()).preferences.saveString("locationstatus", "False"); // value to store
				((MainActivity) getActivity()).preferences.commit();
			} else if (!((MainActivity) getActivity()).preferences.getBoolean("inLocation", false) && !((MainActivity) getActivity()).preferences.getString("locationstatus", "").equalsIgnoreCase("True")) {
				tvTimeInOutLocation.setText("(Not at location)");
				((MainActivity) getActivity()).preferences.saveString("locationstatus", "True"); // value to store
				((MainActivity) getActivity()).preferences.commit();
			}
		}


	}
	public void UpdateLoginLogOut()
	{
		if(TextUtils.isEmpty( ((MainActivity)getActivity()).preferences.getString("TokenCode", ""))) {
			llShiftTime.setVisibility(View.GONE);
			tvTimeInOut.setText("Time In");
		}else{
			llShiftTime.setVisibility(View.VISIBLE);
			tvTimeInOut.setText("Time Out");
		}
	}
	public void onResult(Status status) {
		if (status.isSuccess()) {
			//Toast.makeText(getApplicationContext(),getString(R.string.geofences_added), Toast.LENGTH_SHORT).show();
		} else {
			MainActivity.geofencesAlreadyRegistered = false;
			String errorMessage = getErrorString(getActivity(), status.getStatusCode());
			Toast.makeText(getActivity(), errorMessage,Toast.LENGTH_LONG).show();
		}
	}
	public static String getErrorString(Context context, int errorCode) {
		Resources mResources = context.getResources();
		switch (errorCode) {
			case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
				return mResources.getString(R.string.geofence_not_available);
			case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
				return mResources.getString(R.string.geofence_too_many_geofences);
			case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
				return mResources
						.getString(R.string.geofence_too_many_pending_intents);
			default:
				return mResources.getString(R.string.unknown_geofence_error);
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		Log.d(MainActivity.TAG,"new location : " + location.getLatitude() + ", "+ location.getLongitude() + ". "+ location.getAccuracy());
		broadcastLocationFound(location);

		if (!MainActivity.geofencesAlreadyRegistered) {
			registerGeofences();
		}
	}
	public void broadcastLocationFound(Location location) {

//		Double latitude = bundle.getDouble("latitude");
//		Double longitude = bundle.getDouble("longitude");
		updateMarker(location.getLatitude(), location.getLongitude());
//		Intent intent = new Intent("com.oppo.sfamanagement.geolocation.service");
//		intent.putExtra("latitude", location.getLatitude());
//		intent.putExtra("longitude", location.getLongitude());
//		intent.putExtra("done", 1);

//		getActivity().sendBroadcast(intent);
	}
	protected void registerGeofences() {
		if (MainActivity.geofencesAlreadyRegistered) {
			return;
		}

		Log.d(MainActivity.TAG, "Registering Geofences");

		HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore
				.getInstance(((MainActivity)getActivity()).preferences.getString(Preferences.SITENAME,""), StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LATITUDE,""))
						, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LONGITUDE,"")),StringUtils.getInt(((MainActivity)getActivity()).preferences.getString(Preferences.SITE_RADIUS,""))).getSimpleGeofences();

		GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
		for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
			SimpleGeofence sg = item.getValue();

			geofencingRequestBuilder.addGeofence(sg.toGeofence());
		}

		GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();

		mPendingIntent = requestPendingIntent();
		if(checkPermission()) {
			LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofencingRequest, mPendingIntent).setResultCallback(this);
		}

		MainActivity.geofencesAlreadyRegistered = true;
	}
	private PendingIntent requestPendingIntent() {

		if (null != mPendingIntent) {

			return mPendingIntent;
		} else {

			Intent intent = new Intent(getActivity(), GeofenceReceiver.class);
			return PendingIntent.getService(getActivity(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

		}
	}
	@Override
	public void onPause() {
		super.onPause();
//		if ( checkPermission())
//			getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapFragment != null) {
			mapFragment.getMapAsync(new OnMapReadyCallback() {

				@Override
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;
					map.animateCamera(CameraUpdateFactory.zoomTo(18));
					displayGeofences();
				}
			});
		}
//		if ( checkPermission())
//			getActivity().registerReceiver(receiver,new IntentFilter("com.oppo.sfamanagement.geolocation.service"));
	}
	// Create GoogleApiClient instance
	protected synchronized void buildGoogleApiClient() {
		Log.i(MainActivity.TAG, "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		createLocationRequest();
	}
	LocationRequest mLocationRequest;
	protected void createLocationRequest() {
		if(mLocationRequest==null) {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
			mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		}
	}
	@Override
	public void onStart() {
		super.onStart();

		// Call GoogleApiClient connection when starting the Activity
		mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
//		mGoogleApiClient.disconnect();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
//			stopLocationUpdates();
		}
	}
		// Disconnect GoogleApiClient when stopping Activity
		protected void stopLocationUpdates() {
			if ( checkPermission() ) {
				LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			}
		}
	private Location lastLocation;
	private void getLastKnownLocation() {
		if ( checkPermission() ) {
			lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if ( lastLocation != null ) {
				updateMarker(lastLocation.getLatitude(), lastLocation.getLongitude());
				map.animateCamera(CameraUpdateFactory.zoomTo(18));
				startLocationUpdates();
			} else {
				startLocationUpdates();
			}
//			getActivity().registerReceiver(receiver,new IntentFilter("com.oppo.sfamanagement.geolocation.service"));
		}
		else askPermission();
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(MainActivity.TAG, "Connected to GoogleApiClient");
		// Zoom in the Google Map
		map.animateCamera(CameraUpdateFactory.zoomTo(18));
		getLastKnownLocation();
	}
	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(MainActivity.TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(MainActivity.TAG,"Connection failed: ConnectionResult.getErrorCode() = "+ result.getErrorCode());
	}
//	protected LocationRequest mLocationRequest;
	protected void startLocationUpdates() {
		if ( checkPermission() ) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
	}
	private boolean checkPermission() {
		// Ask for permission if it wasn't granted yet
		return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED );
	}
	String strFile;
	private void OpenCamera(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = getOutputPhotoFile();
		strFile = file.getAbsolutePath();
		Uri fileUri = Uri.fromFile(file);

		//File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
		//fileUri =  Uri.fromFile(f);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
		} else {
			intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, REQ_CAMERA);
	}
	private final static int REQ_CAMERA =1003;
	private File getOutputPhotoFile()
	{
		File directory = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), getContext().getPackageName());
		if (!directory.exists())
		{
			if (!directory.mkdirs())
			{
				return null;
			}
		}

		return new File(directory.getPath() + File.separator + "IMG_"+ System.currentTimeMillis() + ".jpg");
	}
	private boolean checkCameraPermission() {
		// Ask for permission if it wasn't granted yet
		return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
				== PackageManager.PERMISSION_GRANTED );
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQ_CAMERA){
				Uri photoUri = null;
				if (data == null) {

				} else {
					photoUri = data.getData();
					strFile=photoUri.getPath();
				}
				File imageFile = new File(strFile);
				if (imageFile.exists()){
					UserLoginTask task = new UserLoginTask();
					task.execute(new String[] {
							((MainActivity)getActivity()).preferences.getString("UserName", ""),
							((MainActivity)getActivity()).preferences.getString("Password", "") });
				}
			}
		}
	}

	private boolean checkStoragePermission() {
		// Ask for permission if it wasn't granted yet
		return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
				== PackageManager.PERMISSION_GRANTED );
	}
	private void askStoragePermission() {
		this.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },REQ_Storage_PERMISSION);
	}
	private void askCameraPermission() {
		this.requestPermissions(new String[] { Manifest.permission.CAMERA },REQ_CAMERA_PERMISSION);
	}
//	LocationRequest locationRequest;
//	private final int UPDATE_INTERVAL =  1000;
//	private final int FASTEST_INTERVAL = 900;
//	private void startLocationUpdates(){
//		locationRequest = LocationRequest.create()
//				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//				.setInterval(UPDATE_INTERVAL)
//				.setFastestInterval(FASTEST_INTERVAL);
//
//		if ( checkPermission() )
//			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
//	}
	private final static int REQ_PERMISSION =1001;
	private final static int REQ_CAMERA_PERMISSION =1002;
	private final static int REQ_Storage_PERMISSION =1000;

	// Asks for permission
	private void askPermission() {
		this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },REQ_PERMISSION);
	}

	// Verify user's response of the permission requested
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch ( requestCode ) {
			case REQ_PERMISSION: {
				if ( grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED ){
					// Permission granted
					getLastKnownLocation();

				} else {
					// Permission denied
					permissionsDenied();
				}
				break;
			}
			case REQ_CAMERA_PERMISSION: {
				if ( grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED ){
					// Permission granted
					if(checkStoragePermission())
						OpenCamera();
					else
						askStoragePermission();

				} else {
					// Permission denied
					permissionsDenied();
				}
				break;
			}
			case REQ_Storage_PERMISSION: {
				if ( grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED ){
					// Permission granted
						OpenCamera();

				} else {
					// Permission denied
					permissionsDenied();
				}
				break;
			}
		}

	}

	// App cannot work without the permissions
	private void permissionsDenied() {
//		Log.w(TAG, "permissionsDenied()");
	}
	protected void displayGeofences()
	{
		HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance(((MainActivity)getActivity()).preferences.getString(Preferences.SITENAME,""), StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LATITUDE,""))
				, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LONGITUDE,"")),StringUtils.getInt(((MainActivity)getActivity()).preferences.getString(Preferences.SITE_RADIUS,""))).getSimpleGeofences();
		for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
			SimpleGeofence sg = item.getValue();
			CircleOptions circleOptions = new CircleOptions()
					.center(new LatLng(sg.getLatitude(), sg.getLongitude()))
					.strokeColor(Color.argb(50, 0, 127, 255))
					.fillColor( Color.argb(100, 137, 207, 240) )
					.radius( sg.getRadius() );
			 map.addCircle( circleOptions );
		}
	}

	protected void createMarker(Double latitude, Double longitude) {
		LatLng latLng = new LatLng(latitude, longitude);

		myPositionMarker = map.addMarker((new MarkerOptions().position(latLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	protected void updateMarker(Double latitude, Double longitude) {
		if (myPositionMarker == null) {
			createMarker(latitude, longitude);
		}
		((MainActivity)getActivity()).preferences.saveString("userlat", latitude+""); // value to store
		((MainActivity)getActivity()).preferences.commit();
		((MainActivity)getActivity()).preferences.saveString("userlong", longitude+""); // value to store
		((MainActivity)getActivity()).preferences.commit();
		UpdateLocationStatus();
		LatLng latLng = new LatLng(latitude, longitude);
		myPositionMarker.setPosition(latLng);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}



	private class UserLoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			((MainActivity)getActivity()).pd.show();
		};

		@Override
		protected String doInBackground(String... params) {
			String response = "";
			try {
				MultipartUtility multipart ;

				// In your case you are not adding form data so ignore this
                /*This is to add parameter values */

				if(TextUtils.isEmpty( ((MainActivity)getActivity()).preferences.getString("TokenCode", ""))){
					// Login
					multipart= new MultipartUtility(API.GetLoginRest(params[0], params[1]), "UTF-8");
					multipart.addFormField("site.siteId",((MainActivity) getActivity()).preferences.getString("siteId","21364"));
					multipart.addFormField("serviceType.serviceTypeId","16");
					multipart.addFormField("description","Time In");
					multipart.addFormField("issueType.issueTypeId","29");
					multipart.addFormField("latitude",((MainActivity)getActivity()).preferences.getString("userlat",""));
					multipart.addFormField("longitude",((MainActivity)getActivity()).preferences.getString("userlong",""));
					multipart.addFormField("severity.enumerationId","MAJOR");
				}else{
					// logout
					multipart= new MultipartUtility(API.GetLogOutRest(params[0], params[1]), "UTF-8");
					multipart.addFormField("ticketId",((MainActivity)getActivity()).preferences.getString("TokenCode",""));
					multipart.addFormField("status.enumerationId","CLOSED");
					multipart.addFormField("description","Time Out");
				}



                /*This is to add file content*/
//				for (int i = 0; i < myFileArray.size(); i++) {
					multipart.addFilePart("ticketAttachments",new File(strFile));
//				}

				for (String line : multipart.finish()) {
					response = line;
				}
			} catch (Exception e) {
				e.printStackTrace();
				response = "";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			((MainActivity)getActivity()).pd.dismiss();
			if(API.DEBUG)
				System.out.println("The Message Is: " + result);
			if (!(result.equals("No Internet")) || !(result.equals(""))) {
				try {
					String strDate = "";
					if(result.toString().contains("status") && (new JSONObject(result).getString("status").toString().equals("success")))
					{
						String ticketId = "";
						String transitionName = "Time Out";
						if(TextUtils.isEmpty( ((MainActivity)getActivity()).preferences.getString("TokenCode", ""))) {
							// Login
							JSONObject data = new JSONObject(result);
							JSONObject obj = data.getJSONObject("data");
							ticketId = obj.getString("ticketId");
							transitionName = "Time In";
							strDate = DateFormat.format("dd/M/yyyy HH:mm:ss",new Date()).toString();
						}
						((MainActivity) getActivity()).preferences.saveString("TokenCode", ticketId); // value to store
						((MainActivity) getActivity()).preferences.commit();
						((MainActivity) getActivity()).preferences.saveString("LoginDate", strDate); // value to store
						((MainActivity) getActivity()).preferences.commit();
						String date = DateFormat.format("yyyy-MM-dd HH:mm:ss",new Date()).toString();
						EventDataSource eds = new EventDataSource(getActivity());
						eds.create(transitionName, date,  ((MainActivity)getActivity()).preferences.getString("SiteName", ""));
						eds.close();
					}else{
						Toast.makeText(getActivity(), ""+new JSONObject(result).getString("messages").toString(), Toast.LENGTH_SHORT).show();
					}



				} catch (Exception e) {
					if(API.DEBUG){
						e.printStackTrace();
					}

					Toast.makeText(getActivity(),
							"Error in response. Please try again.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(),
						"Error in response. Please try again.",
						Toast.LENGTH_SHORT).show();
			}
			UpdateLoginLogOut();
		}

	}


	public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) throws IOException {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		String result = "";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			Iterator<String> keys = parmas.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = parmas.get(key);

				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(value);
				outputStream.writeBytes(lineEnd);
			}

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


			if (200 != connection.getResponseCode()) {
				throw new IOException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
			}

			inputStream = connection.getInputStream();

			result = this.convertStreamToString(inputStream);

			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();

			return result;
		} catch (Exception e) {
			throw new IOException(e.toString());
		}

	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
