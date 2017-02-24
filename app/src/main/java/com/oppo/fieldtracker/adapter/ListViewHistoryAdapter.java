package com.oppo.fieldtracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.model.HistoryChild;
import com.oppo.fieldtracker.model.HistoryNew;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class ListViewHistoryAdapter extends ArrayAdapter<HistoryNew>{

    protected ArrayList<HistoryNew> list;
    protected int resourceId;
    protected Context context;

    public ListViewHistoryAdapter(Context context, int resource, ArrayList<HistoryNew> list) {
        super(context,resource,list);
        this.context = context;
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

        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(resourceId,parent,false);
        }

        HistoryNew historyNew =  getItem(position);
        HistoryChild childForFromdate = historyNew.getHistoryChildren().get(0);
        HistoryChild childForThrudate = historyNew.getHistoryChildren().get(historyNew.getHistoryChildren().size()-1);
        historyNew.setTimeIn(childForFromdate.getFromDate());
        historyNew.setTimeOut(childForThrudate.getThruDate());

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
