package com.allsmart.fieldtracker.fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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

import com.allsmart.fieldtracker.activity.CameraActivity;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.customviews.CustomBuilder;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.Store;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.allsmart.fieldtracker.constants.AppsConstant.BACK_CAMREA_OPEN;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddPromoterFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Object> {
    private EditText fN,lN,ph,eAdd,address;
    private ImageView ivPhoto,ivAdhar,ivAddressProof;
    private TextView tvStoreAssignment,seAssignment;
    private String storeId ;
    private Preferences preferences;
    ArrayList<Store> list;
    private int i = 0;
    String image[] = new String[3];
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
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
        tvStoreAssignment = (TextView)view.findViewById(R.id.tvStoreAssignment);
        seAssignment = (TextView) view.findViewById(R.id.tvSEAssignment);
        Button Add = (Button) view.findViewById(R.id.btPAdd);
        Button Cancel = (Button) view.findViewById(R.id.btAddPCancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                ((MainActivity)getActivity()).displayMessage("Cancel is clicked");
            }
        });
        tvStoreAssignment.setTag(new Store());


        Bundle b = new Bundle();
        b.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.STORE_LIST));
        b.putString(AppsConstant.METHOD,AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.STORE_LIST,b,AddPromoterFragment.this).forceLoad();

        tvStoreAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomBuilder builder = new CustomBuilder(getContext(),"Select Store",true);
                builder.setSingleChoiceItems(list,tvStoreAssignment.getTag(), new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        tvStoreAssignment.setTag(selectedObject);
                        tvStoreAssignment.setText(((Store) selectedObject).getStoreName());
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
                if(isNotNull(fN.getText().toString(),lN.getText().toString(),ph.getText().toString(),eAdd.getText().toString(),
                        address.getText().toString(),tvStoreAssignment.getText().toString(),seAssignment.getText().toString())) {
                    if(!TextUtils.isEmpty(image[0]) && !TextUtils.isEmpty(image[1]) && !TextUtils.isEmpty(image[2])) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.ADD_PROMOTER));
                        b.putString(AppsConstant.METHOD, AppsConstant.POST);
                        b.putString(AppsConstant.PARAMS, ParameterBuilder.getAddPromoter("RqtAddPromoter",AppsConstant.ORGANIZATIONID,"Yeh hai description",
                                fN.getText().toString(),
                                lN.getText().toString(),
                                ph.getText().toString(),
                                eAdd.getText().toString(),
                                address.getText().toString(),
                                storeId,"ReqSubmitted","RqtAddPromoter",image[0],image[1],image[2]));
                        getActivity().getLoaderManager().initLoader(LoaderConstant.ADD_PROMOTER,b,AddPromoterFragment.this).forceLoad();
                    } else {
                        ((MainActivity)getActivity()).displayMessage("Please take all photos");
                    }

                } else {
                    ((MainActivity)getActivity()).displayMessage("Fields cannot be empty");
                }


            }
        });
        ivPhoto.setOnClickListener(this);
        ivAdhar.setOnClickListener(this);
        ivAddressProof.setOnClickListener(this);

        return view;
    }

    public boolean isNotNull(String firstName,String lastName, String phone,String email,String address,String storeAssign,String sEassign) {
        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) &&
                !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(address) && !TextUtils.isEmpty(storeAssign) &&
                !TextUtils.isEmpty(sEassign)){
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
                    i = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                    bundle.putString(AppsConstant.FILE, responseValue);
                    bundle.putString(AppsConstant.FILEPURPOSE,purpose);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD,bundle,AddPromoterFragment.this).forceLoad();

                //    ivPhoto.setImageResource(R.drawable.photo_tick);
               //     ivPhoto.setEnabled(false);
                }
            } else if (requestCode == AppsConstant.IMAGE_AADHAR) {
                final String responseValue = data.getStringExtra("response");
                final String purpose = data.getStringExtra("image_purpose");
                if (!responseValue.equals(null)) {
                    i = 2;
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                    bundle.putString(AppsConstant.FILE, responseValue);
                    bundle.putString(AppsConstant.FILEPURPOSE, purpose);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD, bundle, AddPromoterFragment.this).forceLoad();
                 //   ivAdhar.setImageResource(R.drawable.aadhartick);
                  //  ivAdhar.setEnabled(false);
                }
            } else if (requestCode == AppsConstant.IMAGE_ADDRESS_PROOF) {
                final String responseValue = data.getStringExtra("response");
                final String purpose = data.getStringExtra("image_purpose");
                if (!responseValue.equals(null)) {
                    i = 3;
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, Services.DomainUrlImage);
                    bundle.putString(AppsConstant.FILE, responseValue);
                    bundle.putString(AppsConstant.FILEPURPOSE, purpose);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.IMAGE_UPLOAD, bundle, AddPromoterFragment.this).forceLoad();

               //     ivAddressProof.setImageResource(R.drawable.id_card_tick);
               //     ivAddressProof.setEnabled(false);
                }
            }
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
    private void askStoragePermission() {
        this.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },REQ_Storage_PERMISSION);
    }
    private void askCameraPermission() {
        this.requestPermissions(new String[] { Manifest.permission.CAMERA },REQ_CAMERA_PERMISSION);
    }
    private final static int REQ_CAMERA =1003;
    private final static int REQ_PERMISSION =1001;
    private final static int REQ_CAMERA_PERMISSION =1002;
    private final static int REQ_Storage_PERMISSION =1000;

    // Asks for permission
    private void askPermission() {
        this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },REQ_PERMISSION);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhoto:
                if(checkCameraPermission()){
                    if(checkStoragePermission())
                        OpenCamera(AppsConstant.FRONT_CAMREA_OPEN,AppsConstant.IMAGE_PHOTO,"ForPhoto");
                    else
                        askStoragePermission();
                }else{
                    askCameraPermission();
                }




                break;
            case R.id.ivAadhar:
                if(checkCameraPermission()){
                    if(checkStoragePermission())
                        OpenCamera(BACK_CAMREA_OPEN,AppsConstant.IMAGE_AADHAR,"ForAadhar");
                    else
                        askStoragePermission();
                }else{
                    askCameraPermission();
                }
                /*Intent i2 = new Intent(getActivity(),CameraActivity.class);
                i2.putExtra("camera_key",BACK_CAMREA_OPEN);
                i2.putExtra("purpose","ForAadhar");
                startActivityForResult(i2,AppsConstant.IMAGE_AADHAR);*/
                break;
            case R.id.ivAddressProof:
                if(checkCameraPermission()){
                    if(checkStoragePermission())
                        OpenCamera(BACK_CAMREA_OPEN,AppsConstant.IMAGE_ADDRESS_PROOF,"ForAddressProof");
                    else
                        askStoragePermission();
                }else{
                    askCameraPermission();
                }
                /*Intent i3 = new Intent(getActivity(),CameraActivity.class);
                i3.putExtra("camera_key",BACK_CAMREA_OPEN);
                i3.putExtra("purpose","ForAddressProof");
                startActivityForResult(i3,AppsConstant.IMAGE_ADDRESS_PROOF);*/
                break;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }switch (id) {
            case LoaderConstant.ADD_PROMOTER:
                return new LoaderServices(getContext(), LoaderMethod.ADD_PROMOTER,args);
            case LoaderConstant.STORE_LIST:
                return new LoaderServices(getContext(),LoaderMethod.STORE_LIST,args);
            case LoaderConstant.IMAGE_UPLOAD:
                return new LoaderServices(getContext(),LoaderMethod.IMAGE_UPLOAD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
        }switch (loader.getId()) {
            case LoaderConstant.STORE_LIST:
                if(data!=null && data instanceof ArrayList ){
                    list = (ArrayList<Store>) data;
                }
                else {

                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case LoaderConstant.IMAGE_UPLOAD:
                if (data != null && data instanceof String) {
                    String result = (String) data;
                    if(!TextUtils.isEmpty(result) && !result.equalsIgnoreCase("error")) {
                        if(i == 1) {
                            //photo
                            image[0] = (String) data;
                            ivPhoto.setImageResource(R.drawable.photo_tick);
                            ivPhoto.setEnabled(false);
                        }else if(i == 2) {
                            //aadhar
                            image[1] = (String) data;
                            ivAdhar.setImageResource(R.drawable.aadhartick);
                            ivAdhar.setEnabled(false);
                        } else if(i == 3) {
                            //address
                            image[2] = (String) data;
                            ivAddressProof.setImageResource(R.drawable.id_card_tick);
                            ivAddressProof.setEnabled(false);
                        } else {
                            //error
                            Toast.makeText(getContext(),
                                    "Failed to upload. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            i = 0;
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "Error in response. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                  //  image[i] = (String) data;
                    Log.d("IMAGE", (String) data);
                   // i++;


                } else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case LoaderConstant.ADD_PROMOTER:
                if (data != null && data instanceof String) {

                    String result = (String) data;
                    if(!TextUtils.isEmpty(result) && !result.equalsIgnoreCase("success") && !result.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(),
                                result,
                                Toast.LENGTH_SHORT).show();
                    } else if(!TextUtils.isEmpty(result) && result.equalsIgnoreCase("success") && !result.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(),
                                "Added Successfully",
                                Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                    } else {
                        Toast.makeText(getContext(),
                                "Failed to Add promoter",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                /*if(data.equals("success")) {
                    Toast.makeText(getContext(),
                            "Promoter Added Successfully",
                            Toast.LENGTH_SHORT).show();
                    Fragment fragment = new PromotersFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.flMiddle,fragment).commit();
                    fm.executePendingTransactions();
                } else {
                    Toast.makeText(getContext(),
                            "Failed to upload",
                            Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
            if(getActivity() != null && getActivity() instanceof MainActivity) {
                getActivity().getLoaderManager().destroyLoader(loader.getId());
            }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
