package com.oppo.fieldtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oppo.fieldtracker.R;
import com.oppo.fieldtracker.adapter.ListViewHistorySublistAdapter;
import com.oppo.fieldtracker.model.HistoryChild;
import com.oppo.fieldtracker.model.HistoryNew;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 06-12-2016.
 */

public class HistoryListTrackFragment extends Fragment {

    //private HistoryNew historyNew;
    private TextView date,timeIn,timeOut,hour;
    private ListView listView;
    private RelativeLayout layout;
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
        layout = (RelativeLayout) view.findViewById(R.id.llEvents);


        HistoryNew historyNew = getArguments().getParcelable("sub_history");
        ArrayList<HistoryChild> list = historyNew.getHistoryChildren();
        System.out.println(list.size() + " list size providing");
        date.setText(historyNew.getDate());
        timeIn.setText(historyNew.getTimeIn());
        timeOut.setText(historyNew.getTimeOut());
        hour.setText(historyNew.getHours());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });

        ListViewHistorySublistAdapter adapter = new ListViewHistorySublistAdapter(getContext(),R.layout.history_tracking_item,historyNew.getHistoryChildren());
        listView.setAdapter(adapter);
        adapter.refresh(historyNew.getHistoryChildren());
        return view;
    }

}
