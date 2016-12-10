package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.Promoter;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class EditPromoterFragment extends Fragment {

    private Button edit,cancel;
    private EditText firstName,lastName,phone,emailAddress,Address;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_promoter_fragment,container,false);
      //  edit = (Button) view.findViewById(R.id.btPromoterEdit);
      //  cancel = (Button) view.findViewById(R.id.btPromoterCancel);
        firstName = (EditText) view.findViewById(R.id.etPromoterFN);
        lastName = (EditText) view.findViewById(R.id.etPromoterLN);
        phone = (EditText) view.findViewById(R.id.etPromoterPh);
        emailAddress = (EditText) view.findViewById(R.id.etPromoterEA);
        Address = (EditText) view.findViewById(R.id.etPromoterAdd);
        Promoter promoter = getArguments().getParcelable("promoter");
        firstName.setText(promoter.getFirstName());
        lastName.setText(promoter.getLastName());
        phone.setText(promoter.getPhoneNum());
        emailAddress.setText(promoter.getEmailAddress());
        Address.setText(promoter.getAddress());
        return view;
    }
}
