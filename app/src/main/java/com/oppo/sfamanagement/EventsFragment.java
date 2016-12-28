package com.oppo.sfamanagement;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oppo.sfamanagement.adapter.ListViewHistoryAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.fragment.HistoryListTrackFragment;
import com.oppo.sfamanagement.model.HistoryNew;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object> {
	protected ListViewHistoryAdapter adapter;
	protected ListView listView;
	ArrayList<HistoryNew> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView) view.findViewById(R.id.expandableList);

		Bundle bundle = new Bundle();
		bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,"anand@securet.in","0","10"));
		bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
		getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST,bundle,EventsFragment.this);
		/*adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,arrayList());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);*/
	}

	/*	@Override
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
	*/

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			default:
				Fragment f = new HistoryListTrackFragment();
				FragmentManager fm = getFragmentManager();
                Bundle b = new Bundle();
                HistoryNew hn = list.get(position);
				System.out.println(hn.getHistoryChildren().size() + "size of corresponding position");
                b.putParcelable("sub_history",hn);
                b.putInt("position",position);
                f.setArguments(b);
				fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
				fm.executePendingTransactions();
		}
	}

/*	private class UserLoginListTask extends AsyncTask<String, Void, String> {
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
	}*/

	/*private List<HistorySublist> hardCodeData() {
		List<HistorySublist> arrayList = new ArrayList<>();
		HistorySublist a = new HistorySublist("11:32","Out of Location");
		HistorySublist b = new HistorySublist("11:56","In Location");
		HistorySublist c = new HistorySublist("03:01","Out Location");
		arrayList.add(a);
		arrayList.add(b);
		arrayList.add(c);
		return arrayList;
	}
	private HashMap<History,ArrayList<HistorySublist>> getData() {
		HashMap<History,ArrayList<HistorySublist>> hashMap = new HashMap<>();
		History a = new History("12-OCT-16","10:12am","10:12am","9h 10m");
		History b = new History("12-OCT-16","10:12am","10:12am","9h 10m");
		History c = new History("12-OCT-16","10:12am","10:12am","9h 10m");
		ArrayList<HistorySublist> lis = new ArrayList<>();

		HistorySublist p = new HistorySublist("10:45","Time In");
		HistorySublist q = new HistorySublist("11:32","Out of Location");
		HistorySublist r = new HistorySublist("11:56","In Location");
		HistorySublist s = new HistorySublist("03:01","Out Location");
		lis.add(p);
		lis.add(q);
		lis.add(r);
		lis.add(s);
		hashMap.put(a,lis);
		hashMap.put(b,lis);
		hashMap.put(c,lis);
		return hashMap;
	}*/



	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {
		((MainActivity)getActivity()).showHideProgressForLoder(false);
		switch (id) {
			case LoaderConstant.HISTORY_LIST:
				return new LoaderServices(getContext(), LoaderMethod.HISTORY_LIST,args);
			default:
				return null;
		}
	}
	/*String createdTimestamp = obj.getString("createdTimestamp");
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
								listData.setTimeIn(DateFormat.format(mFormat, mCalendar).toString());*/

	/*public void getDate (String timeStamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		try {
			date = dateFormat.parse(timeStamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(date.getTime());

	}*/
	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {
		((MainActivity)getActivity()).showHideProgressForLoder(true);
		if(data != null && data instanceof ArrayList) {
			list = (ArrayList<HistoryNew>) data;
			System.out.println(list.size()   +  "   After Loader");
            //System.out.println(list.get(7).getHistoryChildren().toString());
			adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {

	}
}
