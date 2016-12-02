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
import com.oppo.sfamanagement.model.Store;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class ListViewStoreListAdapter extends ArrayAdapter<String> {

    protected ArrayList<String> list;
    protected Activity activity;
    protected int resourceId;
    public ListViewStoreListAdapter(Activity activity, int resource, ArrayList<String> list) {
        super(activity,resource,list);
        this.list = list;
        this.activity = activity;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    public Store getItem(Store position) {
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
      //  Store store = new Store();
        TextView tvStoreList = (TextView)rowView.findViewById(R.id.tvStoreItem);
        tvStoreList.setText(list.get(position));
        return rowView;
    }
}
