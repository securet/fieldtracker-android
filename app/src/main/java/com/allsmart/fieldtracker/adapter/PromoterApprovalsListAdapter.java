package com.allsmart.fieldtracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.model.PromoterApprovals;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 31-01-2017.
 */

public class PromoterApprovalsListAdapter extends ArrayAdapter {
    private Context context;
    private int resourceId;
    private ArrayList<PromoterApprovals> list;

    public PromoterApprovalsListAdapter(Context context, int resource,ArrayList<PromoterApprovals> list) {
        super(context, resource, list);
        this.context = context;
        this.resourceId =resource;
        this.list =list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void refresh(ArrayList<PromoterApprovals> list) {
        this.list = list;
    }

    @Nullable
    @Override
    public PromoterApprovals getItem(int position) {
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
        PromoterApprovals promoter = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.tvPromoterItem);
        TextView storeName = (TextView) view.findViewById(R.id.pStoreName);
        TextView statusId = (TextView) view.findViewById(R.id.tvStatusId);

        name.setText(promoter.getFirstName() + " " + promoter.getLastName());
        //storeName.setText("Store Name");
        String status = promoter.getStatusId();
        if(status.equalsIgnoreCase("ReqRejected")) {
            statusId.setText("Rejected");
        } else if(status.equalsIgnoreCase("ReqSubmitted")) {
            statusId.setText("Pending");
        } else if(status.equalsIgnoreCase("ReqCompleted")) {
            statusId.setText("Approved");
        }

        return view;
    }
}
