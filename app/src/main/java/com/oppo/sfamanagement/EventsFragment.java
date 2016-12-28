package com.oppo.sfamanagement;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oppo.sfamanagement.adapter.ListViewHistoryAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.fragment.HistoryListTrackFragment;
import com.oppo.sfamanagement.model.HistoryNew;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object> {
	protected ListViewHistoryAdapter adapter;
	protected ListView listView;
	ArrayList<HistoryNew> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView) view.findViewById(R.id.expandableList);

		Bundle bundle = new Bundle();
		bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,"anand@securet.in","0","10"));
		bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
		getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST,bundle,EventsFragment.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			default:
				Fragment f = new HistoryListTrackFragment();
				FragmentManager fm = getFragmentManager();
                Bundle b = new Bundle();
                HistoryNew hn = list.get(position);
			//	System.out.println(hn.getHistoryChildren());
                b.putParcelable("sub_history",hn);
                b.putInt("position",position);
                f.setArguments(b);
				fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
				fm.executePendingTransactions();
		}
	}
	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {
		((MainActivity)getActivity()).showHideProgressForLoder(false);
		switch (id) {
			case LoaderConstant.HISTORY_LIST:
				return new LoaderServices(getContext(), LoaderMethod.HISTORY_LIST,args);
			default:
				return null;
		}
	}
	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {
		((MainActivity)getActivity()).showHideProgressForLoder(true);
		if(data != null && data instanceof ArrayList) {
			list = (ArrayList<HistoryNew>) data;
			System.out.println(list.size()   +  "   After Loader");
            //System.out.println(list.get(7).getHistoryChildren().toString());
			adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {

	}
}
