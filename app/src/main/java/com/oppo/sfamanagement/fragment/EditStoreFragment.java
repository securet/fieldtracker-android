package com.oppo.sfamanagement.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.model.Store;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class EditStoreFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks {

    protected EditText storeName,address;
    protected TextView lattitude,longitude;
    protected Button btEdit,btCancel;
    private int storeId;
    protected ProgressDialog pd;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_store,container,false);
        storeName = (EditText) view.findViewById(R.id.etStoreName);
        address = (EditText) view.findViewById(R.id.etAddress);
        lattitude = (TextView) view.findViewById(R.id.tvLattitude);
        longitude = (TextView) view.findViewById(R.id.tvLongitude);
        btEdit = (Button) view.findViewById(R.id.btEdit);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        pd = new ProgressDialog(getContext());
        btEdit.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        Store b = getArguments().getParcelable("Store");
        storeId = b.getStoreId();
        storeName.setText(b.getStoreName());
        address.setText(b.getAddress());
        lattitude.setText("Lat: " + b.getLattitude());
        longitude.setText("Lon: " +b.getLongitude());
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

                Bundle b = new Bundle();
                b.putString(AppsConstant.URL, UrlBuilder.getStoreUpdate(Services.STORE_UPDATE,String.valueOf(storeId)));
                b.putString(AppsConstant.METHOD, AppsConstant.PUT );
                b.putString(AppsConstant.PARAMS, ParameterBuilder.getStoreUpdate(String.valueOf(storeId),sName,sAddress,sLattitude,sLongitude));
                getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_UPDATE,b, EditStoreFragment.this).forceLoad();
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
        switch (id) {
            case LoaderConstant.STORE_UPDATE:
                return new LoaderServices(getContext(),LoaderMethod.STORE_UPDATE,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader loader, Object data) {
        if (isAdded()) {
            getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {

    }

}
