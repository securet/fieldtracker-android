package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

/**
 * Created by allsmartlt218 on 10-12-2016.
 */

public class EditPromoterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    private Button edit,cancel;
    private EditText firstName,lastName,phone,emailAddress,Address;
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
        Address = (EditText) view.findViewById(R.id.etPromoterAdd);
        final Promoter promoter = getArguments().getParcelable("promoter");
        firstName.setText(promoter.getFirstName());
        lastName.setText(promoter.getLastName());
        phone.setText(promoter.getPhoneNum());
        emailAddress.setText(promoter.getEmailAddress());
        Address.setText(promoter.getAddress());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(AppsConstant.URL,UrlBuilder.getUpdatePromoter(Services.UPDATE_PROMOTER,promoter.getRequestId()));
                b.putString(AppsConstant.METHOD,AppsConstant.PUT);
                b.putString(AppsConstant.PARAMS,ParameterBuilder.getPromoterUpdate(promoter.getRequestId(),promoter.getRequestType(),firstName.getText().toString(),
                        lastName.getText().toString(),phone.getText().toString(),Address.getText().toString(),emailAddress.getText().toString(),"100000","ReqSubmitted",
                        "RqtAddPromoter","description after updation","test_JxpUD2_1479136438674.png","test_JxpUD2_1479136438674.png","test_JxpUD2_1479136438674.png"));
                getActivity().getLoaderManager().initLoader(LoaderConstant.UPDATE_PROMOTER,b,EditPromoterFragment.this);
            }
        });


        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch(id) {
            case LoaderConstant.UPDATE_PROMOTER:
                return new LoaderServices(getContext(), LoaderMethod.UPDATE_PROMOTER,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
