package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.oppo.sfamanagement.CameraActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Store;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddPromoterFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Object> {
    EditText fN,lN,ph,eAdd,address;
    public static final int FRONT_CAMREA_OPEN = 1;
    public static final int BACK_CAMREA_OPEN = 2;
    ImageView ivPhoto,ivAdhar,ivAddressProof;
    TextView storeAssignment,seAssignment;
    int storeId ;
    Preferences preferences;
    ArrayList<Store> list;

 //   protected Preferences preferences;
    String image[] = new String[3];
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_promoter,container,false);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        ivAdhar = (ImageView) view.findViewById(R.id.ivAadhar);
        ivAddressProof = (ImageView) view.findViewById(R.id.ivAddressProof);
        fN = (EditText) view.findViewById(R.id.etPFN);
        lN = (EditText) view.findViewById(R.id.etPLN);
        ph = (EditText) view.findViewById(R.id.etPPh);
        eAdd = (EditText) view.findViewById(R.id.etPEA);
        address = (EditText) view.findViewById(R.id.etPAdd);
        storeAssignment = (TextView)view.findViewById(R.id.tvStoreAssignment);
        seAssignment = (TextView) view.findViewById(R.id.tvSEAssignment);
        Button Add = (Button) view.findViewById(R.id.btPAdd);
        Button Cancel = (Button) view.findViewById(R.id.btAddPCancel);
        storeAssignment.setTag(new Store());
        preferences = new Preferences(getActivity());

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.STORE_LIST));
        b.putString(AppsConstant.METHOD,AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,b,AddPromoterFragment.this).forceLoad();

        storeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomBuilder builder = new CustomBuilder(getContext(),"Select Store",true);
                builder.setSingleChoiceItems(list,storeAssignment.getTag(), new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        storeAssignment.setTag((Store)selectedObject);
                        storeAssignment.setText(((Store) selectedObject).getStoreName());
                        storeId = ((Store) selectedObject).getStoreId();
                        seAssignment.setText(preferences.getString(Preferences.USERFULLNAME,"Full Name"));
                        builder.dismiss();
                    }
                });
                builder.setCancelListener(new CustomBuilder.OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                });
                builder.show();
            }
        });

//        String imagePath = getArguments().getString("server_imagepath");
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = fN.getText().toString();
                String lName = lN.getText().toString();
                String sPh = ph.getText().toString();
                String  email = eAdd.getText().toString();
                String sAdd = address.getText().toString();
                String sId = storeAssignment.getText().toString();
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.ADD_PROMOTER));
                b.putString(AppsConstant.METHOD, AppsConstant.POST);
                b.putString(AppsConstant.PARAMS, ParameterBuilder.getAddPromoter("RqtAddPromoter",fName,lName,sPh,email,sAdd,
                        String.valueOf(storeId),"ReqSubmitted","RqtAddPromoter",image[0],image[1],image[2]));
                getActivity().getLoaderManager().initLoader(LoaderConstant.ADD_PROMOTER,b,AddPromoterFragment.this).forceLoad();
            }
        });
        ivPhoto.setOnClickListener(this);
        ivAdhar.setOnClickListener(this);
        ivAddressProof.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AppsConstant.IMAGE_PHOTO){
            String responseValue = data.getStringExtra("image_photo");
            if(!responseValue.equals(null) && !responseValue.equals("cancel")) {
                Toast.makeText(getContext(),responseValue,Toast.LENGTH_SHORT).show();
                Log.d("path",responseValue);
                image[0] = responseValue;
                ivPhoto.setImageResource(R.drawable.photo_tick);
                ivPhoto.setEnabled(false);
            }
        } else if (resultCode == AppsConstant.IMAGE_AADHAR) {
            String responseValue = data.getStringExtra("image_photo");
            if(!responseValue.equals(null) && !responseValue.equals("cancel")) {
                Toast.makeText(getContext(),responseValue,Toast.LENGTH_SHORT).show();
                Log.d("path",responseValue);
                image[1] = responseValue;
                ivAdhar.setImageResource(R.drawable.aadhartick);
                ivAdhar.setEnabled(false);
            }
        } else if (resultCode == AppsConstant.IMAGE_ADDRESS_PROOF) {
            String responseValue = data.getStringExtra("image_photo");
            if(!responseValue.equals(null) && !responseValue.equals("cancel")) {
                Toast.makeText(getContext(),responseValue,Toast.LENGTH_SHORT).show();
                Log.d("path",responseValue);
                image[2] = responseValue;
                ivAddressProof.setImageResource(R.drawable.id_card_tick);
                ivAddressProof.setEnabled(false);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhoto:

              /*  Bundle bundle = new Bundle();
                bundle.putInt("camera_key",FRONT_CAMREA_OPEN);
                bundle.putString("purpose","For Photo"); */
                Intent i = new Intent(getActivity(), CameraActivity.class);
                i.putExtra("camera_key",FRONT_CAMREA_OPEN);
                i.putExtra("purpose","For Photo");
                startActivityForResult(i,AppsConstant.IMAGE_PHOTO);
                //((Activity) getActivity()).overridePendingTransition(0,0);
   //             Fragment fragment = new CameraFragment();

                break;
            case R.id.ivAadhar:
                Intent i2 = new Intent(getActivity(),CameraActivity.class);
                i2.putExtra("camera_key",BACK_CAMREA_OPEN);
                i2.putExtra("purpose","For Aadhar");
                startActivityForResult(i2,AppsConstant.IMAGE_AADHAR);
                break;
            case R.id.ivAddressProof:
                Intent i3 = new Intent(getActivity(),CameraActivity.class);
                i3.putExtra("camera_key",BACK_CAMREA_OPEN);
                i3.putExtra("purpose","For Address Proof");
                startActivityForResult(i3,AppsConstant.IMAGE_ADDRESS_PROOF);
                break;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.ADD_PROMOTER:
                return new LoaderServices(getContext(), LoaderMethod.ADD_PROMOTER,args);
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getContext(),LoaderMethod.STORE_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        switch (loader.getId()) {
            case LoaderConstant.STORE_LIST:
                if(data!=null && data instanceof ArrayList ){
                    list = (ArrayList<Store>) data;
                }
                else {

                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
        }

        if (isAdded()) {
            getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
