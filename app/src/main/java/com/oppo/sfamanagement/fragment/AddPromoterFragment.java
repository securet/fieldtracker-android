package com.oppo.sfamanagement.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.oppo.sfamanagement.AddPromoterCameraHandler;
import com.oppo.sfamanagement.CameraActivity;
import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class AddPromoterFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Object> {
    EditText fN,lN,ph,eAdd,address;
    public static final int FRONT_CAMREA_OPEN = 1;
    public static final int BACK_CAMREA_OPEN = 2;
    ImageView ivPhoto,ivAdhar,ivAddressProof;
    protected Preferences preferences;
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
        Button Add = (Button) view.findViewById(R.id.btPAdd);
        Button Cancel = (Button) view.findViewById(R.id.btAddPCancel);

//        String imagePath = getArguments().getString("server_imagepath");
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = fN.getText().toString();
                String lName = lN.getText().toString();
                String sPh = ph.getText().toString();
                String  email = eAdd.getText().toString();
                String sAdd = address.getText().toString();
                for (int i = 0 ;i < 3 ; i++) {
                    String photo = getArguments().getString("image_server_path");
                    image[i] = photo;
               }
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.ADD_PROMOTER));
                b.putString(AppsConstant.METHOD, AppsConstant.POST);
                b.putString(AppsConstant.PARAMS, ParameterBuilder.getAddPromoter("RqtAddPromoter",fName,lName,sPh,email,sAdd,
                        "100000","ReqSubmitted","RqtAddPromoter",image[0],image[1],image[2]));
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
        if(resultCode == 2){
            String responseValue = data.getStringExtra("response");
            if(!responseValue.equals(null) && !responseValue.equals("cancel")) {
                Toast.makeText(getContext(),responseValue,Toast.LENGTH_SHORT).show();
                Bitmap bmp = BitmapFactory.decodeFile(responseValue);
                ivPhoto.setImageBitmap(bmp);
                ivPhoto.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhoto:

                Bundle bundle = new Bundle();
                bundle.putInt("camera_key",FRONT_CAMREA_OPEN);
                bundle.putString("purpose","For Photo");
                Intent i = new Intent(getActivity(), AddPromoterCameraHandler.class);
                i.putExtra("camera_key",FRONT_CAMREA_OPEN);
                i.putExtra("purpose","For Photo");
                startActivityForResult(i,2);
                //((Activity) getActivity()).overridePendingTransition(0,0);
   //             Fragment fragment = new CameraFragment();

                break;
            case R.id.ivAadhar:
                Fragment fragment2 = new CameraFragment();
                FragmentManager fm2 = getFragmentManager();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("camera_key",BACK_CAMREA_OPEN);
                bundle2.putString("purpose","For Aadhar");
                fragment2.setArguments(bundle2);
                fm2.beginTransaction().replace(R.id.flMiddle,fragment2).addToBackStack(null).commit();
                fm2.executePendingTransactions();
                break;
            case R.id.ivAddressProof:
                Fragment fragment3 = new CameraFragment();
                FragmentManager fm3 = getFragmentManager();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("camera_key",BACK_CAMREA_OPEN);
                bundle3.putString("purpose","For Address Proof");
                fragment3.setArguments(bundle3);
                fm3.beginTransaction().replace(R.id.flMiddle,fragment3).addToBackStack(null).commit();
                fm3.executePendingTransactions();
                break;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.ADD_PROMOTER:
                return new LoaderServices(getContext(), LoaderMethod.ADD_PROMOTER,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);

        if (isAdded()) {
            getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
