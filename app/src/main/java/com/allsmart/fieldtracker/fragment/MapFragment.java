package com.allsmart.fieldtracker.fragment;

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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.model.Response;
import com.allsmart.fieldtracker.model.SimpleGeofence;
import com.allsmart.fieldtracker.parsers.ImageUploadParser;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.SimpleGeofenceStore;
import com.allsmart.fieldtracker.activity.CameraActivity;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.utils.UrlBuilder;
import com.allsmart.fieldtracker.webmethods.RestHelper;
import com.crashlytics.android.Crashlytics;
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
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.utils.CalenderUtils;
import com.allsmart.fieldtracker.storage.EventDataSource;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.customviews.ShiftTimeView;
import com.allsmart.fieldtracker.utils.StringUtils;
import com.allsmart.fieldtracker.model.TimeInOutDetails;
import com.allsmart.fieldtracker.service.UploadTransactions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status>, LoaderManager.LoaderCallbacks<Object>/*, LoaderManager.LoaderCallbacks*/ {
    protected SupportMapFragment mapFragment;
    protected GoogleMap map;
    private LocationRequest mLocationRequest;
    protected Marker myPositionMarker;
    private Preferences preferences;
    protected EventDataSource dataSource;
    private GoogleApiClient mGoogleApiClient;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
    private PendingIntent mPendingIntent;
    private int confirmCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());



        /*mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();

        buildGoogleApiClient();

        if(mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                   // moveToCurentLocation();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(preferences.getDouble(Preferences.USERLATITUDE,0),
                                    preferences.getDouble(Preferences.USERLONGITUDE,0)),16));
                    displayGeofences();
                }
            });
        }*/
    }

    LinearLayout llLogin_Logout, llShiftTime;
    TextView tvTimeInOut, tvTimeInOutLocation,tvTimeInOutMessage;
    ImageView ivCurrentLocation, ivTimeLineExpand;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        if(preferences == null) {
            preferences = new Preferences(getContext());
        }
        ivTimeLineExpand = (ImageView) rootView.findViewById(R.id.ivTimeLineExapand);
        llLogin_Logout = (LinearLayout) rootView.findViewById(R.id.llLogin_Logout);
        tvTimeInOutMessage = (TextView) rootView.findViewById(R.id.tvTimeInOutMessage);
        dataSource = new EventDataSource(getContext());
        /*if(dataSource.getToday().getComments().equalsIgnoreCase("TimeOut")) {
            *//*GregorianCalendar gc = new GregorianCalendar();
            int today = (int) TimeUnit.MILLISECONDS.toDays(gc.getTimeInMillis());
            System.out.println(today  +"     "+preferences.getInt(Preferences.TOMORROW,0));
*//*
            if(preferences.getInt(Preferences.TOMORROW,0) == today) {
                llLogin_Logout.setEnabled(true);
            }else {
                llLogin_Logout.setEnabled(false);
            }
        }
*/
        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();

        buildGoogleApiClient();

        /*if(map == null) {
            if(mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                                LatLng(StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""))
                                , StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""))), 16));

                    }
                });
            }*//*

        }*/


        preferences.saveString(Preferences.LOCATIONSTATUS, ""); // value to store
