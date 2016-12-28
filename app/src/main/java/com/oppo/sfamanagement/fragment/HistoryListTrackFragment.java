package com.oppo.sfamanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewHistorySublistAdapter;
import com.oppo.sfamanagement.model.HistoryChild;
import com.oppo.sfamanagement.model.HistoryNew;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-12-2016.
 */

public class HistoryListTrackFragment extends Fragment {

    //private HistoryNew historyNew;
    private TextView date,timeIn,timeOut,hour,tvTime;
    private ListView listView;
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
        listView = (ListView) view.findViewById(R.id.lvHistorySub);
        hour = (TextView) view.findViewById(R.id.tvHistoryTime);


        HistoryNew historyNew = getArguments().getParcelable("sub_history");
        ArrayList<HistoryChild> list = historyNew.getHistoryChildren();
        System.out.println(list.size() + " list size providing");
        date.setText(historyNew.getDate());
        timeIn.setText(historyNew.getTimeIn());
        timeOut.setText(historyNew.getTimeOut());
        hour.setText(historyNew.getHours());

        ListViewHistorySublistAdapter adapter = new ListViewHistorySublistAdapter(getContext(),R.layout.history_tracking_item,historyNew.getHistoryChildren());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

}
