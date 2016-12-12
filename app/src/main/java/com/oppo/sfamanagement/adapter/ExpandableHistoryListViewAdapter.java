package com.oppo.sfamanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.DynamicElementModel;
import com.oppo.sfamanagement.model.History;

import java.util.HashMap;
import java.util.List;

/**
 * Created by allsmartlt218 on 07-12-2016.
 */

public class ExpandableHistoryListViewAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    List<DynamicElementModel> list;
    private HashMap<History,List<DynamicElementModel>> hashMap;

    public ExpandableHistoryListViewAdapter(Activity activity, HashMap<History,List<DynamicElementModel>> hashMap, List<DynamicElementModel> list) {
        this.activity = activity;
        this.hashMap = hashMap;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.hashMap.get(this.list.get(groupPosition))
                .get(childPosition);

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
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.history_list_group, null);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.history_list_item, parent);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
