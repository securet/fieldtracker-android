package com.allsmart.fieldtracker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.model.HistoryChild;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 26-12-2016.
 */

public class ListViewHistorySublistAdapter extends ArrayAdapter<HistoryChild> {
    private Context context;
    private int resource;
    private ArrayList<HistoryChild> list;
    public ListViewHistorySublistAdapter(Context context, int resource,ArrayList<HistoryChild> list) {
        super(context,resource,list );
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    public void refresh(ArrayList<HistoryChild> list){
        this.list = list;
        notifyDataSetChanged();
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
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(convertView == null) {
            view = inflater.inflate(R.layout.history_tracking_item,parent,false);
        } else {
            view = convertView;
        }
        HistoryChild c = getItem(position);
        TextView timeF = (TextView) view.findViewById(R.id.tvTimeFrom);
        TextView timeT = (TextView) view.findViewById(R.id.tvTimeThru);
        TextView grayTop = (TextView) view.findViewById(R.id.tvGrayTop);
        TextView grayBottom = (TextView) view.findViewById(R.id.tvGrayBottom);
        TextView tvColor1 = (TextView) view.findViewById(R.id.tvColor1);
        TextView tvLocationStatus1 = (TextView) view.findViewById(R.id.tvLocationStatus1);
        TextView tvLocationStatus2 = (TextView) view.findViewById(R.id.tvLocationStatus2);
        TextView tvColor2 = (TextView) view.findViewById(R.id.tvColor2);
        LinearLayout layoutFrom = (LinearLayout) view.findViewById(R.id.llFromDate);
        LinearLayout layoutThru = (LinearLayout) view.findViewById(R.id.llThruDate);
        if(position == 0) {
                Drawable blueBackground = null;
                Drawable redBackground = null;
                Drawable grayBackground = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    blueBackground = context.getResources().getDrawable(R.drawable.history_time_in_element,null);
                    redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red,null);
                    grayBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_gray,null);
                } else {
                    blueBackground = context.getResources().getDrawable(R.drawable.history_time_in_element);
                    redBackground = context.getResources().getDrawable(R.drawable.history_time_in_element_red);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvColor1.setBackground(blueBackground);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if(getCount() != 1) {
                        tvColor2.setBackground(redBackground);
                    } else {
                        tvColor2.setBackground(grayBackground);
                    }
                }
                grayTop.setVisibility(View.GONE);
                if(getCount() == 1) {
                    grayBottom.setVisibility(View.GONE);
                    tvLocationStatus2.setText("Time Out");
                } else {
                    tvLocationStatus2.setText("Out of Location");
                }
                tvLocationStatus1.setText("Time In");

        }
        else if (position == list.size()-1) {
            Drawable blueBackground = null;
            Drawable redBackground = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blueBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_green, null);
                redBackground = context.getResources().getDrawable(R.drawable.history_tracking_element_gray, null);
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
            if (TextUtils.isEmpty(c.getThruDate())) {
                layoutThru.setVisibility(View.GONE);
            } else {
                layoutThru.setVisibility(View.VISIBLE);
                tvLocationStatus2.setText("Time Out");
            }
            tvLocationStatus1.setText("In Location");
            grayBottom.setVisibility(View.GONE);

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
        RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) layoutFrom.getLayoutParams();
        params.bottomMargin = c.getTimeSpace();
        layoutFrom.setLayoutParams(params);
        timeF.setText(c.getFromDate());
        if(!TextUtils.isEmpty(c.getThruDate())){
            timeT.setText(c.getThruDate());
        } else{
            timeT.setText("        -        ");
        }
        return view;
    }
}
