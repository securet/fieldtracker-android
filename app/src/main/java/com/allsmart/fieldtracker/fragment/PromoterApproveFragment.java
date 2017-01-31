package com.allsmart.fieldtracker.fragment;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.CameraActivity;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.customviews.CustomBuilder;
import com.allsmart.fieldtracker.model.PromoterApprovals;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Promoter;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.allsmart.fieldtracker.constants.AppsConstant.BACK_CAMREA_OPEN;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class PromoterApproveFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, View.OnClickListener {

    private Button edit,cancel;
    private EditText firstName,lastName,phone,emailAddress,Address;
    private FragmentManager fragmentManager;
    ImageView ivPhoto,ivAadhar,ivAddress;
    private TextView tvStore,tvSE;
    private ArrayList<Store> list;
    private String[] image = new String[3];
    private int i = 0;
    private PromoterApprovals promoter;
    private Preferences preferences;
    private int storeId;
    private LinearLayout llWeeklyOff;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promoter = getArguments().getParcelable("promoter_approve");
        preferences = new Preferences(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_promoter_fragment,container,false);
        edit = (Button) view.findViewById(R.id.btEPEdit);
        edit.setText("Approve");
        llWeeklyOff = (LinearLayout) view.findViewById(R.id.llWeeklyOff);
        cancel = (Button) view.findViewById(R.id.btEAddPCancel);
        cancel.setText("Reject");
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
        fragmentManager = getFragmentManager();



        final String photo  = promoter.getUserPhoto();
        final String adhar  = promoter.getAadharIdPath();
        final String address = promoter.getAddressIdPath();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.REJECT_PROMOTER));
                b.putString(AppsConstant.METHOD,AppsConstant.PUT);
                b.putString(AppsConstant.PARAMS,ParameterBuilder.getPromoterApprove(promoter.getRequestId()));
                getActivity().getLoaderManager().initLoader(LoaderConstant.APPROVE_PROMOTER,b,PromoterApproveFragment.this);
            }
        });
        firstName.setText(promoter.getFirstName());
        lastName.setText(promoter.getLastName());
        phone.setText(promoter.getPhoneNum());
        emailAddress.setText(promoter.getEmailAddress());
        Address.setText(promoter.getAddress());
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        phone.setEnabled(false);
        emailAddress.setEnabled(false);
        Address.setEnabled(false);
        tvStore.setEnabled(false);
        tvSE.setEnabled(false);




        System.out.println(photo + "     " + adhar + "           " + address);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(photo)).placeholder(R.drawable.photo).fit().into(ivPhoto);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(adhar)).placeholder(R.drawable.aadhar).fit().into(ivAadhar);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(address)).placeholder(R.drawable.id_card).fit().into(ivAddress);
        ivPhoto.setOnClickListener(this);
        ivAadhar.setOnClickListener(this);
        ivAddress.setOnClickListener(this);

        ivPhoto.setEnabled(false);
        ivAadhar.setEnabled(false);
        ivAddress.setEnabled(false);
        tvStore.setTag(new Store());
        /*if (!isApproved()) {
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.STORE_LIST));
            b.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST, b, PromoterApproveFragment.this).forceLoad();
        } else {*/
            Bundle b = new Bundle();
            b.putString(AppsConstant.URL, UrlBuilder.getStoreDetails(Services.STORE_DETAIL,promoter.getProductStoreId()));
            b.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_DETAIL, b, PromoterApproveFragment.this).forceLoad();
      //  }
        /*tvStore.setOnClickListener(new View.OnClickListener() {
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
        });*/
        //  if (!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.APPROVE_PROMOTER));
                b.putString(AppsConstant.METHOD,AppsConstant.PUT);
                b.putString(AppsConstant.PARAMS,ParameterBuilder.getPromoterApprove(promoter.getRequestId()));
                getActivity().getLoaderManager().initLoader(LoaderConstant.APPROVE_PROMOTER,b,PromoterApproveFragment.this);

            }
        });

        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).showHideProgressForLoder(false);
        }
        switch(id) {
            case LoaderConstant.STORE_DETAIL:
                return new LoaderServices(getContext(),LoaderMethod.STORE_DETAIL,args);
            case LoaderConstant.APPROVE_PROMOTER:
                return new LoaderServices(getContext(),LoaderMethod.APPROVE_PROMOTER,args);
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
        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
        }
        switch (loader.getId()) {

            case LoaderConstant.STORE_DETAIL:
                if (data != null && data instanceof String) {
                    String storeName = (String) data;
                    if(!TextUtils.isEmpty(storeName) && !storeName.equalsIgnoreCase("error")) {
                        tvStore.setText(storeName);
                        tvSE.setText(preferences.getString(Preferences.USERFULLNAME,""));
                    } else {
                        Toast.makeText(getContext(),
                                "Error in response. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case LoaderConstant.APPROVE_PROMOTER:
                if(data != null && data instanceof String) {
                    String message = (String) data;
                    if(!TextUtils.isEmpty(message) && !message.equalsIgnoreCase("error") && !message.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(),
                                message,
                                Toast.LENGTH_SHORT).show();
                    } else if(!TextUtils.isEmpty(message) && message.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(),
                                "Success",
                                Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getFragmentManager();
                        fm.popBackStackImmediate();
                    } else {
                        Toast.makeText(getContext(),
                                "Operation Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else  {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                break;

        }
        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
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
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.promoter_image_fragment);
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.ivPromoterImage);
                    Picasso.with(getContext()).load(UrlBuilder.getServerImage(promoter.getUserPhoto())).fit().into(imageView);
                }


                break;
            case R.id.ivAadhar:
                if (!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
                    Intent i2 = new Intent(getActivity(),CameraActivity.class);
                    i2.putExtra("camera_key",BACK_CAMREA_OPEN);
                    i2.putExtra("purpose","ForAadhar");
                    startActivityForResult(i2,AppsConstant.IMAGE_AADHAR);
                } else {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.promoter_image_fragment);
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.ivPromoterImage);
                    Picasso.with(getContext()).load(UrlBuilder.getServerImage(promoter.getAadharIdPath())).fit().into(imageView);
                }

                break;
            case R.id.ivAddressProof:
                if(!promoter.getStatusId().equals(null) && !promoter.getStatusId().equalsIgnoreCase("ReqCompleted")) {
                    Intent i3 = new Intent(getActivity(),CameraActivity.class);
                    i3.putExtra("camera_key",BACK_CAMREA_OPEN);
                    i3.putExtra("purpose","ForAddressProof");
                    startActivityForResult(i3,AppsConstant.IMAGE_ADDRESS_PROOF);
                } else {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.promoter_image_fragment);
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.ivPromoterImage);
                    Picasso.with(getContext()).load(UrlBuilder.getServerImage(promoter.getAddressIdPath())).fit().into(imageView);
                }
                break;
        }
    }
}
