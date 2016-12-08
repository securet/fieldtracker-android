package com.oppo.sfamanagement.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.oppo.sfamanagement.model.History;

import java.util.HashMap;
import java.util.List;

/**
 * Created by allsmartlt218 on 07-12-2016.
 */

public class ExpandableHistoryListViewAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    List<History> list;
    private HashMap<Integer,List<History>> hashMap;

    public ExpandableHistoryListViewAdapter(Activity activity, HashMap<Integer,List<History>> hashMap, List<History> list) {
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
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
