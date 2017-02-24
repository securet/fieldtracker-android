package com.oppo.fieldtracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.model.Leave;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class ListViewLeaveStatusListAdapter extends ArrayAdapter<Leave> {
    protected Activity activity;
    protected int resourceId;
    protected ArrayList<Leave> list;

    public ListViewLeaveStatusListAdapter(Activity activity, int resource, ArrayList<Leave> list) {
        super(activity,resource);
        this.activity = activity;
        this.resourceId = resource;
        this.list = list;
    }
    public void refresh(ArrayList<Leave> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public Leave getItem(Leave position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        if(rowView == null) {
            rowView = inflater.inflate(resourceId,parent,false);
        }
        Leave leave = list.get(position);
        TextView tvDays = (TextView) rowView.findViewById(R.id.tvLeaveDays);
        TextView tvFrom = (TextView) rowView.findViewById(R.id.tvLeaveFrom);
        TextView tvTo = (TextView) rowView.findViewById(R.id.tvLeaveTo);
        TextView tvStatus = (TextView) rowView.findViewById(R.id.tvLeaveStatus);
        TextView tvReason = (TextView) rowView.findViewById(R.id.tvLeaveReason);
        tvDays.setText(leave.getDays());
        tvFrom.setText("From: " + leave.getFromDate());
        tvTo.setText("To: " + leave.getToDate());
        tvStatus.setText(leave.getStatus());
        tvReason.setText(leave.getReasonType());
        return rowView;
    }
}
