package com.oppo.fieldtracker.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.storage.Preferences;
import com.oppo.fieldtracker.model.TimeLine;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 27-12-2016.
 */

public class ListViewTimeLineAdapter extends BaseAdapter {
    private Context context;
    private int resouce;
    private ArrayList<TimeLine> list;
    private Preferences preferences;
    public ListViewTimeLineAdapter(Context context, int resource, ArrayList<TimeLine> list) {
        this.context = context;
        this.resouce = resource;
        this.list = list;
        preferences = new Preferences(context);
    }

    public void refresh( ArrayList<TimeLine> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public TimeLine getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        TimeLine tl = getItem(position);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.timeline_list_item,parent,false);
            holder = new ViewHolder();
            holder.timeF = (TextView) view.findViewById(R.id.tvTimeFrom);
            holder.timeT = (TextView) view.findViewById(R.id.tvTimeThru);
            holder.grayTop = (TextView) view.findViewById(R.id.tvGrayTop);
            holder.grayBottom = (TextView) view.findViewById(R.id.tvGrayBottom);
            holder.tvColor1 = (TextView) view.findViewById(R.id.tvColor1);
            holder.tvLocationStatus1 = (TextView) view.findViewById(R.id.tvLocationStatus1);
            holder.tvLocationStatus2 = (TextView) view.findViewById(R.id.tvLocationStatus2);
            holder.tvColor2 = (TextView) view.findViewById(R.id.tvColor2);
            holder.layoutFrom = (LinearLayout) view.findViewById(R.id.llFromDate);
            holder.layoutThru = (LinearLayout) view.findViewById(R.id.llThruDate);
            holder.position = position;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(position == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element,null));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
            }
            holder.grayTop.setVisibility(View.GONE);
            if(getCount() == 1) {
                holder.grayBottom.setVisibility(View.GONE);
            }else{
                holder.grayBottom.setVisibility(View.VISIBLE);
            }
            holder.layoutThru.setVisibility(View.VISIBLE);
            holder.tvLocationStatus1.setText("Time In");
            holder.tvLocationStatus2.setText("Out of Location");
        }
        else if (position == getCount()-1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green,null));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
            } else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
            }
            if(TextUtils.isEmpty(tl.getThruDate())){
                holder.layoutThru.setVisibility(View.GONE);
            }else{
                holder.layoutThru.setVisibility(View.VISIBLE);
                holder.tvLocationStatus2.setText("Out of Location");
            }
            holder.tvLocationStatus1.setText("In Location");
            holder.grayTop.setVisibility(View.VISIBLE);
            holder.grayBottom.setVisibility(View.GONE);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green,null));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
            }
            holder.layoutThru.setVisibility(View.VISIBLE);
            holder.tvLocationStatus1.setText("In Location");
            holder.tvLocationStatus2.setText("Out of Location");
            holder.grayTop.setVisibility(View.VISIBLE);
            holder.grayBottom.setVisibility(View.VISIBLE);
        }

        /*RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) holder.layoutFrom.getLayoutParams();
        params.bottomMargin = tl.getTimeSpace();
        holder.layoutFrom.setLayoutParams(params);*/
        holder.timeF.setText(tl.getFromDate());
//        timeT.setText(tl.getThruDate());
        if(!TextUtils.isEmpty(tl.getThruDate())){
            holder.timeT.setText(tl.getThruDate());
        } else {
            holder.timeT.setText("        -        ");
        }
        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView timeF;
        TextView timeT;
        TextView grayTop;
        TextView grayBottom;
        TextView tvColor1 ;
        TextView tvLocationStatus1 ;
        TextView tvLocationStatus2 ;
        TextView tvColor2 ;
        LinearLayout layoutFrom;
        LinearLayout layoutThru;
        int position;
    }
}


