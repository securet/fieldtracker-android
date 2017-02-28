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
import com.allsmart.fieldtracker.model.Manager;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 25-01-2017.
 */

public class AttendenceManagerAdapter extends ArrayAdapter{

    private Context context;
    private ArrayList<Manager> list;
    private int resourceId;

    public AttendenceManagerAdapter(Context context, int resource, ArrayList<Manager> list) {
        super(context, resource, list);
        this.context = context;
        this.resourceId = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Manager getItem(int position) {
        return list.get(position);
    }

    public void refresh(ArrayList<Manager> list) {
        this.list= list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(view == null) {
            view = inflater.inflate(resourceId,parent,false);
        }
        TextView agentName = (TextView) view.findViewById(R.id.tvAgentName);
        TextView storeName = (TextView) view.findViewById(R.id.tvStoreName);
        TextView status = (TextView) view.findViewById(R.id.tvStatus);
   //     storeName.setVisibility(View.GONE);
        status.setVisibility(View.INVISIBLE);

        Manager manager = getItem(position);
        agentName.setText(manager.getAgentName());
        storeName.setText(manager.getStoreName());
  //      status.setText(manager.getStatus());

        return view;
    }
}
