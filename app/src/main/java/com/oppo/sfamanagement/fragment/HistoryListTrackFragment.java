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

/**
 * Created by allsmartlt218 on 06-12-2016.
 */

public class HistoryListTrackFragment extends Fragment {

    int height = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list_track_fragment,container,false);
        TextView tv = (TextView) view.findViewById(R.id.tvVerticleLine);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l1 = (LinearLayout) view.findViewById(R.id.redItem1);
        layoutParams.setMargins(30,DynamicElement.findMarginTop("10:45","11:32"),0,0);
        l1.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l2 = (LinearLayout) view.findViewById(R.id.greenItem1);
        layoutParams2.setMargins(30,DynamicElement.findMarginTop("11:32","11:56"),0,0);
        l2.setLayoutParams(layoutParams2);

        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l3 = (LinearLayout) view.findViewById(R.id.redItem2);
        layoutParams3.setMargins(30,DynamicElement.findMarginTop("11:56","03:01"),0,0);
        l3.setLayoutParams(layoutParams3);

        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l4 = (LinearLayout) view.findViewById(R.id.greenItem2);
        layoutParams4.setMargins(30,DynamicElement.findMarginTop("03:01","03:23"),0,0);
        l4.setLayoutParams(layoutParams4);

        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout l5 = (LinearLayout) view.findViewById(R.id.grayItem);
        layoutParams5.setMargins(30,DynamicElement.findMarginTop("03:23","06:41"),0,0);
        l5.setLayoutParams(layoutParams5);
        height = DynamicElement.findMarginTop("10:45","11:32") + DynamicElement.findMarginTop("11:32","11:56") + DynamicElement.findMarginTop("11:56","03:01") + DynamicElement.findMarginTop("03:01","03:23" + 80                                     );
        return view;
    }
}
