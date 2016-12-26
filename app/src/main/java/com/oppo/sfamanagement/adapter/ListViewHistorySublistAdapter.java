package com.oppo.sfamanagement.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.HistoryChild;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 26-12-2016.
 */

public class ListViewHistorySublistAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<HistoryChild> list;
    public ListViewHistorySublistAdapter(Context context, int resource,ArrayList<HistoryChild> list) {
        super(context, resource,list);
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Nullable
    @Override
    public HistoryChild getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.history_tracking_item,parent,false);
        }
        HistoryChild c = list.get(position);
        TextView textView = (TextView) view.findViewById(R.id.tvTime);
        TextView tvColor = (TextView) view.findViewById(R.id.tvColor);
        if(position == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor.setBackground(getContext().getResources().getDrawable(R.drawable.history_time_in_element,null));
            }
        }
        else if (position == list.size() - 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor.setBackground(getContext().getResources().getDrawable(R.drawable.history_tracking_element_gray,null));
            }
        }
        else if(position % 2 == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor.setBackground(getContext().getResources().getDrawable(R.drawable.history_tracking_element_green,null));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor.setBackground(getContext().getResources().getDrawable(R.drawable.history_time_in_element_red,null));
            }
        }
        textView.setText(c.getFromDate());
        return view;
    }
}
