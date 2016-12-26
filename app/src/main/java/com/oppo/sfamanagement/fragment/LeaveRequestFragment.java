package com.oppo.sfamanagement.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppo.sfamanagement.LeaveFragment;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Store;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveRequestFragment extends Fragment implements View.OnClickListener , DatePickerDialog.OnDateSetListener {

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        etStart.setText(getArguments().getString("day") + "/" +getArguments().getString("month") + "/" + getArguments().getString("year"));
    }

    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.etStartDate || v.getId() == R.id.ivDatePicker) {
           isFrom = true;
           Calendar now = Calendar.getInstance();
           DatePickerDialog dpd = DatePickerDialog.newInstance(
                   this,
                   now.get(Calendar.YEAR),
                   now.get(Calendar.MONTH),
                   now.get(Calendar.DAY_OF_MONTH) - 1
           );
           dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
           dpd.vibrate(true);
           dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
           dpd.setTitle("Select From Date");
           dpd.setMinDate(now);

       } else if(v.getId() == R.id.etEndDate || v.getId() == R.id.ivDatePicker2) {
           if(!TextUtils.isEmpty(etEnd.getText().toString())) {
               isFrom=false;
               Calendar now = Calendar.getInstance();
               now.set(Calendar.YEAR,fromyear);
               now.set(Calendar.MONTH,frommonth);
               now.set(Calendar.DAY_OF_MONTH,fromDay);
               DatePickerDialog dpd = DatePickerDialog.newInstance(
                       this,
                       now.get(Calendar.YEAR),
                       now.get(Calendar.MONTH),
                       now.get(Calendar.DAY_OF_MONTH) - 1
               );
               dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
               dpd.vibrate(true);
               dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
               dpd.setTitle("Select To Date");
               dpd.setMinDate(now);
           }/*else{
               AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
               dialog.setMessage("Please select from Date.");
               dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                       // TODO Auto-generated method stub
                   }
               });
               dialog.show();
           }*/
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

    boolean isFrom;
    int fromyear,frommonth,fromDay;
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(isFrom){
            fromyear =year;
            frommonth =monthOfYear;
            fromDay =dayOfMonth;
            monthOfYear = monthOfYear + 1;
            String selDate = (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth) + "-" + (monthOfYear < 10 ? "0" + (monthOfYear) : monthOfYear) + "-" + year;
            etStart.setText(selDate);
            //etEnd.setText("");
        }else{
            monthOfYear = monthOfYear + 1;
            String selDate = (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth) + "-" + (monthOfYear < 10 ? "0" + (monthOfYear) : monthOfYear) + "-" + year;
            etEnd.setText(selDate);
        }
    }
}
