package com.oppo.sfamanagement.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.fragment.DynamicElement;
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
        HistoryChild c = getItem(position);
        TextView timeF = (TextView) view.findViewById(R.id.tvTimeFrom);
        TextView timeT = (TextView) view.findViewById(R.id.tvTimeThru);
        TextView tvColor1 = (TextView) view.findViewById(R.id.tvColor1);
        TextView tvLocationStatus1 = (TextView) view.findViewById(R.id.tvLocationStatus1);
        TextView tvLocationStatus2 = (TextView) view.findViewById(R.id.tvLocationStatus2);
        TextView tvColor2 = (TextView) view.findViewById(R.id.tvColor2);
        LinearLayout layoutFrom = (LinearLayout) view.findViewById(R.id.llFromDate);
        LinearLayout layoutThru = (LinearLayout) view.findViewById(R.id.llThruDate);
        if(position == 0) {
            Drawable blueBackground = null;
            Drawable redBackground = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blueBackground = context.getResources().getDrawable(R.drawable.history_time_in_element,null);
                redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red,null);
            } else {
                blueBackground = context.getResources().getDrawable(R.drawable.history_time_in_element);
                redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor1.setBackground(blueBackground);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor2.setBackground(redBackground);
            }
            tvLocationStatus1.setText("Time In");
            tvLocationStatus2.setText("Out of Location");
        }
        else if (position == list.size()-1) {
            Drawable blueBackground = null;
            Drawable redBackground = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blueBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_green,null);
                redBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_gray,null);
            } else {
                blueBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_green);
                redBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_gray);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor1.setBackground(blueBackground);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor2.setBackground(redBackground);
            }

            tvLocationStatus2.setText("Time Out");
            tvLocationStatus1.setText("In Location");

        } else {
            Drawable blueBackground = null;
            Drawable redBackground = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blueBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_green,null);
                redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red,null);
            } else {
                blueBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_green);
                redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor1.setBackground(blueBackground);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvColor2.setBackground(redBackground);
            }

            tvLocationStatus1.setText("In Location");
            tvLocationStatus2.setText("Out of Location");

        }
        LinearLayout.LayoutParams params  = (LinearLayout.LayoutParams) layoutFrom.getLayoutParams();
        params.bottomMargin = c.getTimeSpace();
        layoutFrom.setLayoutParams(params);
        timeF.setText(c.getFromDate());
        timeT.setText(c.getThruDate());

        return view;
    }
}
