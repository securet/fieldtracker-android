package com.oppo.sfamanagement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.oppo.sfamanagement.database.CustomBuilder;
import com.oppo.sfamanagement.database.Event;
import com.oppo.sfamanagement.database.MultipartUtility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
	protected LeaveListAdapter adapter;
	TextView tvReason,tvFromDate,tvTodate;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_leaves, container,false);

		tvReason = (TextView) rootView.findViewById(R.id.tvReason);
		tvFromDate = (TextView) rootView.findViewById(R.id.tvFromDate);
		tvTodate = (TextView) rootView.findViewById(R.id.tvTodate);
		Button btnSave = (Button) rootView.findViewById(R.id.btnSave);
		tvReason.setTag("");
		tvReason.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ArrayList<String> list = new ArrayList<String>();
				list.add("Casual ");
				list.add("Sick");
				list.add("Privilege");
				CustomBuilder builder = new CustomBuilder(getActivity(), "Select Reason", true);
				// Hide Search Bar
				builder.setSingleChoiceItems(list, tvReason.getTag(), new CustomBuilder.OnClickListener()
				{
					@Override
					public void onClick(CustomBuilder builder, Object selectedObject)
					{
						tvReason.setTag((String)selectedObject);
						tvReason.setText((String)selectedObject);
						builder.dismiss();
					}
				},true);


				builder.setCancelListener(new CustomBuilder.OnCancelListener()
				{
					@Override
					public void onCancel()
					{
					}
				});

				builder.show();
			}
		});
		tvFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isFrom = true;
				Calendar now = Calendar.getInstance();
				DatePickerDialog dpd = DatePickerDialog.newInstance(
						LeaveFragment.this,
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH) - 1
				);
				dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
				dpd.vibrate(true);
				dpd.show(getActivity().getFragmentManager() , "Datepickerdialog");
				dpd.setTitle("Select From Date");
				dpd.setMinDate(now);
			}
		});
		tvTodate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if(!TextUtils.isEmpty(tvFromDate.getText().toString())) {
					isFrom=false;
					Calendar now = Calendar.getInstance();
					now.set(Calendar.YEAR,fromyear);
					now.set(Calendar.MONTH,frommonth);
					now.set(Calendar.DAY_OF_MONTH,fromDay);
					DatePickerDialog dpd = DatePickerDialog.newInstance(
							LeaveFragment.this,
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH) - 1
					);
					dpd.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
					dpd.vibrate(true);
					dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
					dpd.setTitle("Select To Date");
					dpd.setMinDate(now);
				}else{
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
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(TextUtils.isEmpty(tvFromDate.getText().toString())) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					dialog.setMessage("Please select from Date.");
					dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO Auto-generated method stub
						}
					});
					dialog.show();
				}else if(TextUtils.isEmpty(tvTodate.getText().toString())) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					dialog.setMessage("Please select to Date.");
					dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO Auto-generated method stub
						}
					});
					dialog.show();
				}else if(TextUtils.isEmpty(tvReason.getText().toString())) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					dialog.setMessage("Please select reason.");
					dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO Auto-generated method stub
						}
					});
					dialog.show();
				}else{
					strDesc = tvFromDate.getText().toString()+"-"+tvTodate.getText().toString();
					if(tvReason.getText().toString().equalsIgnoreCase("Privilege")){
						strType = 32;
					}else if(tvReason.getText().toString().equalsIgnoreCase("Sick")){
						strType =31;
					}else{
						strType = 30;
					}
				}

			}
		});

		ArrayList<Event> list = new ArrayList<Event>();
		ListView listLv = (ListView) rootView.findViewById(R.id.list);
		adapter = new LeaveListAdapter(getActivity(), R.layout.item_leave, list);
		listLv.setAdapter(adapter);
		return rootView;


	}
	String strDesc ="";
	int strType = 30;

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
			tvFromDate.setText(selDate);
			tvTodate.setText("");
		}else{
			monthOfYear = monthOfYear + 1;
			String selDate = (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth) + "-" + (monthOfYear < 10 ? "0" + (monthOfYear) : monthOfYear) + "-" + year;
			tvTodate.setText(selDate);
		}
	}


	@Override
	public void onResume() {
		super.onResume();
	}






	private File getOutputPhotoFile()
	{
		File directory = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), getContext().getPackageName());
		if (!directory.exists())
		{
			if (!directory.mkdirs())
			{
				return null;
			}
		}

		return new File(directory.getPath() + File.separator + "IMG_"+ System.currentTimeMillis() + ".jpg");
	}
}
