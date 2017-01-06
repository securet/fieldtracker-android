package com.oppo.sfamanagement.fragment;

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

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Promoter;

/**
 * Created by allsmartlt218 on 04-01-2017.
 */

public class ChangePasswordFragment extends Fragment {

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
                                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
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
}
