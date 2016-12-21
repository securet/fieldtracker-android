package com.oppo.sfamanagement;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofenceStatusCodes;
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
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.CalenderUtils;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.database.ShiftTimeView;
import com.oppo.sfamanagement.database.StringUtils;
import com.oppo.sfamanagement.model.Response;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback<Status>, LoaderManager.LoaderCallbacks {
	protected SupportMapFragment mapFragment;
	protected GoogleMap map;
	protected Marker myPositionMarker;
	private GoogleApiClient mGoogleApiClient;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
	private PendingIntent mPendingIntent;

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

		((MainActivity) getActivity()).preferences.saveString(Preferences.LOCATIONSTATUS, ""); // value to store
		((MainActivity) getActivity()).preferences.saveBoolean(Preferences.INLOCATION, isUserInLoacation());
		((MainActivity) getActivity()).preferences.commit();
		tvTimeInOut = (TextView) rootView.findViewById(R.id.tvTimeInOut);
		tvTimeInOutLocation = (TextView) rootView.findViewById(R.id.tvTimeInOutLocation);
		ShiftTimeView stvShiftTime = (ShiftTimeView) rootView.findViewById(R.id.stvShiftTime);
		llShiftTime = (LinearLayout) rootView.findViewById(R.id.llShiftTime);
		llLogin_Logout = (LinearLayout) rootView.findViewById(R.id.llLogin_Logout);
		llLogin_Logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(((MainActivity)getActivity()).preferences.getBoolean(Preferences.INLOCATION, false)){

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

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		((MainActivity)getActivity()).showHideProgressForLoder(false);
		switch (id) {
			case LoaderConstant.TIMEINOUT:
				return new LoaderServices(getContext(), LoaderMethod.TIMEINOUT,args);
			default:
				return null;
		}
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1))
				* Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1))
				* Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	private boolean isUserInLoacation(){
		double lat1 = StringUtils.getDouble(((MainActivity) getActivity()).preferences.getString(Preferences.USERLATITUDE,""));
		double lon1 = StringUtils.getDouble(((MainActivity) getActivity()).preferences.getString(Preferences.USERLONGITUDE,""));
		double lat2 = StringUtils.getDouble(((MainActivity) getActivity()).preferences.getString(Preferences.LATITUDE,""));
		double lon2 = StringUtils.getDouble(((MainActivity) getActivity()).preferences.getString(Preferences.LONGITUDE,""));
		Double distance = distance(lat1, lon1, lat2, lon2);
		int DistanceInRadius ;
		DistanceInRadius  = (int)deg2rad(distance)*1000;
		return DistanceInRadius<=StringUtils.getInt(((MainActivity) getActivity()).preferences.getString(Preferences.SITE_RADIUS,""));

	}
	@Override
	public void onLoadFinished(Loader loader, Object data) {
		((MainActivity) getActivity()).showHideProgressForLoder(true);
		Response response = null;
		switch (loader.getId()) {
			case LoaderConstant.TIMEINOUT:
				if (data != null && data instanceof Response) {
					String strDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
					response= (Response) data;
					if (((Response) data).getResponce().equalsIgnoreCase("Time In")) {
						((MainActivity)getActivity()).preferences.saveString(Preferences.TIMEINOUTSTATUS,strDate+"TimeIn");
					}else if (((Response) data).getResponce().equalsIgnoreCase("Time Out")) {
						((MainActivity)getActivity()).preferences.saveString(Preferences.TIMEINOUTSTATUS,strDate+"TimeOut");
					}
					((MainActivity)getActivity()).preferences.commit();
				}
				UpdateLoginLogOut();
				Toast.makeText(getActivity(),response.getResponceMessage(),Toast.LENGTH_LONG).show();
				break;
			}

			getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader loader) {

	}
	public void moveToCurentLocation()
	{
		if(checkPermission()) {
			if (map != null) {
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
						LatLng(StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.USERLATITUDE,""))
						, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.USERLONGITUDE,""))),18));
			}
		}
	}
	public void UpdateLocationStatus()
	{
		if(TextUtils.isEmpty(((MainActivity)getActivity()).preferences.getString(Preferences.LOCATIONSTATUS, "")))
		{
			if (((MainActivity) getActivity()).preferences.getBoolean(Preferences.INLOCATION, false) ) {
				tvTimeInOutLocation.setText("at " + ((MainActivity) getActivity()).preferences.getString(Preferences.SITENAME, ""));
				((MainActivity) getActivity()).preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
				((MainActivity) getActivity()).preferences.commit();
				UpdateTimeInOut("");
			} else {
				tvTimeInOutLocation.setText("(Not at location)");
				((MainActivity) getActivity()).preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
				((MainActivity) getActivity()).preferences.commit();
				UpdateTimeInOut("");
			}
		}else if (((MainActivity) getActivity()).preferences.getBoolean(Preferences.INLOCATION, false) && !((MainActivity) getActivity()).preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("False")) {
			tvTimeInOutLocation.setText("at " + ((MainActivity) getActivity()).preferences.getString(Preferences.SITENAME, ""));
			((MainActivity) getActivity()).preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
			((MainActivity) getActivity()).preferences.commit();
			UpdateTimeInOut("");
		} else if (!((MainActivity) getActivity()).preferences.getBoolean(Preferences.INLOCATION, false)
				&& !((MainActivity) getActivity()).preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("True")) {
			tvTimeInOutLocation.setText("(Not at location)");
			((MainActivity) getActivity()).preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
			((MainActivity) getActivity()).preferences.commit();
			UpdateTimeInOut("");
		}
	}
	public void UpdateLoginLogOut()
	{
		String strDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
		if(((MainActivity)getActivity()).preferences.getString(Preferences.TIMEINOUTSTATUS,strDate+"Pending").equalsIgnoreCase(strDate+"TimeIn")) {
			llShiftTime.setVisibility(View.VISIBLE);
			tvTimeInOut.setText("Time Out");
			llLogin_Logout.setVisibility(View.VISIBLE);
//		}else if(((MainActivity)getActivity()).preferences.getString(Preferences.TIMEINOUTSTATUS,strDate+"Pending").equalsIgnoreCase(strDate+"TimeOut")) {
//			llShiftTime.setVisibility(View.GONE);
//			llLogin_Logout.setVisibility(View.GONE);
		}else{
			llShiftTime.setVisibility(View.GONE);
			tvTimeInOut.setText("Time In");
			llLogin_Logout.setVisibility(View.VISIBLE);
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
						, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LONGITUDE,"")),StringUtils.getInt(((MainActivity)getActivity()).preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS))).getSimpleGeofences();

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
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
			return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED);
		} else {
			return true;
		}
	}
	private void OpenCamera(){
		Intent i = new Intent(getActivity(), CameraActivity.class);
		i.putExtra("camera_key",AppsConstant.FRONT_CAMREA_OPEN);
		i.putExtra("purpose","For Photo");
		startActivityForResult(i,REQ_CAMERA);
	}
	private final static int REQ_CAMERA =1003;
	private boolean checkCameraPermission() {
		// Ask for permission if it wasn't granted yet
		return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
				== PackageManager.PERMISSION_GRANTED );
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQ_CAMERA)
			{
				String strFile = data.getStringExtra("response");
				if(strFile.length() != 0) {
					Toast.makeText(getContext(),strFile,Toast.LENGTH_SHORT).show();
					Log.d("path",strFile);
					UpdateTimeInOut(strFile);
				}
			}
		}
	}

	private void UpdateTimeInOut(String strFile){
		String strDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
		String strActionType = "";
		String strComments = "";
		if(((MainActivity)getActivity()).preferences.getString(Preferences.TIMEINOUTSTATUS,strDate+"Pending").equalsIgnoreCase(strDate+"TimeIn")) {
			if(!TextUtils.isEmpty(strFile)) {
				strActionType = "clockOut";
				strComments = "Time Out";
			}else{
				if (((MainActivity) getActivity()).preferences.getBoolean(Preferences.INLOCATION, false) ) {
					strActionType = "clockIn";
					strComments = "In Location";
				}else{
					strActionType = "clockOut";
					strComments = "Out Location";
				}
			}
		}else if(!TextUtils.isEmpty(strFile)){
			strActionType = "clockIn";
			strComments = "Time In";
		}
		if(!TextUtils.isEmpty(strActionType)) {

			Bundle b = new Bundle();
			b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.TIME_IN_OUT));
			b.putString(AppsConstant.METHOD, AppsConstant.POST);
			b.putString(AppsConstant.REASON, strComments);
			b.putString(AppsConstant.PARAMS, ParameterBuilder.getTimeinOut(((MainActivity) getActivity()).preferences, strComments, strActionType, strFile));
			getActivity().getLoaderManager().initLoader(LoaderConstant.TIMEINOUT, b, MapFragment.this).forceLoad();
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
				, StringUtils.getDouble(((MainActivity)getActivity()).preferences.getString(Preferences.LONGITUDE,"")),StringUtils.getInt(((MainActivity)getActivity()).preferences.getString(Preferences.SITE_RADIUS,AppsConstant.DEFAULTRADIUS))).getSimpleGeofences();
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
		((MainActivity)getActivity()).preferences.saveString(Preferences.USERLATITUDE, latitude+""); // value to store
		((MainActivity)getActivity()).preferences.commit();
		((MainActivity)getActivity()).preferences.saveString(Preferences.USERLONGITUDE, longitude+""); // value to store
		((MainActivity)getActivity()).preferences.commit();
		UpdateLocationStatus();
		LatLng latLng = new LatLng(latitude, longitude);
		myPositionMarker.setPosition(latLng);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}

}
