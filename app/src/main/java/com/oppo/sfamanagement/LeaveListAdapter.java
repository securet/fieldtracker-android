package com.oppo.sfamanagement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oppo.sfamanagement.database.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LeaveListAdapter extends ArrayAdapter<Event> {
	protected Activity activity;
	protected ArrayList<Event> list;
	protected int textViewResourceId;

	public LeaveListAdapter(Activity activity, int textViewResourceId,
							ArrayList<Event> list) {
		super(activity, textViewResourceId, list);
		this.activity = activity;
		this.textViewResourceId = textViewResourceId;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Event getItem(Event position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(textViewResourceId, parent, false);
		}

		Event e = list.get(position);

		TextView tvDate = (TextView) rowView.findViewById(R.id.tvDate);
		TextView tvSite = (TextView) rowView.findViewById(R.id.tvSite);
		TextView tvDescription = (TextView) rowView.findViewById(R.id.tvDescription);
		TextView tvStatus = (TextView) rowView.findViewById(R.id.tvStatus);
		tvDate.setText(e.getDate());
		tvSite.setText(e.getPlaceName());
		tvDescription.setText(e.getTimeIn()+"\n"+e.getTimeOut());
		tvStatus.setText(e.getTime());

		return rowView;
	}

	static public String getDisplayDate(Calendar date) {
		String displayDate = "";
//		Calendar startDate = Calendar.getInstance();
//		startDate.set(Calendar.HOUR_OF_DAY, 0);
//		startDate.set(Calendar.MINUTE, 0);
//		startDate.set(Calendar.SECOND, 0);
//		Calendar endDate = Calendar.getInstance();
//		endDate.set(Calendar.HOUR_OF_DAY, 23);
//		endDate.set(Calendar.MINUTE, 59);
//		endDate.set(Calendar.SECOND, 59);
//		if (date.after(startDate) && date.before(endDate)) {
//			SimpleDateFormat format = new SimpleDateFormat("HH:mm",
//					Locale.getDefault());
//			displayDate = format.format(date.getTime());
//		} else {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
//					Locale.getDefault());
//			displayDate = format.format(date.getTime());
//		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.getDefault());
		displayDate = format.format(date.getTime());
		return displayDate;
	}
}
