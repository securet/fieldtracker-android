package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.fitness.data.DataSource;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewTimeLineAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.CalenderUtils;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.TimeInOutDetails;
import com.oppo.sfamanagement.model.TimeLine;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by allsmartlt218 on 27-12-2016.
 */

public class TimeLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

    private ImageView ivClose;
    private TextView timeInTimeOut,tvTimeInOutLocation;
    private ListView lvTimeLine;
    private Preferences preferences;
    private EventDataSource dataSource;
    private ListViewTimeLineAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_fragment,container,false);
        preferences = new Preferences(getContext());
        dataSource = new EventDataSource(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivClose = (ImageView) view.findViewById(R.id.ivCloseButton);
        timeInTimeOut = (TextView) view.findViewById(R.id.tvTimeInOut);
        lvTimeLine = (ListView) view.findViewById(R.id.lvTimeLine);
        tvTimeInOutLocation = (TextView) view.findViewById(R.id.tvTimeInOutLocation);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
        TimeInOutDetails details = dataSource.getToday();
        String lastDate = CalenderUtils.getDateMethod(details.getClockDate(),CalenderUtils.DateMonthDashedFormate);
        if(!details.equals(null)) {
            if(clockDate.equalsIgnoreCase(lastDate)) {
                String actionType = details.getComments();
                // String time = getTime(today.getClockDate());

                if (TextUtils.isEmpty(actionType)  || actionType.equalsIgnoreCase("TimeOut")) {
                    Log.d("TIMEIN",preferences.getString(Preferences.TIMEINOUTSTATUS,""));
                    timeInTimeOut.setText("Time In");
                    //             tvTimeInOutLocation.setText("Time In at " + getCurrentTime(new Date()));
                } else if(/*actionType.equalsIgnoreCase("OutLocation") || actionType.equalsIgnoreCase("InLocation") ||*/ actionType.equalsIgnoreCase("TimeIn")){
                    timeInTimeOut.setText("Time Out");
                    tvTimeInOutLocation.setText(preferences.getString(Preferences.SITENAME,""));
                } else {

                }
            }
        }
        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,"anand@securet.in","0","1"));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.TIMELINE_LIST,b,TimeLineFragment.this).forceLoad();
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
        switch (id) {
            case LoaderConstant.TIMELINE_LIST:
                return new LoaderServices(getContext(), LoaderMethod.TIMELINE_LIST,args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        if (!data.equals(null) && data instanceof ArrayList) {
            ArrayList<TimeLine> list = (ArrayList<TimeLine>) data;

            adapter = new ListViewTimeLineAdapter(getContext(),R.layout.timeline_list_item, list);
            lvTimeLine.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


        getActivity().getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
