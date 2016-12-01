package com.oppo.sfamanagement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.adapter.ListViewHistoryAdapter;
import com.oppo.sfamanagement.database.API;
import com.oppo.sfamanagement.database.Event;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.model.History;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventsFragment extends Fragment {
	protected ListViewHistoryAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,false);
		ArrayList<History> list = new ArrayList<History>();
		ListView listLv = (ListView) rootView.findViewById(R.id.list);
		adapter = new ListViewHistoryAdapter(getActivity(), R.layout.history_list_item, list);
		listLv.setAdapter(adapter);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	protected void loadData() {
		UserLoginListTask task = new UserLoginListTask();
		task.execute(new String[] {
				((MainActivity)getActivity()).myPrefs.getString("UserName", ""),
				((MainActivity)getActivity()).myPrefs.getString("Password", "") });
//		EventDataSource eds = new EventDataSource(getActivity());
//		ArrayList<Event> list = eds.getEvents();
//		eds.close();
//
//		adapter.clear();
//		adapter.addAll(list);
//		adapter.notifyDataSetChanged();
	}

	private class UserLoginListTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			((MainActivity)getActivity()).pd.show();
		};

		@Override
		protected String doInBackground(String... params) {
			String response = "";
			try {
				response = API.GetSitesListRest(params[0], params[1]);
			} catch (Exception e) {
				e.printStackTrace();
				response = "";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			((MainActivity)getActivity()).pd.dismiss();
			ArrayList<History> attendenceList = new ArrayList<>();
			if(API.DEBUG)
				System.out.println("The Message Is: " + result);
			if (!(result.equals("No Internet")) || !(result.equals(""))) {
				try {

					if(result.toString().contains("status") && (new JSONObject(result).getString("status").toString().equals("success")))
					{
						JSONObject data = new JSONObject(result);
						JSONObject list = data.getJSONObject("data");
						JSONArray objArray = list.getJSONArray("data");
						for(int i=0;i<objArray.length();i++)
						{
							History listData = new History();
							JSONObject obj = objArray.getJSONObject(i);
							String statusId = obj.getString("statusId");
							JSONObject serviceType = obj.getJSONObject("serviceType");
							String serviceTypeId = serviceType.getString("serviceTypeId");
							if(serviceTypeId.equalsIgnoreCase("16"))
							{
								JSONObject site = obj.getJSONObject("site");
							//	listData.setPlaceName(site.getString("name"));
								String createdTimestamp = obj.getString("createdTimestamp");
								SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
								Date timeIn = new Date();
								try
								{
									timeIn = simpleDateFormat.parse(createdTimestamp);
								}catch (Exception e){
									e.printStackTrace();
								}
								Calendar mCalendar = Calendar.getInstance();
								mCalendar.setTimeInMillis(timeIn.getTime());
								listData.setDate(DateFormat.format("dd-MMM-yy", mCalendar).toString());
								String mFormat = "hh:mm";
								listData.setTimeIn(DateFormat.format(mFormat, mCalendar).toString());
								if(statusId.equalsIgnoreCase("Closed"))
								{
									String lastUpdatedTimestamp = obj.getString("lastUpdatedTimestamp");
									Date timeOut = new Date();
									try
									{
										timeOut = simpleDateFormat.parse(lastUpdatedTimestamp);
									}catch (Exception e){
										e.printStackTrace();
									}
									mCalendar.setTimeInMillis(timeOut.getTime());
									listData.setTimeOut(DateFormat.format(mFormat, mCalendar).toString());
									long millis = timeOut.getTime()- timeIn.getTime();
									listData.setTime(String.format("%02dh %02dm", TimeUnit.MILLISECONDS.toHours(millis),
											TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
								}else{
									listData.setTimeOut("-");
									listData.setTime("-");
								}
								attendenceList.add(listData);
							}
						}
					}else{
						Toast.makeText(getActivity(), ""+new JSONObject(result).getString("messages").toString(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					if(API.DEBUG){
						e.printStackTrace();
					}
					Toast.makeText(getActivity(),"Error in response. Please try again.",Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(),"Error in response. Please try again.",Toast.LENGTH_SHORT).show();
			}
			adapter.clear();
			adapter.addAll(attendenceList);
			adapter.notifyDataSetChanged();
		}

	}

	private String getDate(String input){
		//String input = "23/12/2014 10:22:12 PM";
		//Format of the date defined in the input String
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		//Desired format: 24 hour format: Change the pattern as per the need
		SimpleDateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date date = null;
		String output = null;
		try{
			//Converting the input String to Date
			date= df.parse(input);
			//Changing the format of date and storing it in String
			output = outputformat.format(date);
			//Displaying the date
			System.out.println(output);
		}catch(Exception pe){
			pe.printStackTrace();
		}
		return  output;
	}
}
