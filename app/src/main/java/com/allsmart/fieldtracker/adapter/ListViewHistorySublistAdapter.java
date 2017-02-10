package com.allsmart.fieldtracker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
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
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.history_tracking_item,parent,false);
            holder = new ViewHolder();
            holder.timeF = (TextView) convertView.findViewById(R.id.tvTimeFrom);
            holder.timeT = (TextView) convertView.findViewById(R.id.tvTimeThru);
            holder.grayTop = (TextView) convertView.findViewById(R.id.tvGrayTop);
            holder.grayBottom = (TextView) convertView.findViewById(R.id.tvGrayBottom);
            holder.tvColor1 = (TextView) convertView.findViewById(R.id.tvColor1);
            holder.tvLocationStatus1 = (TextView) convertView.findViewById(R.id.tvLocationStatus1);
            holder.tvLocationStatus2 = (TextView) convertView.findViewById(R.id.tvLocationStatus2);
            holder.tvColor2 = (TextView) convertView.findViewById(R.id.tvColor2);
            holder.layoutFrom = (LinearLayout) convertView.findViewById(R.id.llFromDate);
            holder.layoutThru = (LinearLayout) convertView.findViewById(R.id.llThruDate);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryChild c = getItem(position);

        if(position == 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element,null));
                    if(getCount() == 1) {
                        holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray,null));
                    } else {
                        holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
                    }
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element));
                    if(getCount() == 1) {
                        holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray));
                    } else {
                        holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
                    }
                }
            holder.grayTop.setVisibility(View.GONE);
            if(getCount() == 1) {
                holder.grayBottom.setVisibility(View.GONE);
                holder.tvLocationStatus2.setText("Time Out");
            }else{
                holder.grayBottom.setVisibility(View.VISIBLE);
                holder.tvLocationStatus2.setText("Out of Location");
            }
            holder.layoutThru.setVisibility(View.VISIBLE);
            holder.tvLocationStatus1.setText("Time In");


        }
        else if (position == list.size()-1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green,null));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray,null));
            } else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green));
                holder.tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray));
            }
            if(TextUtils.isEmpty(c.getThruDate())){
                holder.layoutThru.setVisibility(View.GONE);
            }else{
                holder.layoutThru.setVisibility(View.VISIBLE);
                holder.tvLocationStatus2.setText("Time Out");
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
   //     RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) holder.layoutFrom.getLayoutParams();
   //     params.bottomMargin =5 + c.getTimeSpace();
        Log.d(MainActivity.TAG,c.getTimeSpace() + " This is bottom margin");

   //     holder.layoutFrom.setLayoutParams(params);
        holder.timeF.setText(c.getFromDate());
        if(!TextUtils.isEmpty(c.getThruDate())){
            holder. timeT.setText(c.getThruDate());
        } else{
            holder. timeT.setText("        -        ");
        }
        return convertView;
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
