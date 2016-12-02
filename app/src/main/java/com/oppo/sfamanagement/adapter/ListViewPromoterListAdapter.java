package com.oppo.sfamanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oppo.sfamanagement.R;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class ListViewPromoterListAdapter extends ArrayAdapter<String> {

    protected Activity activity;
    protected int resourceId;
    protected ArrayList<String> list;

    public ListViewPromoterListAdapter(Activity activity, int resource, ArrayList<String> list) {
        super(activity, resource, list);
        this.activity = activity;
        this.resourceId = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public String getItem(String position) {
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
        View view = convertView;
        if (view == null) {
           view = inflater.inflate(resourceId,parent,false);
        }
        TextView tvPromoter = (TextView) view.findViewById(R.id.tvPromoterItem);
        tvPromoter.setText(list.get(position));
        return view;
    }
}
