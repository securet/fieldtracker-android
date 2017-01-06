package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.model.Store;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.oppo.sfamanagement.database.AppsConstant.BACK_CAMREA_OPEN;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class EditPromoterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, View.OnClickListener {

    private Button edit,cancel;
    private EditText firstName,lastName,phone,emailAddress,Address;
    ImageView ivPhoto,ivAadhar,ivAddress;
    private TextView tvStore,tvSE;
    private ArrayList<Store> list;
    private String[] image = new String[3];
    private int i = 0;
    private Promoter promoter;
    private Preferences preferences;
    private int storeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promoter = getArguments().getParcelable("promoter");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_promoter_fragment,container,false);
        edit = (Button) view.findViewById(R.id.btEPEdit);
        cancel = (Button) view.findViewById(R.id.btEAddPCancel);
        firstName = (EditText) view.findViewById(R.id.etPromoterFN);
        lastName = (EditText) view.findViewById(R.id.etPromoterLN);
        phone = (EditText) view.findViewById(R.id.etPromoterPh);
        emailAddress = (EditText) view.findViewById(R.id.etPromoterEA);
        tvStore = (TextView) view.findViewById(R.id.tvStoreAssignment);
        tvSE = (TextView) view.findViewById(R.id.tvSEAssignment);
        Address = (EditText) view.findViewById(R.id.etPromoterAdd);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        ivAddress = (ImageView) view.findViewById(R.id.ivAddressProof);
        ivAadhar = (ImageView) view.findViewById(R.id.ivAadhar);

        preferences = new Preferences(getContext());


        final String photo  = promoter.getUserPhoto();
        final String adhar  = promoter.getAadharIdPath();
        final String address = promoter.getAddressIdPath();

        firstName.setText(promoter.getFirstName());
        lastName.setText(promoter.getLastName());
        phone.setText(promoter.getPhoneNum());
        emailAddress.setText(promoter.getEmailAddress());
        Address.setText(promoter.getAddress());
        if (isApproved()) {
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            phone.setEnabled(false);
            emailAddress.setEnabled(false);
            Address.setEnabled(false);
            tvStore.setEnabled(false);
            tvSE.setEnabled(false);
            edit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }



        System.out.println(photo + "     " + adhar + "           " + address);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(photo)).placeholder(R.drawable.photo).fit().into(ivPhoto);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(adhar)).placeholder(R.drawable.aadhar).fit().into(ivAadhar);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(address)).placeholder(R.drawable.id_card).fit().into(ivAddress);
        ivPhoto.setOnClickListener(this);
        ivAadhar.setOnClickListener(this);
        ivAddress.setOnClickListener(this);
        tvStore.setTag(new Store());
        if (!isApproved()) {
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.STORE_LIST));
            b.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST, b, EditPromoterFragment.this).forceLoad();
        }
        tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomBuilder builder = new CustomBuilder(getContext(),"Select Store",true);
                builder.setSingleChoiceItems(list,tvStore.getTag(), new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        tvStore.setTag(selectedObject);
                        tvStore.setText(((Store) selectedObject).getStoreName());
                        storeId = ((Store) selectedObject).getStoreId();
                        tvSE.setText(preferences.getString(Preferences.USERFULLNAME,"Full Name"));
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
      //  if (!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
            edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL,UrlBuilder.getUpdatePromoter(Services.UPDATE_PROMOTER,promoter.getRequestId()));
                    b.putString(AppsConstant.METHOD,AppsConstant.PUT);
                if(image != null) {
                    b.putString(AppsConstant.PARAMS, ParameterBuilder.getPromoterUpdate(promoter.getRequestId(), promoter.getRequestType(), firstName.getText().toString(),
                            lastName.getText().toString(), phone.getText().toString(), Address.getText().toString(), emailAddress.getText().toString(), storeId + "", "ReqSubmitted",
                            "RqtAddPromoter", "description after updation", image[0], image[1], image[2]));
                }else {
                    b.putString(AppsConstant.PARAMS, ParameterBuilder.getPromoterUpdate(promoter.getRequestId(), promoter.getRequestType(), firstName.getText().toString(),
                            lastName.getText().toString(), phone.getText().toString(), Address.getText().toString(), emailAddress.getText().toString(), storeId + "", "ReqSubmitted",
                            "RqtAddPromoter", "description after updation", photo,adhar,address));
                }getActivity().getLoaderManager().initLoader(LoaderConstant.UPDATE_PROMOTER,b,EditPromoterFragment.this);
                    }
        });

        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch(id) {
            case  LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(),LoaderMethod.IMAGE_UPLOAD,args);
            case LoaderConstant.UPDATE_PROMOTER:
                return new LoaderServices(getContext(), LoaderMethod.UPDATE_PROMOTER,args);
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getContext(),LoaderMethod.STORE_LIST,args);
            default:
                return null;
        }
    }

    private boolean isFieldExecutive() {
        if(preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExecutiveOnPremise") ||
                preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExecutiveOffPremise") ) {
           return true;
        } else {
            return false;
        }


    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        switch (loader.getId()) {
            case LoaderConstant.STORE_LIST:
            if (data != null && data instanceof ArrayList) {
                list = (ArrayList<Store>) data;
            } else {
                Toast.makeText(getContext(),
                        "Error in response. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }

                break;
            case LoaderConstant.IMAGE_UPLOAD:
                if (data != null && data instanceof String) {
                    if(i == 1) {
                        //photo
                        image[0] = (String) data;
                    }else if(i == 2) {
                        //aadhar
                        image[1] = (String) data;
                    } else if(i == 3) {
                        //address
                        image[2] = (String) data;
                    } else {
                        //error
                        i = 0;
                    }
                    //  image[i] = (String) data;
                    Log.d("IMAGE", (String) data);

                } else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case LoaderConstant.UPDATE_PROMOTER:
                break;
        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
    private boolean isApproved() {
        String statusId = promoter.getStatusId();
        if(!TextUtils.isEmpty(statusId) && statusId.equalsIgnoreCase("ReqCompleted")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppsConstant.IMAGE_PHOTO) {
                String responseValue = data.getStringExtra("response");
                String purpose = data.getStringExtra("image_purpose");
                if (!responseValue.equals(null)) {
                    /*Toast.makeText(getContext(), responseValue, Toast.LENGTH_SHORT).show();
                    Log.d("path", responseValue);*/
                    i = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                    bundle.putString(AppsConstant.FILE, responseValue);
                    bundle.putString(AppsConstant.FILEPURPOSE,purpose);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,EditPromoterFragment.this);
                    ivPhoto.setImageResource(R.drawable.photo_tick);
                    ivPhoto.setEnabled(false);
                }
            } else if (requestCode == AppsConstant.IMAGE_AADHAR) {
                final String responseValue = data.getStringExtra("response");
                final String purpose = data.getStringExtra("image_purpose");
                if (!responseValue.equals(null)) {
                    /*Toast.makeText(getContext(), responseValue, Toast.LENGTH_SHORT).show();
                    Log.d("path", responseValue);*/
                    i = 2;
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                    bundle.putString(AppsConstant.FILE, responseValue);
                    bundle.putString(AppsConstant.FILEPURPOSE,purpose);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,EditPromoterFragment.this).forceLoad();
                    ivAadhar.setImageResource(R.drawable.aadhartick);
                    ivAadhar.setEnabled(false);
                }
            } else if (requestCode == AppsConstant.IMAGE_ADDRESS_PROOF) {
                final String responseValue = data.getStringExtra("response");
                final String purpose = data.getStringExtra("image_purpose");
                if (!responseValue.equals(null)) {
                    /*Toast.makeText(getContext(), responseValue, Toast.LENGTH_SHORT).show();
                    Log.d("path", responseValue);*/

                            Bundle bundle = new Bundle();
                            bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                            bundle.putString(AppsConstant.FILE, responseValue);
                            bundle.putString(AppsConstant.FILEPURPOSE,purpose);
                            getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,EditPromoterFragment.this).forceLoad();
                    i = 3;
                    //  image[2] = responseValue;
                    ivAddress.setImageResource(R.drawable.id_card_tick);
                    ivAddress.setEnabled(false);
                }
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivPhoto:
                if (!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
                    Intent i = new Intent(getActivity(), CameraActivity.class);
                    i.putExtra("camera_key",AppsConstant.FRONT_CAMREA_OPEN);
                    i.putExtra("purpose","ForPhoto");
                    startActivityForResult(i,AppsConstant.IMAGE_PHOTO);
                } else {
                    // just show previous image in new pop up
                }


                break;
            case R.id.ivAadhar:
                if (!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
                    Intent i2 = new Intent(getActivity(),CameraActivity.class);
                    i2.putExtra("camera_key",BACK_CAMREA_OPEN);
                    i2.putExtra("purpose","ForAadhar");
                    startActivityForResult(i2,AppsConstant.IMAGE_AADHAR);
                } else {

                }

                break;
            case R.id.ivAddressProof:
                if(!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
                    Intent i3 = new Intent(getActivity(),CameraActivity.class);
                    i3.putExtra("camera_key",BACK_CAMREA_OPEN);
                    i3.putExtra("purpose","ForAddressProof");
                    startActivityForResult(i3,AppsConstant.IMAGE_ADDRESS_PROOF);
                } else {

                }
                break;
        }
    }
}
