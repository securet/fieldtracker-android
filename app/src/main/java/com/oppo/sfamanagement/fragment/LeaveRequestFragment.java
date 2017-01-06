package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oppo.sfamanagement.LeaveFragment;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.LeaveType;
import com.oppo.sfamanagement.model.Store;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.ParameterBuilder;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class LeaveRequestFragment extends Fragment implements View.OnClickListener , DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Object> {

    private TextView etStart,etEnd,etType;
    private ImageView ivStart,ivEnd;
    private Button submit,cancel;
    private ArrayList<LeaveType> leaveTypeList;

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
        submit = (Button) view.findViewById(R.id.btSubmit);
        cancel = (Button) view.findViewById(R.id.btnCancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDate = etStart.getText().toString();
                String thruDate = etEnd.getText().toString();
                String leaveTypeEnumId = etType.getText().toString();
                String leaveReasonId ;

                Bundle bundle = new Bundle();
                bundle.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.APPLY_LEAVES));
                bundle.putString(AppsConstant.METHOD, AppsConstant.POST);
                bundle.putString(AppsConstant.PARAMS,ParameterBuilder.getApplyLeave(leaveTypeEnumId,"leaveTypeEnumId","description",fromDate,thruDate,"OPPO_ORG"));
                getActivity().getLoaderManager().initLoader(LoaderConstant.APPLY_LEAVE,bundle,LeaveRequestFragment.this).forceLoad();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        etType.setOnClickListener(this);
        etStart.setOnClickListener(this);
        etEnd.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivEnd.setOnClickListener(this);
        etType.setTag(new LeaveType());

        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.LEAVE_TYPES));
        b.putString(AppsConstant.METHOD,AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.LEAVE_TYPES,b,LeaveRequestFragment.this).forceLoad();

        return view;
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
           builder.setSingleChoiceItems(leaveTypeList,etType.getTag(), new CustomBuilder.OnClickListener() {
               @Override
               public void onClick(CustomBuilder builder, Object selectedObject) {
                   etType.setTag(selectedObject);
                   etType.setText(((LeaveType) selectedObject).getDescription());

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

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoaderConstant.LEAVE_TYPES:
                return new LoaderServices(getContext(), LoaderMethod.LEAVE_TYPES,args);
            case LoaderConstant.APPLY_LEAVE:
                return new LoaderServices(getContext(), LoaderMethod.APLLY_LEAVES,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

        switch (loader.getId()) {
            case LoaderConstant.LEAVE_TYPES:
                if(data != null && data instanceof ArrayList) {

                } else {

                }
                leaveTypeList = (ArrayList<LeaveType>) data;

        }
        getActivity().getLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
