package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Store;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveRequestFragment extends Fragment implements View.OnClickListener {

    TextView etStart,etEnd,etType;
    ImageView ivStart,ivEnd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_request,container,false);
        etStart = (TextView) view.findViewById(R.id.etStartDate);
        etEnd = (TextView) view.findViewById(R.id.etEndDate);
        etType = (TextView) view.findViewById(R.id.etType);
        ivStart = (ImageView) view.findViewById(R.id.ivDatePicker);
        ivEnd = (ImageView) view.findViewById(R.id.ivDatePicker2);
        etType.setOnClickListener(this);
        etStart.setOnClickListener(this);
        etEnd.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivEnd.setOnClickListener(this);
        etType.setTag(new String());
        return view;
    }

    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Casual");
        list.add("Sick");
        list.add("Other");
        return list;
    }
    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.etStartDate || v.getId() == R.id.ivDatePicker) {
           Fragment fragment = new PickStartDateFragment();
           FragmentManager fm = getFragmentManager();
           fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
       } else if(v.getId() == R.id.etEndDate || v.getId() == R.id.ivDatePicker2) {
           Fragment fragment = new PickEndDateFragment();
           FragmentManager fm = getFragmentManager();
           fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
       } else if(v.getId() == R.id.etType) {
           CustomBuilder builder = new CustomBuilder(getContext(),"Select Leave Type",true);
           builder.setSingleChoiceItems(getData(),etType.getTag(), new CustomBuilder.OnClickListener() {
               @Override
               public void onClick(CustomBuilder builder, Object selectedObject) {
                   etType.setTag(selectedObject);
                   etType.setText(((String) selectedObject));

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
    }
}
