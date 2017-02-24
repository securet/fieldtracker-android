package com.oppo.fieldtracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.model.LeaveRequisition;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 26-01-2017.
 */

public class LeaveRequisitionListAdapter  extends ArrayAdapter{

    private Context context;
    private int resourceId;
    private ArrayList<LeaveRequisition> list;

    public LeaveRequisitionListAdapter(Context context, int resource, ArrayList<LeaveRequisition> list) {
        super(context, resource, list);
        this.context = context;
        this.resourceId = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    public void refresh(ArrayList<LeaveRequisition> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Nullable
    @Override
    public LeaveRequisition getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(resourceId,parent,false);
        }
        LeaveRequisition leave = getItem(position);
        TextView tvDays = (TextView) view.findViewById(R.id.leaveDays);
        TextView tvFrom = (TextView) view.findViewById(R.id.leaveFrom);
        TextView tvTo = (TextView) view.findViewById(R.id.leaveTo);
        TextView tvStatus = (TextView) view.findViewById(R.id.leaveStatus);
        TextView tvAgentName = (TextView) view.findViewById(R.id.agentName);
        tvDays.setText(leave.getDays());
        tvFrom.setText("From: " +leave.getFromDate());
        tvTo.setText("To: " +leave.getToDate());
        tvStatus.setText(leave.getStatus());
        tvAgentName.setText(leave.getAgentName());
        return view;
    }
}
