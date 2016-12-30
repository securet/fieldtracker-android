package com.oppo.sfamanagement.fragment;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.oppo.sfamanagement.R;

import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;
import com.squareup.picasso.Picasso;


/**
 * Created by allsmartlt218 on 29-12-2016.
 */

public class FieldExecutivePromoterFragment extends Fragment implements View.OnClickListener {

    private TextView firstName,lastName,phone,email,address,storeAssignment,seAssignment;
    private ImageView ivPhoto,ivAadhar,ivAddress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.field_executive_promoter_fragment,container,false);
        firstName = (TextView) view.findViewById(R.id.etPromoterFN);
        lastName = (TextView) view.findViewById(R.id.etPromoterLN);
        phone = (TextView) view.findViewById(R.id.etPromoterPh);
        email = (TextView) view.findViewById(R.id.etPromoterEA);
        address = (TextView) view.findViewById(R.id.etPromoterAdd);
        storeAssignment = (TextView) view.findViewById(R.id.tvStoreAssignment);
        seAssignment = (TextView) view.findViewById(R.id.tvSEAssignment);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        ivAadhar = (ImageView) view.findViewById(R.id.ivAadhar);
        ivAddress = (ImageView) view.findViewById(R.id.ivAddressProof);
        ivPhoto.setOnClickListener(this);
        ivAadhar.setOnClickListener(this);
        ivAddress.setOnClickListener(this);


        Promoter promoter = getArguments().getParcelable("promoter");
        firstName.setText(promoter.getFirstName());
        lastName.setText(promoter.getLastName());
        phone.setText(promoter.getPhoneNum());
        email.setText(promoter.getEmailAddress());
        address.setText(promoter.getAddress());
        String photo  = promoter.getUserPhoto();
        String adhar  = promoter.getAadharIdPath();
        String address = promoter.getAddressIdPath();
        /*ivPhoto.setImageBitmap(BitmapFactory.decodeFile(photo));
        ivAadhar.setImageBitmap(BitmapFactory.decodeFile(adhar));
        ivAddress.setImageBitmap(BitmapFactory.decodeFile(address));*/
        System.out.println(photo + "     " + adhar + "           " + address);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(photo)).placeholder(R.drawable.photo).fit().into(ivPhoto);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(adhar)).placeholder(R.drawable.aadhar).fit().into(ivAadhar);
        Picasso.with(getContext()).load(UrlBuilder.getServerImage(address)).placeholder(R.drawable.id_card).fit().into(ivAddress);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:

        }
    }

    public void showImage(String imagePath) {

    }
}
