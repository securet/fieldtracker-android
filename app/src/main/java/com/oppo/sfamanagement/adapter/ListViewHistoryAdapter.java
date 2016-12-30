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
import com.oppo.sfamanagement.TimeDifferenceCalculator;
import com.oppo.sfamanagement.model.HistoryChild;
import com.oppo.sfamanagement.model.HistoryNew;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class ListViewHistoryAdapter extends ArrayAdapter<HistoryNew>{

    protected ArrayList<HistoryNew> list;
    protected int resourceId;
    protected Activity activity;

    public ListViewHistoryAdapter(Activity activity, int resource, ArrayList<HistoryNew> list) {
        super(activity,resource,list);
        this.activity = activity;
        this.resourceId = resource;
        this.list = list;
    }
    public void  Refresh( ArrayList<HistoryNew> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Nullable
    public HistoryNew getItem(int position) {

        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
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
        if (rowView == null) {
            rowView = inflater.inflate(resourceId,parent,false);
        }

        HistoryNew historyNew =  getItem(position);
        HistoryChild childForFromdate = historyNew.getHistoryChildren().get(0);
        HistoryChild childForThrudate = historyNew.getHistoryChildren().get(historyNew.getHistoryChildren().size()-1);
        historyNew.setTimeIn(childForFromdate.getFromDate());
        historyNew.setTimeOut(childForThrudate.getThruDate());
        historyNew.setHours(TimeDifferenceCalculator.findMarginTop(childForFromdate.getFromDate(),childForThrudate.getThruDate()));

        TextView tvDate = (TextView) rowView.findViewById(R.id.tvHistoryDate);
        TextView tvTimeIn = (TextView) rowView.findViewById(R.id.tvHistoryTimeIn);
        TextView tvTimeOut = (TextView) rowView.findViewById(R.id.tvHistoryTimeOut);
        TextView tvTime = (TextView) rowView.findViewById(R.id.tvHistoryTime);
        tvDate.setText(historyNew.getDate());
        tvTimeIn.setText(historyNew.getTimeIn());
        tvTimeOut.setText(historyNew.getTimeOut());
        tvTime.setText(historyNew.getHours());
        return rowView;
    }
}
