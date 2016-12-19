/*
package com.oppo.sfamanagement.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.HistorySublist;

import java.util.ArrayList;
import java.util.HashMap;

*/
/**
 * Created by allsmartlt218 on 07-12-2016.
 *//*


public class ExpandableHistoryListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private HashMap<History,ArrayList<HistorySublist>> hashMap;

    public ExpandableHistoryListViewAdapter(Context context, HashMap<History,ArrayList<HistorySublist>> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
    }

    @Override
    public int getGroupCount() {
        return hashMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<HistorySublist> sublists = hashMap.get(groupPosition);
        return sublists.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return hashMap.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<HistorySublist> list = hashMap.get(groupPosition);
        HistorySublist sublist = list.get(childPosition);
        return sublist;

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        History listTitle = (History) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.history_list_group, null);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
     // HistorySublist expandedList =(HistorySublist) hashMap.get(groupPosition).get(childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);

            } else {
                params.setMargins(20,10,0,10);
            }
            convertView = layoutInflater.inflate(R.layout.history_tracking_item, parent,false);
            convertView.setLayoutParams(params);
        }
        TextView color = (TextView) convertView.findViewById(R.id.tvColor);
        TextView time = (TextView) convertView.findViewById(R.id.tvTime);
        TextView timeIn = (TextView) convertView.findViewById(R.id.timeIn);
           // time.setText(expandedList.getTime());
            //timeIn.setText(expandedList.getTimeIn());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
*/
