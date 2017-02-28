package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.allsmart.fieldtracker.model.LeaveRequisition;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.utils.CalenderUtils;
import com.allsmart.fieldtracker.model.LeaveReason;
import com.allsmart.fieldtracker.model.LeaveType;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.ParameterBuilder;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 09-01-2017.
 */

public class ApproveLeaveFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    private TextView etStart,etEnd,etType,tvReasonType,tvDays, tvEditLeave;
    private ImageView ivStart,ivEnd;
    private Button submit,cancel,Approve,Reject;
    private LeaveRequisition leaveRequisition;
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
        etComments = (EditText) view.findViewById(R.id.etComments);

        cancel = (Button) view.findViewById(R.id.btnCancel);
        tvDays = (TextView) view.findViewById(R.id.tvLeaveDays);
        tvReasonType = (TextView) view.findViewById(R.id.etTypeReason);
        Approve = (Button) view.findViewById(R.id.btApprove);
        Reject = (Button) view.findViewById(R.id.btnReject);
        tvEditLeave = (TextView) view.findViewById(R.id.tvEditLeave);

        etStart.setEnabled(false);
        etEnd.setEnabled(false);
        etReason.setEnabled(false);
        etType.setEnabled(false);
        ivStart.setEnabled(false);
        ivEnd.setEnabled(false);
        tvReasonType.setEnabled(false);

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
                /*Toast.makeText(getContext(),"Approve Clicked",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();*/
                String comments = etComments.getText().toString();
                if (!TextUtils.isEmpty(comments)) {
                    String fDate = CalenderUtils.getYearMonthDashedFormate(etStart.getText().toString());
                    String tDate = CalenderUtils.getYearMonthDashedFormate(etEnd.getText().toString());
                    if(leaveRequisition != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppsConstant.METHOD,AppsConstant.PUT);
                        bundle.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.APPROVE_LEAVE));
                        bundle.putString(AppsConstant.PARAMS,ParameterBuilder.getLeaveApprove(leaveRequisition.getPartyRelationShipId(),
                                fDate,tDate,getEnumType(etType.getText().toString()),getReaonType(tvReasonType.getText().toString()),"Y",comments));
                        getActivity().getLoaderManager().initLoader(LoaderConstant.APPROVE_LEAVE,bundle,ApproveLeaveFragment.this);
                    } else {
                        ((MainActivity)getActivity()).displayMessage("Something went wrong try again later");
                    }
                } else {
                    ((MainActivity)getActivity()).displayMessage("Please enter comments");
                }
            }
        });

        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comments = etComments.getText().toString();
                if (!TextUtils.isEmpty(comments)) {
                    String fDate = CalenderUtils.getYearMonthDashedFormate(etStart.getText().toString());
                    String tDate = CalenderUtils.getYearMonthDashedFormate(etEnd.getText().toString());
                    if(leaveRequisition != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppsConstant.METHOD,AppsConstant.PUT);
                        bundle.putString(AppsConstant.URL,UrlBuilder.getUrl(Services.APPROVE_LEAVE));
                        bundle.putString(AppsConstant.PARAMS,ParameterBuilder.getLeaveApprove(leaveRequisition.getPartyRelationShipId(),
                                fDate,tDate,getEnumType(etType.getText().toString()),getReaonType(tvReasonType.getText().toString()),"N",comments));
                        getActivity().getLoaderManager().initLoader(LoaderConstant.APPROVE_LEAVE,bundle,ApproveLeaveFragment.this);
                    } else {
                        ((MainActivity)getActivity()).displayMessage("Something went wrong try again later");
                    }
                } else {
                    ((MainActivity)getActivity()).displayMessage("Please enter comments");
                }
            }
        });

        final LeaveRequisition leave = getArguments().getParcelable("leave_requisition_key");
        if(null != leave) {
            leaveRequisition = leave;
            etStart.setText(leave.getFromDate());
            etEnd.setText(leave.getToDate());
            tvDays.setText(leave.getDays().replaceAll("[^0-9]", ""));
            etReason.setText(leave.getReason());
            etType.setText(leave.getEnumType());
            tvReasonType.setText(leave.getReasonType());
        }

        return view;
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


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.APPROVE_LEAVE:
                return new LoaderServices(getContext(), LoaderMethod.APPROVE_LEAVE,args);
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

            case LoaderConstant.APPROVE_LEAVE:
                if(data != null && data instanceof String) {
                    if(((String) data).equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(),
                                "Leave Applied successfully",
                                Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                    } else if(!((String) data).equalsIgnoreCase("error") && !((String) data).equalsIgnoreCase("success")) {
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

}
