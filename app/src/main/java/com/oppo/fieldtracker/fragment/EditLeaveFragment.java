package com.oppo.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

import com.crashlytics.android.Crashlytics;
import com.oppo.fieldtracker.activity.MainActivity;
import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.constants.AppsConstant;
import com.oppo.fieldtracker.utils.CalenderUtils;
import com.oppo.fieldtracker.customviews.CustomBuilder;
import com.oppo.fieldtracker.utils.Logger;
import com.oppo.fieldtracker.model.Leave;
import com.oppo.fieldtracker.model.LeaveReason;
import com.oppo.fieldtracker.model.LeaveReasonApply;
import com.oppo.fieldtracker.model.LeaveType;
import com.oppo.fieldtracker.constants.LoaderConstant;
import com.oppo.fieldtracker.constants.LoaderMethod;
import com.oppo.fieldtracker.service.LoaderServices;
import com.oppo.fieldtracker.utils.ParameterBuilder;
import com.oppo.fieldtracker.constants.Services;
import com.oppo.fieldtracker.utils.UrlBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by allsmartlt218 on 09-01-2017.
 */

public class EditLeaveFragment  extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Object>, DatePickerDialog.OnDateSetListener {

    private TextView etStart,etEnd,etType,tvReasonType,tvDays, tvEditLeave;
    private ImageView ivStart,ivEnd;
    private Button submit,cancel,Approve,Reject;
    private String enumTypeId="";
    private String enumReasonId="";
    private EditText etReason,etComments;
    private String leaveReasonId = "" ;
    private ArrayList<LeaveType> leaveTypeList;
    private ArrayList<LeaveReason> leaveReasonList;
    private boolean isFrom = false, isTo = false;
    private int fromyear = 0,frommonth = 0,fromDay = 0;
    private int[] month31 = {1,3,5,7,8,10,12};
    private int check = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_leave_fragment,container,false);
        etStart = (TextView) view.findViewById(R.id.etStartDate);
        etEnd = (TextView) view.findViewById(R.id.etEndDate);
        etReason = (EditText) view.findViewById(R.id.etReason);
        etType = (TextView) view.findViewById(R.id.etType);
        ivStart = (ImageView) view.findViewById(R.id.ivDatePicker);
        ivEnd = (ImageView) view.findViewById(R.id.ivDatePicker2);
        submit = (Button) view.findViewById(R.id.btSubmit);
        cancel = (Button) view.findViewById(R.id.btnCancel);
        etComments = (EditText) view.findViewById(R.id.etComments);
        etComments.setVisibility(View.GONE);
        tvDays = (TextView) view.findViewById(R.id.tvLeaveDays);
        tvReasonType = (TextView) view.findViewById(R.id.etTypeReason);
        Approve = (Button) view.findViewById(R.id.btApprove);
        Reject = (Button) view.findViewById(R.id.btnReject);
        tvEditLeave = (TextView) view.findViewById(R.id.tvEditLeave);

        if(((MainActivity)getActivity()).isManager()) {
            try {
                check = getArguments().getInt("leave_requisition");
            } catch (Exception e) {
                Log.d(MainActivity.TAG,e.getMessage());
            }
            if(check == 1) {
                showManagerViews();
            } else if(check == 2) {
                showFieldAgentViews();
            }
        } else {
            showFieldAgentViews();
        }
        Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Approve Clicked",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });

        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Reject Clicked",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });

        final Leave leave = getArguments().getParcelable("leave_key");
        if(null != leave) {
            etStart.setText(leave.getFromDate());
            etEnd.setText(leave.getToDate());
            tvDays.setText(leave.getDays().replaceAll("[^0-9]", ""));
            etReason.setText(leave.getReason());
            etType.setText(leave.getEnumType());
            tvReasonType.setText(leave.getReasonType());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDate = etStart.getText().toString();
                String thruDate = etEnd.getText().toString();
                Log.d(MainActivity.TAG,"before    "+"fromDate: " +fromDate    + "    thruDate: " +thruDate);


                if (!TextUtils.isEmpty(tvReasonType.getText().toString()) && !TextUtils.isEmpty(etType.getText().toString()) && !TextUtils.isEmpty(etReason.getText().toString()) && !TextUtils.isEmpty(leave.getPartyRelationShipId())) {
                    String fDate = CalenderUtils.getYearMonthDashedFormate(fromDate);
                    System.out.println(leave.getPartyRelationShipId());
                    String tDate = CalenderUtils.getYearMonthDashedFormate(thruDate);
                    Log.d(MainActivity.TAG,"fromDate: " +fDate    + "    thruDate: " +tDate);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.APPLY_LEAVES));
                    bundle.putString(AppsConstant.METHOD, AppsConstant.PUT);
                    if(TextUtils.isEmpty(enumTypeId) && TextUtils.isEmpty(enumReasonId)) {
                        bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getUpdateLeave(leave.getEnumTypeId(), leave.getReasonTypeId(), etReason.getText().toString(), fDate, tDate, leave.getPartyRelationShipId()));
                    } else {
                        if(TextUtils.isEmpty(enumTypeId) && !TextUtils.isEmpty(enumReasonId)) {
                            bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getUpdateLeave(leave.getEnumTypeId(),
                                    enumReasonId, etReason.getText().toString(), fDate, tDate, leave.getPartyRelationShipId()));
                        } else if(!TextUtils.isEmpty(enumTypeId) && TextUtils.isEmpty(enumReasonId)) {
                            bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getUpdateLeave(enumTypeId, leave.getReasonTypeId(),
                                    etReason.getText().toString(), fDate, tDate, leave.getPartyRelationShipId()));
                        } else {
                            bundle.putString(AppsConstant.PARAMS, ParameterBuilder.getUpdateLeave(enumTypeId, enumReasonId,
                                    etReason.getText().toString(), fDate, tDate, leave.getPartyRelationShipId()));
                        }
                    }
                    getActivity().getLoaderManager().initLoader(LoaderConstant.APPLY_LEAVE, bundle, EditLeaveFragment.this).forceLoad();
                } else {
                    ((MainActivity)getActivity()).displayMessage("Fields cannot be empty");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBuilder builder = new CustomBuilder(getContext(),"Select Leave Type",true);
                builder.setSingleChoiceItems(leaveTypeList,etType.getTag(), new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        etType.setTag(selectedObject);
                        etType.setText(((LeaveType) selectedObject).getTypeDescription());
                        enumTypeId = ((LeaveType) selectedObject).getEnumType();
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
        etStart.setOnClickListener(this);
        etEnd.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivEnd.setOnClickListener(this);
        etType.setTag(new LeaveType());
        tvReasonType.setTag(new LeaveReason());
        tvReasonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBuilder builder = new CustomBuilder(getContext(),"Select Reason",true);
                builder.setSingleChoiceItems(leaveReasonList,tvReasonType.getTag(), new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        tvReasonType.setTag(selectedObject);
                        tvReasonType.setText(((LeaveReason) selectedObject).getReasonDescription());
                        enumReasonId = ((LeaveReason) selectedObject).getEnumTypeReason();
                        leaveReasonId = tvReasonType.getText().toString();
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
        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.LEAVE_TYPES));
        b.putString(AppsConstant.METHOD,AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.LEAVE_TYPES,b,EditLeaveFragment.this).forceLoad();

        return view;
    }

    private void showManagerViews() {
        Approve.setVisibility(View.VISIBLE);
        Reject.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        tvEditLeave.setText("Leave Approval");
    }

    private void showFieldAgentViews() {
        Approve.setVisibility(View.INVISIBLE);
        Reject.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        tvEditLeave.setText("Edit Leave");
    }

    private String getEnumType(String enumType) {
        if(enumType.equals("Loss of Pay")){
            return "EltLossOfPay";
        } else if (enumType.equals("Holiday")) {
            return "EltHoliday";
        } else if(enumType.equals("Earned Leave")) {
            return "EltEarned";
        } else {
            return "EltSpecialDayOff";
        }
    }

    private String getReaonType(String reasonType) {
        if(reasonType.equals("Medical")) {
            return "ElrMedical";
        } else {
            return "ElrPersonal";
        }
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
        }
        if (v.getId() == R.id.etEndDate || v.getId() == R.id.ivDatePicker2) {
            if (isFrom) {
                if(!TextUtils.isEmpty(etEnd.getText().toString())) {
                    isFrom=false;
                    isTo = true;
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
                }
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Please select from Date.");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
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
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHideProgressForLoder(true);
        }
        switch (loader.getId()) {
            case LoaderConstant.LEAVE_TYPES:
                if(data != null && data instanceof ArrayList) {

                } else {

                }
                ArrayList<LeaveReasonApply> arrayList = (ArrayList<LeaveReasonApply>) data;
                LeaveReasonApply reasonApply = arrayList.get(0);
                leaveTypeList = reasonApply.getTypeList();
                leaveReasonList = reasonApply.getReasonList();
                break;
            case LoaderConstant.APPLY_LEAVE:
                if(data != null && data instanceof String) {
                    String result = (String) data;
                    if(result.equals("success")) {
                        Toast.makeText(getContext(),
                                "Leave Applied successfully",
                                Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getFragmentManager();
                        fm.popBackStack();
                    } else if(!result.equals("error") && !result.equals("success")) {
                        Toast.makeText(getContext(),
                                data.toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),
                                "Leave Apply failed",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Error in response. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                break;


        }
        if(getActivity() != null  && getActivity() instanceof  MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    private boolean isLeapYear(int year) {
        GregorianCalendar calender = new GregorianCalendar();
        if(calender.isLeapYear(year)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMonth31(int fromMonth) {
        for(int x : month31) {
            if(x == fromMonth) {
                return true;
            }
        }
        return false;
    }

    public String getDays(String fromDate, String thruDate) {
        if (fromDate.length() == thruDate.length() && !TextUtils.isEmpty(fromDate) && !TextUtils.isEmpty(thruDate) && fromDate.length() == 10) {
            int thru = 0, from = 0, year = 0;
            try {
                thru = Integer.parseInt(thruDate.substring(8, 10));
                from = Integer.parseInt(fromDate.substring(8, 10));
                year = Integer.parseInt(fromDate.substring(0, 4));
            } catch (Exception e) {
                Logger.e("Log", e);
                Crashlytics.log(1, getClass().getName(), "Error in Leave Request");
                Crashlytics.logException(e);
                Toast.makeText(getContext(), "Select Date Properly", Toast.LENGTH_SHORT).show();
            }
            if (thru != 0 && from != 0 && year != 0) {
                if (from == thru) {
                    return "1";
                } else if ((thru - from) < 0) {
                    int thruMonth = Integer.parseInt(thruDate.substring(5, 7));
                    int fromMonth = Integer.parseInt(fromDate.substring(5, 7));
                    if (thruMonth > fromMonth) {
                        if (isMonth31(fromMonth)) {
                            return (thru + (31 - from) + 1) + "";
                        } else if (!isMonth31(fromMonth)) {
                            return (thru + (30 - from) + 1) + "";
                        } else {
                            if (isLeapYear(year)) {
                                return (thru + (29 - from) + 1) + "";
                            } else {
                                return (thru + (28 - from) + 1) + "";
                            }
                        }
                    } else {
                        return "-";
                    }

                } else {
                    return ((thru - from) + 1) + "";
                }

            } else {
                return "-";
            }
        } else {
            Toast.makeText(getContext(), "Select Date Properly", Toast.LENGTH_SHORT).show();
            return "-";
        }
    }





    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(isFrom){
            fromyear =year;
            frommonth =monthOfYear;
            fromDay =dayOfMonth;
            monthOfYear = monthOfYear + 1;
            String selDate = (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth) + "/" + (monthOfYear < 10 ? "0" + (monthOfYear) : monthOfYear) + "/" + year;
            etStart.setText(selDate);
            //etEnd.setText("");
        }else{
            monthOfYear = monthOfYear + 1;
            String selDate =  (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth)+ "/" + (monthOfYear < 10 ? "0" + (monthOfYear) : monthOfYear) + "/" + year;
            etEnd.setText(selDate);
        }
        if(isTo) {
            String days = CalenderUtils.getDifferenceDate(CalenderUtils.getYearMonthDashedFormate(etStart.getText().toString()), CalenderUtils.getYearMonthDashedFormate(etEnd.getText().toString()));
            if(days.equals("-")) {
                ((MainActivity)getActivity()).displayMessage("Please select date properly");
            } else {
                tvDays.setText(days);
            }
        }
    }
}