package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.HistoryChild;
import com.oppo.sfamanagement.model.HistoryNew;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-12-2016.
 */

public class HistoryListTrackFragment extends Fragment {

    int height = 0;
    private TextView date,timeIn,timeOut,hour;
    private TextView fromDate,comment,fromDate2,comment2,fromDate3,comment3;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list_track_fragment,container,false);
        date = (TextView) view.findViewById(R.id.tvHistoryDate);
        timeIn = (TextView) view.findViewById(R.id.tvHistoryTimeIn);
        timeOut = (TextView) view.findViewById(R.id.tvHistoryTimeOut);
        hour = (TextView) view.findViewById(R.id.tvHistoryTime);
        fromDate = (TextView) view.findViewById(R.id.tvTimeInTimeBlue) ;
        comment = (TextView) view.findViewById(R.id.tvComment1);
        fromDate2 = (TextView) view.findViewById(R.id.tvTimeInGreen1);
        comment2 = (TextView) view.findViewById(R.id.tvCommentGreen1);
        fromDate3 = (TextView) view.findViewById(R.id.tvTimeInTimeGreen2);
        comment3 = (TextView) view.findViewById(R.id.tvCommentGreen2);

        HistoryNew historyNew = getArguments().getParcelable("sub_history");
        date.setText(historyNew.getDate());
        timeIn.setText(historyNew.getTimeIn());
        timeOut.setText(historyNew.getTimeOut());
        hour.setText(historyNew.getHours());
       /* ArrayList<HistoryChild> historyChildList = historyNew.getHistoryChildren();
        System.out.println(historyChildList);
        int position = getArguments().getInt("position");
        HistoryChild historyChild = historyChildList.get(position);
        System.out.println(historyChild);
        HistoryChild historyChild2 = historyChildList.get(position+1);
        System.out.println(historyChild2);
        HistoryChild historyChild3 = historyChildList.get(position+2);
        System.out.println(historyChild3);
        TextView tv = (TextView) view.findViewById(R.id.tvVerticleLine);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l1 = (LinearLayout) view.findViewById(R.id.redItem1);
        fromDate.setText(historyChild.getFromDate());
        comment.setText(historyChild.getComments());
        layoutParams.setMargins(30,DynamicElement.findMarginTop(historyChild.getFromDate(),historyChild2.getFromDate()),0,0);
        l1.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l2 = (LinearLayout) view.findViewById(R.id.greenItem1);
        fromDate2.setText(historyChild2.getFromDate());
        comment2.setText(historyChild2.getComments());
        layoutParams2.setMargins(30,DynamicElement.findMarginTop(historyChild2.getFromDate(),historyChild3.getFromDate()),0,0);
        l2.setLayoutParams(layoutParams2);

        *//*LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l3 = (LinearLayout) view.findViewById(R.id.redItem2);
        layoutParams3.setMargins(30,DynamicElement.findMarginTop("11:56","03:01"),0,0);
        l3.setLayoutParams(layoutParams3);*//*

        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l4 = (LinearLayout) view.findViewById(R.id.greenItem2);
        fromDate3.setText(historyChild3.getFromDate());
        comment3.setText(historyChild3.getComments());
        layoutParams4.setMargins(30,DynamicElement.findMarginTop(historyChild3.getFromDate(),historyNew.getTimeOut()),0,0);
        l4.setLayoutParams(layoutParams4);

       *//* LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l5 = (LinearLayout) view.findViewById(R.id.grayItem);
        layoutParams5.setMargins(30,DynamicElement.findMarginTop("03:23","06:41"),0,0);
        l5.setLayoutParams(layoutParams5);
        height = DynamicElement.findMarginTop("10:45","11:32") + DynamicElement.findMarginTop("11:32","11:56") + DynamicElement.findMarginTop("11:56","03:01") + DynamicElement.findMarginTop("03:01","03:23" + 80                                     );
        */return view;
    }
}
