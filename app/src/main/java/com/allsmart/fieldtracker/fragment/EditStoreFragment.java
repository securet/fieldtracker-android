package com.allsmart.fieldtracker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.CameraActivity;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.Logger;
import com.allsmart.fieldtracker.utils.NetworkUtils;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.allsmart.fieldtracker.constants.AppsConstant.BACK_CAMREA_OPEN;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class EditStoreFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected EditText storeName,address,siteRadius;
    protected TextView lattitude,longitude;
    protected Button btEdit,btCancel,getLocation;
    private Preferences preferences;
    private ImageView storePhoto, storeImg;
    private ScrollView svParent;
    private String storeId;
    protected ProgressDialog pd;
    private Location location;
    private String lat = "";
    private String lon = "";
    private GoogleApiClient googleApiClient;
    private boolean isGetLocationClicked = false;
    private String storeImage = "";
    private String sLattitude= "";
    private String sLongitude ="";
    private Store b;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        if(googleApiClient == null || !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if(googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_store,container,false);
        storeName = (EditText) view.findViewById(R.id.etStName);
        address = (EditText) view.findViewById(R.id.etStAddress);
        svParent = (ScrollView) view.findViewById(R.id.svEditStore);
        lattitude = (TextView) view.findViewById(R.id.tvLattitude);
        longitude = (TextView) view.findViewById(R.id.tvLongitude);
        btEdit = (Button) view.findViewById(R.id.btEdit);
        storeImg = (ImageView) view.findViewById(R.id.storeImage);
        siteRadius = (EditText) view.findViewById(R.id.etSiteRadius);
        getLocation = (Button) view.findViewById(R.id.getLocation);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        storePhoto = (ImageView) view.findViewById(R.id.ivStorePhoto);
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext()).
                    addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).
                    addApi(LocationServices.API).build();
        }
        pd = new ProgressDialog(getContext());
        btEdit.setOnClickListener(this);
        storePhoto.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        if (googleApiClient.isConnected()) {
                            Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            if(loc != null) {
                                /*lattitude.setText(loc.getLatitude() + "");
                                longitude.setText(loc.getLongitude() + "");*/
              //                  ((MainActivity) getActivity()).displayMessage("Accurate to " + location.getAccuracy() + " m");
                                isGetLocationClicked = true;
                            } else {
                                ((MainActivity) getActivity()).displayMessage("Unable to get your location");
                            }
                        } else {
                            Log.d(MainActivity.TAG,"Google API is not connected");
                        }
                    }else {
                        requestPermission();
                    }
                }else {/*
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
                                    *//*lattitude.setText(loc.getLatitude()+"");
                                    longitude.setText(loc.getLongitude()+"");*//*
                                    isGetLocationClicked = true;
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
                                    lattitude.setText(loc.getLatitude()+"");
                                    longitude.setText(loc.getLongitude()+"");
                                    isGetLocationClicked = true;
                                }
                            }
                        }
                    }*/
                }
            }
        });

        storeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b != null) {
                    if(!TextUtils.isEmpty(b.getStoreImage()) && !b.getStoreImage().equals(null)){
                        StorePhotoFragment f = new StorePhotoFragment();

                        FragmentManager fm = getFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("store_photo",b.getStoreImage());
                        f.setArguments(bundle);
                        f.show(fm,"Photo_Dialog");
                        /*fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                        fm.executePendingTransactions();*/
                    }
                }
            }
        });

        try {
            b = getArguments().getParcelable("Store");
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(1,getClass().getName(),"Error at getting store details");
        }
        if(b != null) {
            storeId = b.getStoreId();
            storeName.setText(b.getStoreName());
            address.setText(b.getAddress());
            siteRadius.setText(b.getSiteRadius());
            lattitude.setText(b.getLattitude());
            longitude.setText(b.getLongitude());
            if(!TextUtils.isEmpty(b.getStoreImage()) && !b.getStoreImage().equals("null")){
                storeImg.setVisibility(View.VISIBLE);
         //       Picasso.with(getContext()).load(UrlBuilder.getServerImage(b.getStoreImage())).fit().into(storeImg);
            } else {
                storeImg.setVisibility(View.INVISIBLE);
            }

        } else {
            ((MainActivity)getActivity()).displayMessage("error in getting store details");
        }

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (googleApiClient.isConnected()) {
                        if(checkPermission()) {
                            Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            if (loc != null) {
                                /*lattitude.setText(loc.getLatitude() + "");
                                longitude.setText(loc.getLongitude() + "");*/
                                isGetLocationClicked = true;
                            } else {
                                ((MainActivity) getActivity()).displayMessage("Unable to get your location");
                            }
                            Log.d(MainActivity.TAG,"Permision granted");
                        } else {
                            requestPermission();
                        }

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

    private Location getLocationForLollipop() {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;

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

            if(gps_enabled){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    if(locationManager != null) {
                        checkPermission();
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }

            }
            if(network_enabled) {
                if(loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if(locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

            }
        }
        return loc;
    }

    private Location getLocationForMarsh() {

        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;

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

        if(!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        if (!gps_enabled && !network_enabled) {
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

            if(checkPermission()) {
                loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }

            if(gps_enabled){
                if(loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    if(locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }

            }
            if(network_enabled) {
                if(loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if(locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

            }
        }

                /*if (loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
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

                }*/
            }
        return loc;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btEdit:
                String sName = storeName.getText().toString();
                String sAddress = address.getText().toString();
                /*String sLattitude = lattitude.getText().toString();
                String sLongitude = longitude.getText().toString();*/
                String proximity = siteRadius.getText().toString();
                if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sAddress) && !TextUtils.isEmpty(proximity)) {
                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL, UrlBuilder.getStoreUpdate(Services.STORE_UPDATE, storeId));
                    b.putString(AppsConstant.METHOD, AppsConstant.PUT);
                    if (!TextUtils.isEmpty(storeImage)) {
                        b.putString(AppsConstant.PARAMS, ParameterBuilder.updateStore(sName, sAddress, sLattitude, sLongitude, proximity, storeImage));
                        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_UPDATE, b, EditStoreFragment.this).forceLoad();
                    } else {
                        Toast.makeText(getContext(), "Please take store photo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCancel:
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                break;

            case R.id.ivStorePhoto:
                if(checkCameraPermission()){
                    if(checkStoragePermission()) {
                        if(checkLocationPermision()) {
                            OpenCamera(BACK_CAMREA_OPEN,AppsConstant.IMAGE_STORE,"ForStore");
                        } else {
                            askLocationPermision();
                        }
                    } else
                        askStoragePermission();
                }else{
                    askCameraPermission();
                }
                break;
        }
    }



    private boolean checkCameraPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED );
    }
    private void OpenCamera(int camera,int resultCode, String purpose){
        Intent i = new Intent(getActivity(), CameraActivity.class);
        i.putExtra("camera_key",camera);
        i.putExtra("purpose",purpose);
        startActivityForResult(i,resultCode);
        //startActivityForResult(i,REQ_CAMERA);
    }
    private boolean checkStoragePermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED );
    }

    private boolean checkLocationPermision() {
        return (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }
    private void askStoragePermission() {
        this.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },REQ_Storage_PERMISSION);
    }
    private void askCameraPermission() {
        this.requestPermissions(new String[] { Manifest.permission.CAMERA },REQ_CAMERA_PERMISSION);
    }
    private void askLocationPermision() {
        this.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQ_LOCATION_PERMISSION);
    }

    private final static int REQ_PERMISSION =1001;
    private final static int REQ_CAMERA_PERMISSION =1002;
    private final static int REQ_Storage_PERMISSION =1000;
    private final static int REQ_LOCATION_PERMISSION = 1004;


    private boolean addExifData(String imagePath) {
        if(!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
        boolean isExifAdded = false;
        ExifInterface exif = null;
        try {
             exif = new ExifInterface(imagePath);
            if(exif != null) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getLocationForMarsh() != null) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,convert(getLocationForMarsh().getLatitude()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,convert(getLocationForMarsh().getLongitude()));
                        sLattitude = getLocationForMarsh().getLatitude()+"";
                        sLongitude = getLocationForMarsh().getLongitude()+"";
                        if(getLocationForMarsh().getLatitude() > 0) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,"N");

                        } else {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,"S");
                        }

                        if(getLocationForMarsh().getLongitude() > 0) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"E");
                        } else {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"F");
                        }
                        if (exif != null) {
                            exif.saveAttributes();
                            isExifAdded = true;
                        }
                    } else {
                        ((MainActivity)getActivity()).displayMessage("GeoTag was not added");
                    }

                } else {
                    if(getLocationForLollipop() != null) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,convert(getLocationForLollipop().getLatitude()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,convert(getLocationForLollipop().getLongitude()));

                        sLattitude = getLocationForLollipop().getLatitude()+"";
                        sLongitude = getLocationForLollipop().getLongitude()+"";
                        if(getLocationForLollipop().getLatitude() > 0) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,"N");

                        } else {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,"S");
                        }

                        if(getLocationForLollipop().getLongitude() > 0) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"E");
                        } else {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"F");
                        }
                        if (exif != null) {
                            exif.saveAttributes();
                            isExifAdded = true;
                        }
                        Log.d(MainActivity.TAG,"Exif Data" + getLocationForLollipop().getLatitude() +"   "
                                + getLocationForLollipop().getLongitude());
                    } else {
                        ((MainActivity)getActivity()).displayMessage("GeoTag was not added");
                    }
                }

            } else {
                isExifAdded = false;
                ((MainActivity)getActivity()).displayMessage("Error in adding GeoTag");
            }

        } catch (IOException e) {
            e.printStackTrace();
            isExifAdded =false;
        }

        return isExifAdded;

    }

    private static StringBuilder sb = new StringBuilder(20);

    public static final String convert(double latitude) {
        latitude = Math.abs(latitude);
        final int degree = (int)latitude;
        latitude *= 60;
        latitude -= degree * 60.0d;
        final int minute = (int)latitude;
        latitude *= 60;
        latitude -= minute * 60.0d;
        final int second = (int)(latitude * 1000.0d);
        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == AppsConstant.IMAGE_STORE) {
                final String image = data.getStringExtra("response");
                final String purpose = data.getStringExtra("image_purpose");
                if(!TextUtils.isEmpty(image)) {
                    if(addExifData(image)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                        bundle.putString(AppsConstant.FILE, image);
                        bundle.putString(AppsConstant.FILEPURPOSE, purpose);
                        getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD, bundle, EditStoreFragment.this).forceLoad();
                    } else {
                        ((MainActivity)getActivity()).displayMessage("GeoTag not added, Please take again");
                    }
                } else {
                    ((MainActivity)getActivity()).displayMessage("Image not saved, please take again");
                }

            }
        }
    }

    @Override
    public android.content.Loader onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.STORE_UPDATE:
                return new LoaderServices(getContext(),LoaderMethod.STORE_UPDATE,args);
            case LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(),LoaderMethod.IMAGE_UPLOAD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader loader, Object data) {
     //   if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
       // }
        switch (loader.getId()) {
            case LoaderConstant.STORE_UPDATE:
                if(data != null && data instanceof String) {
                    if(data.equals("success")) {
                        Toast.makeText(getContext(),
                                "Store Added Successfully",
                                Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getFragmentManager();
                        fm.popBackStack();
                    } else {
                        Toast.makeText(getContext(),
                                "Failed to Upload",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Error in response, Please try again!",
                            Toast.LENGTH_SHORT).show();
                }


                break;
            case LoaderConstant.IMAGE_UPLOAD:
                if (data != null && data instanceof String) {
                    String result = (String) data;
                    if(!TextUtils.isEmpty(result) && !result.equalsIgnoreCase("error")) {
                        storeImage = (String) data;
                        Toast.makeText(getContext(),
                                "Image Uploaded Successfully",
                                Toast.LENGTH_SHORT).show();
                            storePhoto.setEnabled(false);
                    } else {
                            //error
                            Toast.makeText(getContext(),
                                    "Failed to upload. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                    }
                } else {
                        Toast.makeText(getContext(),
                                "Error in response. Please try again.",
                                Toast.LENGTH_SHORT).show();
                }
                    Log.d("IMAGE", (String) data);

                break;
        }

        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (googleApiClient.isConnected()) {

        } else {
            Log.d(MainActivity.TAG,"Google API not connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
}
