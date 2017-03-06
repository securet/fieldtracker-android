package com.allsmart.fieldtracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.model.Store;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class ListViewStoreListAdapter extends BaseAdapter {

    protected ArrayList<Store> list;
    protected Activity activity;
    protected int resourceId;
    public ListViewStoreListAdapter(Activity activity, int resource, ArrayList<Store> list) {
        this.list = list;
        this.activity = activity;
        this.resourceId = resource;
    }
    public void refresh( ArrayList<Store> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Store getItem(int position) {

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
        View rowView = convertView;
        if(rowView == null) {
            rowView = inflater.inflate(resourceId,parent,false);
        }
        TextView tvStoreList = (TextView)rowView.findViewById(R.id.tvStoreItem);
        RelativeLayout layout = (RelativeLayout) rowView.findViewById(R.id.rlStoreItem);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivErrorIcon);
        Store store = getItem(position);
        if(store != null) {
            tvStoreList.setText(store.getStoreName());
            if("Y" == store.getIsUpdated()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    layout.setBackgroundColor(activity.getResources().getColor(R.color.colorGreenStore,null));
                    //          imageView.setVisibility(View.VISIBLE);
                } else {
                    //          imageView.setVisibility(View.VISIBLE);
                    layout.setBackgroundColor(activity.getResources().getColor(R.color.colorGreenStore));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //        imageView.setVisibility(View.INVISIBLE);

                    layout.setBackgroundColor(activity.getResources().getColor(R.color.colorDimRed,null));
                } else {
                    //          imageView.setVisibility(View.INVISIBLE);
                    layout.setBackgroundColor(activity.getResources().getColor(R.color.colorDimRed));
                }
            }
        }

        return rowView;
    }

}
