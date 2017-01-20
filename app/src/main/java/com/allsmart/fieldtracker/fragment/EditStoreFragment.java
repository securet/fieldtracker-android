package com.allsmart.fieldtracker.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class EditStoreFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks {

    protected EditText storeName,address,siteRadius;
    protected TextView lattitude,longitude;
    protected Button btEdit,btCancel,getLocation;
    private Preferences preferences;
    private int storeId;
    protected ProgressDialog pd;
    private String lat = "";
    private String lon = "";
    private boolean isGetLocationClicked = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_store,container,false);
        storeName = (EditText) view.findViewById(R.id.etStName);
        address = (EditText) view.findViewById(R.id.etStAddress);
        lattitude = (TextView) view.findViewById(R.id.tvLattitude);
        longitude = (TextView) view.findViewById(R.id.tvLongitude);
        btEdit = (Button) view.findViewById(R.id.btEdit);
        siteRadius = (EditText) view.findViewById(R.id.etSiteRadius);
        getLocation = (Button) view.findViewById(R.id.getLocation);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        pd = new ProgressDialog(getContext());
        btEdit.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                isGetLocationClicked = true;
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                lattitude.setText(lat);
                longitude.setText(lon);
            }
        });
        Store b = getArguments().getParcelable("Store");
        storeId = b.getStoreId();
        storeName.setText(b.getStoreName());
        address.setText(b.getAddress());
        siteRadius.setText(b.getSiteRadius());
        lattitude.setText(b.getLattitude());
        longitude.setText(b.getLongitude());
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btEdit:
                String sName = storeName.getText().toString();
                String sAddress = address.getText().toString();
                String sLattitude = lattitude.getText().toString();
                String sLongitude = longitude.getText().toString();
                String proximity = siteRadius.getText().toString();
                if(!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sAddress) && !TextUtils.isEmpty(proximity) ) {
                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL, UrlBuilder.getStoreUpdate(Services.STORE_UPDATE, String.valueOf(storeId)));
                    b.putString(AppsConstant.METHOD, AppsConstant.PUT);
                    b.putString(AppsConstant.PARAMS, ParameterBuilder.getAddStore(sName, sAddress, sLattitude, sLongitude, proximity));
                    getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_UPDATE, b, EditStoreFragment.this).forceLoad();
                } else {
                    Toast.makeText(getContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCancel:
                Fragment fragment = new StoreListFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).commit();
                fm.executePendingTransactions();
                break;
        }
    }


    @Override
    public android.content.Loader onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.STORE_UPDATE:
                return new LoaderServices(getContext(),LoaderMethod.STORE_UPDATE,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader loader, Object data) {
     //   if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
       // }
        if(data != null && data instanceof String) {

        } else {

        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());
        if(data.equals("success")) {
            Toast.makeText(getContext(),
                    "Store Added Successfully",
                    Toast.LENGTH_SHORT).show();
            Fragment fragment = new StoreListFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.flMiddle,fragment).commit();
            fm.executePendingTransactions();
        } else {
            Toast.makeText(getContext(),
                    "Failed to Upload",
                    Toast.LENGTH_SHORT).show();
        }

    //    if(getActivity() != null  && getActivity() instanceof  MainActivity) {

    //    }

    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {

    }

}
