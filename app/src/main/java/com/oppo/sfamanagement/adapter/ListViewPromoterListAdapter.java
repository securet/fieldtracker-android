package com.oppo.sfamanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.Promoter;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class ListViewPromoterListAdapter extends BaseAdapter {

    protected Activity activity;
    protected int resourceId;
    protected ArrayList<Promoter> list;

    public ListViewPromoterListAdapter(Activity activity, int resource, ArrayList<Promoter> list) {
        this.activity = activity;
        this.resourceId = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Promoter getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
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
        Promoter p = getItem(position);
        tvPromoter.setText(p.getFirstName() + " " + p.getLastName());
        return view;
    }

    public void refresh(ArrayList<Promoter> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
