package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

/**
 * Created by allsmartlt218 on 04-01-2017.
 */

public class ChangePasswordFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    private EditText currentPass,newPass,confirmPass;
    private Button btChange;
    private boolean isCurrent = false;
    private boolean isNew = false;
    private boolean isConfirm = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password_fragment,container,false);
        currentPass = (EditText) view.findViewById(R.id.etCurrentPassword);
        newPass = (EditText) view.findViewById(R.id.etNewPassword);
        confirmPass = (EditText) view.findViewById(R.id.etConfirmPassword);
        btChange = (Button) view.findViewById(R.id.btChangePassword);

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    isConfirm = true;
                } else {
                    isConfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    isNew = true;
                } else {
                    isNew = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0) {
                    isCurrent = true;
                } else {
                    isCurrent = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nPass = newPass.getText().toString();
                String sConfirmPass = confirmPass.getText().toString();
                if (isCurrent) {
                    if (isNew) {
                        if (isConfirm) {
                            if (!TextUtils.isEmpty(nPass) && !TextUtils.isEmpty(sConfirmPass) && nPass.equals(sConfirmPass)) {
                            //    Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString(AppsConstant.METHOD ,AppsConstant.PUT);
                                bundle.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.CHANGE_PASSWORD));
                                bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getChangePassword(currentPass.getText().toString(),nPass,sConfirmPass));
                                getActivity().getLoaderManager().initLoader(LoaderConstant.CHANGE_PASSWORD,bundle,ChangePasswordFragment.this).forceLoad();
                            } else {
                                Toast.makeText(getContext(), "new and confirm Paswords mismatching", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Enter Old Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.CHANGE_PASSWORD:
                return new LoaderServices(getContext(), LoaderMethod.CHANGE_PASSWORD,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        if(data != null && data instanceof String) {
            if(!((String) data).equalsIgnoreCase("error") && ((String) data).equalsIgnoreCase("success")) {
                Toast.makeText(getContext(),
                        "Password changed successfully",
                        Toast.LENGTH_SHORT).show();
            } else if(!((String) data).equalsIgnoreCase("error")) {
                Toast.makeText(getContext(),
                        data.toString(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),
                        "Password change failed",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
