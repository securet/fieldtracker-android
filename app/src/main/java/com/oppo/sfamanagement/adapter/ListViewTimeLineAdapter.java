package com.oppo.sfamanagement.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.DynamicElement;
import com.oppo.sfamanagement.model.TimeLine;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        TimeLine tl = getItem(position);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.timeline_list_item,parent,false);
        }
        TextView timeF = (TextView) view.findViewById(R.id.tvTimeFrom);
        TextView timeT = (TextView) view.findViewById(R.id.tvTimeThru);
        TextView tvColor1 = (TextView) view.findViewById(R.id.tvColor1);
        TextView tvLocationStatus1 = (TextView) view.findViewById(R.id.tvLocationStatus1);
        TextView tvLocationStatus2 = (TextView) view.findViewById(R.id.tvLocationStatus2);
        TextView tvColor2 = (TextView) view.findViewById(R.id.tvColor2);
        TextView timeShift = (TextView) view.findViewById(R.id.stvShiftTime);
        LinearLayout layoutFrom = (LinearLayout) view.findViewById(R.id.llFromDate);
        LinearLayout layoutThru = (LinearLayout) view.findViewById(R.id.llThruDate);


        if(position == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element,null));
                tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
                tvLocationStatus1.setText("Time In");
                tvLocationStatus2.setText("Out of Location");
            }
        }
        else if (position == list.size()-1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green,null));
                tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray,null));
                tvLocationStatus2.setText("Time Out");
                tvLocationStatus1.setText("In Location");
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvColor2.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red,null));
                tvColor1.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green,null));
                tvLocationStatus1.setText("In Location");
                tvLocationStatus2.setText("Out of Location");
            }
        }

        LinearLayout.LayoutParams params  = (LinearLayout.LayoutParams) layoutFrom.getLayoutParams();
        params.bottomMargin = tl.getTimeSpace();
        layoutFrom.setLayoutParams(params);
        timeF.setText(tl.getFromDate());
        timeT.setText(tl.getThruDate());
        String time = preferences.getString(Preferences.SHIFTTIME,"");
        System.out.print(time);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
