package com.allsmart.fieldtracker.fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddStoreFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Object>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText storeName, storeAddress, siteRadius;
    Button getCurrentLocation, btAdd, btCancel;
    TextView latitude, longitude;
    String lat = "";
    String lon = "";
    private boolean isClicked = false;
    private GoogleApiClient googleApiClient;
    private Location location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_store_fragment, container, false);
        storeName = (EditText) view.findViewById(R.id.etStoreName);
        storeAddress = (EditText) view.findViewById(R.id.etAddress);
        siteRadius = (EditText) view.findViewById(R.id.etStoreRadius);
        getCurrentLocation = (Button) view.findViewById(R.id.btGetLocation);
        btAdd = (Button) view.findViewById(R.id.btAdd);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext(),this,this).
                    addApi(LocationServices.API).
                    build();
        }
        btAdd.setOnClickListener(this);

        // btAdd.setEnabled(false);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        latitude = (TextView) view.findViewById(R.id.latitude);
        longitude = (TextView) view.findViewById(R.id.longitude);
        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        if (googleApiClient.isConnected()) {
                            Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            if(loc != null) {
                                latitude.setText(loc.getLatitude() + "");
                                longitude.setText(loc.getLongitude() + "");
                         //       ((MainActivity) getActivity()).displayMessage("Accurate to " + location.getAccuracy() + " m");
                                isClicked = true;
                            } else {
                                ((MainActivity) getActivity()).displayMessage("Unable to get your location");
                            }
                        } else {
                            Log.d(MainActivity.TAG,"Google API is not connected");
                        }
                    }else {
                        requestPermission();
                    }
                }else {
                    boolean gps_enabled = false;
                    boolean network_enabled = false;
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    try {
                        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception e) {
                        Logger.e("Log", e);
                        Crashlytics.log(1, getClass().getName(), "Error in Map Fragment");
                        Crashlytics.logException(e);
                    }

                    try {
                        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception e) {
                        Logger.e("Log", e);
                        Crashlytics.log(1, getClass().getName(), "Error in Map Fragment");
                        Crashlytics.logException(e);
                    }

                    if (!gps_enabled && !network_enabled) {
                        ((MainActivity)getActivity()).displayMessage("Network and GPS is not available");
                    } else {
                        if(network_enabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {

                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            });

                            if(locationManager != null) {
                                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if(loc != null) {
                                    latitude.setText(loc.getLatitude()+"");
                                    longitude.setText(loc.getLongitude()+"");
                                    isClicked = true;
                                }
                            }
                        }
                        if(gps_enabled){
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {

                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            });

                            if(locationManager != null) {
                                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(loc != null) {
                                    latitude.setText(loc.getLatitude()+"");
                                    longitude.setText(loc.getLongitude()+"");
                                    isClicked = true;
                                }
                            }
                        }
                    }

                }
            }
        });
        storeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    storeAddress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {
                                //      btAdd.setEnabled(false);
                            } else {
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    btAdd.setBackground(getResources().getDrawable(R.drawable.editstore_edit_button));
                                }*/
                                btAdd.setEnabled(true);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btCancel.setOnClickListener(this);
        return view;
    }

    private final static int REQ_PERMISSION = 1001;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (googleApiClient.isConnected()) {
                        if(checkPermission()) {
                            Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            if(loc != null){
                                latitude.setText(loc.getLatitude() + "");
                                longitude.setText(loc.getLongitude() + "");
                                isClicked = true;
                            } else {
                                ((MainActivity)getActivity()).displayMessage("Unable to get your location");
                            }
                        } else {
                            requestPermission();
                        }
                    } else {
                        Log.d(MainActivity.TAG,"Google API is not connected");
                    }
                } else {
                    ((MainActivity)getActivity()).displayMessage("Permission denied");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)){
                            showMessageOKCancel("You need to allow access to get location", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                REQ_PERMISSION);
                                    }
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        int result = ContextCompat.checkSelfPermission(getContext(),ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},REQ_PERMISSION);
    }

    private Location getLastKnownLocation() {
        if (googleApiClient.isConnected()) {
            if (checkPermission()) {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null) {
                    return location;
                }
            }
        } else {
            Log.d(MainActivity.TAG, "Google API not connected");
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAdd:
                String sN = storeName.getText().toString();
                String sA = storeAddress.getText().toString();
                String sRadius = siteRadius.getText().toString();

                    if(!TextUtils.isEmpty(sN) && !TextUtils.isEmpty(sA) && !TextUtils.isEmpty(sRadius) ) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.ADD_STORE));
                        b.putString(AppsConstant.METHOD,AppsConstant.POST);
                        if(isClicked) {
                            b.putString(AppsConstant.PARAMS, ParameterBuilder.getAddStore(sN, sA, lat, lon, sRadius));
                            getActivity().getLoaderManager().initLoader(LoaderConstant.ADD_STORE,b,AddStoreFragment.this).forceLoad();
                        }else {
                            Toast.makeText(getContext(),"Lat Long empty, Click get Location Button",Toast.LENGTH_SHORT).show();
                        }
                        Log.d("ADD", sN + "  " + sA + "  " + lat + "  " + lon);

                    } else {
                        Toast.makeText(getContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.btCancel:
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                break;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }switch (id) {
            case LoaderConstant.ADD_STORE:
                return new LoaderServices(getActivity(), LoaderMethod.ADD_STORE,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
        }if (data != null && data instanceof String) {

        } else {
            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
        if(data.equals("success")) {
            Toast.makeText(getContext(),
                    "Store Added Successfully",
                    Toast.LENGTH_SHORT).show();
            FragmentManager fm = getFragmentManager();
            /*Fragment fragment = new StoreListFragment();
            fm.beginTransaction().replace(R.id.flMiddle,fragment).commit();
            fm.executePendingTransactions();*/
            fm.popBackStack();

        } else {
            Toast.makeText(getContext(),
                    "Failed To Add",
                    Toast.LENGTH_SHORT).show();
        }if(getActivity() != null && getActivity() instanceof MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