//		((MainActivity) getActivity()).preferences.saveBoolean(Preferences.INLOCATION, isUserInLoacation());
        preferences.commit();
        tvTimeInOut = (TextView) rootView.findViewById(R.id.tvTimeInOut);
        ivCurrentLocation = (ImageView) rootView.findViewById(R.id.ivCurrentLocation);
        //tvTimeInOut.setText("Time Out");
        tvTimeInOutLocation = (TextView) rootView.findViewById(R.id.tvTimeInOutLocation);
        ShiftTimeView stvShiftTime = (ShiftTimeView) rootView.findViewById(R.id.stvShiftTime);
        llShiftTime = (LinearLayout) rootView.findViewById(R.id.llShiftTime);



        ivTimeLineExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new TimeLineFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle, f).commit();
                fm.executePendingTransactions();
            }
        });

        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage("Location is not enabled !");
                    dialog.setPositiveButton("Open setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
                } else {
                    Fragment f = getFragmentManager().findFragmentById(R.id.flMiddle);
                    if (f instanceof MapFragment) {
                        ((MapFragment) f).moveToCurentLocation();
                    }
                }
            }
        });
        llLogin_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.isNetworkConnectionAvailable(getContext())) {
                    if (preferences == null) {
                        preferences = new Preferences(getContext());
                    }

                    /*if(dataSource.getToday().getComments().equalsIgnoreCase("TimeOut")) {
                        ((MainActivity)getActivity()).displayMessage("You have made Time out for today");
                        llLogin_Logout.setEnabled(false);
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(Calendar.DATE, 1);
                        int tomorrow = (int) TimeUnit.MILLISECONDS.toDays(gc.getTimeInMillis());
                        preferences.saveInt(Preferences.TOMORROW,tomorrow);
                        preferences.commit();
                    }*/

                //    if (!preferences.getString(Preferences.ROLETYPEID, "").equals(null) && preferences.getString(Preferences.ROLETYPEID, "").equalsIgnoreCase("FieldExecutiveOnPremise")) {
                  if(preferences.getBoolean(Preferences.ISONPREMISE,false)) {
                        if (isUserInLoacation()) {

                            if (checkCameraPermission()) {
                                if (checkStoragePermission())
                                    OpenCamera();
                                else
                                    askStoragePermission();
                            } else {
                                askCameraPermission();
                            }
                        } else {
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
                    } else {
                        if (checkCameraPermission()) {
                            if (checkStoragePermission())
                                OpenCamera();
                            else
                                askStoragePermission();
                        } else {
                            askCameraPermission();
                        }
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("No Internet Connection");
                    dialog.setMessage("Please connect to the internet to " +tvTimeInOut.getText().toString());
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        //moveToCurentLocation();
        isUserInLoacation();
        SetLoginLogOut();
        UpdateLocationStatus();

        /*Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,preferences.getString(Preferences.USERNAME,""),"0","1"));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.TIMELINE_LIST,b,MapFragment.this).forceLoad();*/

        return rootView;
    }

    private boolean isUserInLoacation() {
        float[] result = new float[1];
        double lat1 = StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""));
        double lon1 = StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""));
        double lat2 = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double lon2 = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        //Double distance = distance(lat1, lon1, lat2, lon2);
        int siteRadius = Integer.parseInt(preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS));
        Location.distanceBetween(lat1, lon1, lat2, lon2, result);
        float distance = result[0];
        Boolean isInLocation = distance <= siteRadius;

        if (isInLocation) {
            preferences.saveBoolean(Preferences.INLOCATION, true);
            preferences.saveString(Preferences.LOCATIONSTATUS, "False");
        } else {
            preferences.saveBoolean(Preferences.INLOCATION, false);
            preferences.saveString(Preferences.LOCATIONSTATUS, "True");
        }
        preferences.commit();

        return isInLocation;
    }

    public void moveToCurentLocation() {
        if (checkPermission()) {
            if (map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""))
                        , StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""))), 16));

                Log.d(MainActivity.TAG,preferences.getString(Preferences.USERLATITUDE,"") + "   "
                        +preferences.getString(Preferences.USERLONGITUDE,"") + " moveToCurrentLocation" );
            }
        }
    }

   /* private void setUpMapIfNeeded() {
        if (map == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    displayGeofences();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(preferences.getDouble(Preferences.USERLATITUDE,0),
                            preferences.getDouble(Preferences.USERLONGITUDE,0)),16));
                }
            });
        }
    }*/

    public void UpdateLocationStatus() {
        if (TextUtils.isEmpty(preferences.getString(Preferences.LOCATIONSTATUS, ""))) {
            if (preferences.getBoolean(Preferences.INLOCATION, false)) {
                tvTimeInOutLocation.setText("at " + (preferences.getString(Preferences.SITENAME, "")));
                preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
                preferences.commit();
            } else {

                tvTimeInOutLocation.setText("(Not at location)");
                preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
                preferences.commit();
            }
        } else if (preferences.getBoolean(Preferences.INLOCATION, false) && !preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("True")) {
            tvTimeInOutLocation.setText("at " + preferences.getString(Preferences.SITENAME, ""));
            preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
            preferences.commit();
        } else if (!preferences.getBoolean(Preferences.INLOCATION, false)
                && !preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("False")) {
            tvTimeInOutLocation.setText("(Not at location)");
            preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
            preferences.commit();
        }

    }

    public void SetLoginLogOut() {
        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
        TimeInOutDetails details = dataSource.getToday();

        tvTimeInOutMessage.setVisibility(View.GONE);
        tvTimeInOut.setVisibility(View.VISIBLE);

        if(/*dataSource.getToday().getUsername().equalsIgnoreCase(preferences.getString(Preferences.USERNAME,""))*/
                dataSource.checkLoggedOut(preferences.getString(Preferences.USERNAME,""))) {
            String clocDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
            String lastDate = CalenderUtils.getDateMethod(dataSource.getToday().getClockDate(), CalenderUtils.DateMonthDashedFormate);

            Log.d(MainActivity.TAG,"today " + clocDate + "     lastEntry " + lastDate);
            if(clocDate.equals(lastDate)) {
                //  ((MainActivity)getActivity()).displayMessage("You can Time In/Out once per day");
                llLogin_Logout.setEnabled(true);
                tvTimeInOutMessage.setVisibility(View.GONE);
                tvTimeInOut.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.bluerectagleborder,null));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.bluerectagleborder));
                }


            }else {
                tvTimeInOut.setVisibility(View.GONE);
                llLogin_Logout.setEnabled(false);
                tvTimeInOutMessage.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder,null));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder));
                }
            }
        } else {
            llLogin_Logout.setEnabled(false);
            tvTimeInOutMessage.setVisibility(View.VISIBLE);
            tvTimeInOut.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder,null));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder));
            }
        }


        String lastDate = CalenderUtils.getDateMethod(details.getClockDate(), CalenderUtils.DateMonthDashedFormate);
        if (!details.equals(null) && clockDate.equalsIgnoreCase(lastDate)) {
            String actionType = details.getComments();
            if (TextUtils.isEmpty(actionType) || actionType.equalsIgnoreCase("TimeOut")) {
                llShiftTime.setVisibility(View.GONE);
                tvTimeInOut.setText("Time In");
                llLogin_Logout.setVisibility(View.VISIBLE);
            } else {
                llShiftTime.setVisibility(View.VISIBLE);
                tvTimeInOut.setText("Time Out");
                llLogin_Logout.setVisibility(View.VISIBLE);
//					tvTimeInOutLocation.setText("Time In at " + getCurrentTime(new Date()));
            }
        } else {
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
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
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
        broadcastLocationFound(location);
        Log.d(MainActivity.TAG, "new location : " + location.getLatitude() + ", " + location.getLongitude() + ". " + location.getAccuracy());


        if (!MainActivity.geofencesAlreadyRegistered) {
            registerGeofences();
        }
    }

    public void broadcastLocationFound(Location location) {
        updateMarker(location.getLatitude(), location.getLongitude());
    }

    protected void registerGeofences() {
        if (MainActivity.geofencesAlreadyRegistered) {
            return;
        }

        Log.d(MainActivity.TAG, "Registering Geofences");

        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore
                .getInstance(preferences.getString(Preferences.SITENAME, ""), StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""))
                        , StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, "")), StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS))).getSimpleGeofences();

        GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
        geofencingRequestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();

            geofencingRequestBuilder.addGeofence(sg.toGeofence());
        }

        GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();

        mPendingIntent = requestPendingIntent();
        if (checkPermission()) {
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofencingRequest, mPendingIntent).setResultCallback(this);
        }

        MainActivity.geofencesAlreadyRegistered = true;
    }

    private PendingIntent requestPendingIntent() {

        if (null != mPendingIntent) {
            return mPendingIntent;
        } else {

          //  Intent intent = new Intent(getActivity(), GeofenceReceiver.class);
          //  return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent("com.allsmart.fieldtracker.ACTION_GEOFENCE_RECEIVER");
            return PendingIntent.getBroadcast(getContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

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
        if(preferences == null) {
            preferences = new Preferences(getContext());
        }

        super.onResume();
    //    setUpMapIfNeeded();
       // moveToCurentLocation();

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
             //       moveToCurentLocation();
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
          //          map.setMyLocationEnabled(true);
                    //map.animateCamera(CameraUpdateFactory.zoomTo(16));
           //         moveToCurentLocation();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                            LatLng(StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""))
                            , StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""))), 16));

                    if(preferences.getBoolean(Preferences.ISONPREMISE,true)) {
                        displayGeofences();
                    }

                }
            });
        }

        Log.d(MainActivity.TAG,preferences.getString(Preferences.USERNAME,"") + " This is username");
        if(/*dataSource.getToday().getUsername().equalsIgnoreCase(preferences.getString(Preferences.USERNAME,""))*/
                dataSource.checkLoggedOut(preferences.getString(Preferences.USERNAME,""))) {
            String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
            String lastDate = CalenderUtils.getDateMethod(dataSource.getToday().getClockDate(), CalenderUtils.DateMonthDashedFormate);

            Log.d(MainActivity.TAG,"today " + clockDate + "     lastEntry " + lastDate);
            if(clockDate.equals(lastDate)) {
              //  ((MainActivity)getActivity()).displayMessage("You can Time In/Out once per day");
                llLogin_Logout.setEnabled(true);
                tvTimeInOutMessage.setVisibility(View.GONE);
                tvTimeInOut.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.bluerectagleborder,null));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.bluerectagleborder));
                }


            }else {
                tvTimeInOut.setVisibility(View.GONE);
                llLogin_Logout.setEnabled(false);
                tvTimeInOutMessage.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder,null));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder));
                }
            }
        } else {
            llLogin_Logout.setEnabled(false);
            tvTimeInOutMessage.setVisibility(View.VISIBLE);
            tvTimeInOut.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder,null));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                llLogin_Logout.setBackground(getResources().getDrawable(R.drawable.grayrectangleborder));
            }
        }



    }


    // Create GoogleApiClient instance
    protected synchronized void buildGoogleApiClient() {
        Log.i(MainActivity.TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        if (mLocationRequest == null) {
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
        if (checkPermission()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private Location lastLocation;

    private void getLastKnownLocation() {
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                updateMarker(lastLocation.getLatitude(), lastLocation.getLongitude());
                if(map != null) {
                    map.animateCamera(CameraUpdateFactory.zoomTo(18));
                }
                startLocationUpdates();
            } else {
                startLocationUpdates();
            }
//			getActivity().registerReceiver(receiver,new IntentFilter("com.oppo.sfamanagement.geolocation.service"));
        } else askPermission();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(MainActivity.TAG, "Connected to GoogleApiClient");
        // Zoom in the Google Map
        if(mGoogleApiClient.isConnected()) {
            if(map != null) {
                map.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
            getLastKnownLocation();
        } else {
            Log.d(MainActivity.TAG,"google api not connected");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(MainActivity.TAG, "Connection suspended");
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(MainActivity.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    //	protected LocationRequest mLocationRequest;
    protected void startLocationUpdates() {
        if (checkPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }

    private void OpenCamera() {
        Intent i = new Intent(getActivity(), CameraActivity.class);
        i.putExtra("camera_key", AppsConstant.FRONT_CAMREA_OPEN);
        i.putExtra("purpose", "ForPhoto");
        startActivityForResult(i, REQ_CAMERA);
    }

    private final static int REQ_CAMERA = 1003;

    private boolean checkCameraPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    private String getCurrentTime(Date date) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);

        return (String) DateFormat.format("hh:mm", mCalendar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CAMERA) {
                final String strFile = data.getStringExtra("response");
                if (!TextUtils.isEmpty(strFile)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    if (!tvTimeInOut.getText().toString().equals(null) && !TextUtils.isEmpty(tvTimeInOut.getText().toString()) && tvTimeInOut.getText().toString().equalsIgnoreCase("Time In")) {
                        dialog.setTitle("Confirm Time In");
                    } else {
                        dialog.setTitle("Confirm Time Out");
                    }
                    if (isUserInLoacation()) {
                        dialog.setMessage("You are currently at " + preferences.getString(Preferences.SITENAME, ""));
                    } else {
                        dialog.setMessage("You are currently Off Site");
                    }
                    dialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            /*if (dataSource.getToday().getComments().equalsIgnoreCase("TimeOut")) {
                                llLogin_Logout.setEnabled(false);
                            }*/
                           /* confirmCount++;
                            GregorianCalendar gc = new GregorianCalendar();
                            gc.add(Calendar.DATE, 1);
                            int tomorrow = (int) TimeUnit.MILLISECONDS.toDays(gc.getTimeInMillis());
                            preferences.saveInt(Preferences.TOMORROW,tomorrow);
                            preferences.commit();
                            if(confirmCount == 2) {
                                llLogin_Logout.setEnabled(false);
                                ((MainActivity)getActivity()).displayMessage("You have made Time out for today");
                            }*/

                            /*GregorianCalendar gc = new GregorianCalendar();
                            gc.add(Calendar.DATE, 1);
                            int tomorrow = (int) TimeUnit.MILLISECONDS.toDays(gc.getTimeInMillis());
                            preferences.saveInt(Preferences.TOMORROW,tomorrow);
                            preferences.commit();*/

                            UploadTimeInOut(strFile);
                        }
                    });
                    dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        }
    }

    private void getTomorrow() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);

    }


    private void UploadTimeInOut(String strFile) {
        String strDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
        String strActionType = "";
        String strComments = "";
        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
        TimeInOutDetails details = dataSource.getToday();
        /*if(details.getComments().equalsIgnoreCase("TimeOut")) {
            llLogin_Logout.setEnabled(false);
        }*/
        String lastDate = CalenderUtils.getDateMethod(details.getClockDate(), CalenderUtils.DateMonthDashedFormate);
        if (!details.equals(null) && clockDate.equalsIgnoreCase(lastDate)) {
            String actionType = details.getComments();
            if (TextUtils.isEmpty(actionType) || actionType.equalsIgnoreCase("TimeOut")) {
                strActionType = "clockIn";
                strComments = "TimeIn";
            } else {
                strActionType = "clockOut";
                strComments = "TimeOut";
            }
        } else {
            strActionType = "clockIn";
            strComments = "TimeIn";
        }

        dataSource.insertTimeInOutDetails(getTimeInOutDetails(strComments, strActionType, strFile, "false"));
        if (NetworkUtils.isNetworkConnectionAvailable(getContext())) {
            Intent uploadTraIntent = new Intent(getContext(), UploadTransactions.class);
            getActivity().startService(uploadTraIntent);
            /*String image = uploadImage(strFile);
            Log.d(MainActivity.TAG,image);
            if (!TextUtils.isEmpty(image) && !image.equalsIgnoreCase("error")) {
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.TIME_IN_OUT));
                b.putString(AppsConstant.METHOD,AppsConstant.POST);
                b.putString(AppsConstant.PARAMS,ParameterBuilder.getTimeinOut(preferences,strComments,strActionType,image));
                getActivity().getLoaderManager().initLoader(LoaderConstant.TIMEINOUT,b,MapFragment.this).forceLoad();
            } else {
                ((MainActivity)getActivity()).displayMessage("Image was not uploaded try again ");
            }*/

        }
        preferences.saveString(Preferences.TIMEINTIME, CalenderUtils.getCurrentDate("dd/MM/yyyy HH:mm:ss"));
        preferences.commit();

        //
         SetLoginLogOut();
    }

    public String uploadImage(String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            String imageResponse = new RestHelper().makeRestCallAndGetResponseImageUpload(Services.DomainUrlImage, imgPath, "ForPhoto",
                    preferences);
            String serverPath = new ImageUploadParser(imageResponse).Parse();
            return serverPath;
        }
        else {
            return "";
        }
    }

    private TimeInOutDetails getTimeInOutDetails(String strComments, String strType, String strImage, String isPushed) {
        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateFormateWithZone);
        TimeInOutDetails details = new TimeInOutDetails();
        details.setUsername(preferences.getString(Preferences.USERNAME, ""));
        details.setClockDate(clockDate);
        details.setActionType(strType);
        details.setComments(strComments);
        details.setLatitude(preferences.getString(Preferences.USERLATITUDE, ""));
        details.setLongitude(preferences.getString(Preferences.USERLONGITUDE, ""));
        details.setActionImage(strImage);
        details.setIsPushed(isPushed);
        return details;
    }

    private boolean checkStoragePermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askStoragePermission() {
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_Storage_PERMISSION);
    }

    private void askCameraPermission() {
        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMISSION);
    }

    private final static int REQ_PERMISSION = 1001;
    private final static int REQ_CAMERA_PERMISSION = 1002;
    private final static int REQ_Storage_PERMISSION = 1000;

    // Asks for permission
    private void askPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
            case REQ_CAMERA_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkStoragePermission())
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    protected void displayGeofences() {
        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance(preferences.getString(Preferences.SITENAME, ""), StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""))
                , StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, "")), StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS))).getSimpleGeofences();

        Log.d(MainActivity.TAG,preferences.getString(Preferences.SITENAME, "")+","+ StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""))
                +","+ StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""))+","+ StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS,AppsConstant.DEFAULTRADIUS)));
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(sg.getLatitude(), sg.getLongitude()))
                    .strokeColor(Color.argb(50, 0, 127, 255))
                    .fillColor(Color.argb(100, 137, 207, 240))
                    .radius(sg.getRadius());
            map.addCircle(circleOptions);
        }
    }

    protected void createMarker(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        if(myPositionMarker != null) {
            myPositionMarker.remove();
            if(map != null) {
                myPositionMarker = map.addMarker((new MarkerOptions()
                        .position(latLng))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
            }

        } else {
            if(map != null) {
                myPositionMarker = map.addMarker((new MarkerOptions()
                        .position(latLng))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
            }
        }
        Log.d(MainActivity.TAG,"User Pointer Lat Long  :  " + latitude + "  " + longitude );
      //  map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    protected void updateMarker(Double latitude, Double longitude) {
//        LatLng latLng = new LatLng(latitude, longitude);
        //       System.out.println("This is at updateMarker  "+latitude + "      " + longitude);
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
   //     if (myPositionMarker == null) {
            createMarker(latitude, longitude);
            if(preferences == null) {
                preferences = new Preferences(getContext());
            }
            preferences.saveString(Preferences.USERLATITUDE, latitude+""); // value to store
            preferences.saveString(Preferences.USERLONGITUDE, longitude+""); // value to store
            preferences.commit();

        UpdateLocationStatus();
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.TIMEINOUT:
                return new LoaderServices(getContext(), LoaderMethod.TIMEINOUT,args);
            /*case LoaderConstant.TIMELINE_LIST:
                return new LoaderServices(getContext(),LoaderMethod.TIMELINE_LIST,args);
*/            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
        }
        switch (loader.getId()) {
            case LoaderConstant.TIMEINOUT:
            if(data != null && data instanceof Response) {
                Response response = (Response) data;
                if(response != null) {
                    if(response.getResponceCode().equals("200")) {
                        ((MainActivity)getActivity()).displayMessage(response.getResponceMessage());
                        SetLoginLogOut();
                    }  else  {
                        if(!TextUtils.isEmpty(response.getResponceMessage())) {
                            ((MainActivity) getActivity()).displayMessage(response.getResponceMessage());
                        } else {
                            ((MainActivity) getActivity()).displayMessage("Something went wrong please try again");
                        }
                    }
                }
            } else {
                ((MainActivity)getActivity()).displayMessage("Error in response");
            }
            break;
            /*case LoaderConstant.TIMELINE_LIST:
                if(data != null && data instanceof ArrayList) {
                    if(!preferences.getBoolean(Preferences.ALLOWTIMEIN,true)) {
              //          llLogin_Logout.setEnabled(false);
                    } else {
              //          llLogin_Logout.setEnabled(true);
                    }
                } else {
                    ((MainActivity)getActivity()).displayMessage("Error in response");
                }

                break;*/
        }

        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
