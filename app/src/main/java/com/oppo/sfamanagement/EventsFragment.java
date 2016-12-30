package com.oppo.sfamanagement;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.adapter.ListViewHistoryAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.fragment.HistoryListTrackFragment;
import com.oppo.sfamanagement.model.HistoryNew;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object>, AbsListView.OnScrollListener {
	protected ListViewHistoryAdapter adapter;
	protected ListView listView;
	ArrayList<HistoryNew> list;
    private int pageIndex = -1;
    ImageView ivLoader;
    private LinearLayout layout;
    private View footerView;
    private boolean isLoading = false;
    private Preferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,false);
		listView = (ListView) rootView.findViewById(R.id.expandableList);
        preferences = new Preferences(getContext());
        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null,false);
        layout = (LinearLayout)footerView.findViewById(R.id.footer_layout) ;
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        layout.setVisibility(View.INVISIBLE);
        footerView.setVisibility(View.INVISIBLE);

		adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,new ArrayList<HistoryNew>());
		listView.setAdapter(adapter);
        listView.addFooterView(layout);
		listView.setOnItemClickListener(this);
        hideloader();
		listView.setOnScrollListener(this);
        pageIndex = 0;
        Bundle bundle = new Bundle();
        System.out.println(pageIndex);
        bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,"anand@securet.in","0","10"));
        bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST,bundle,EventsFragment.this);
		// Page index = 0
		// isLastPage = false

		return rootView;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			default:
				Fragment f = new HistoryListTrackFragment();
				FragmentManager fm = getFragmentManager();
                Bundle b = new Bundle();
                HistoryNew hn = list.get(position);
                b.putParcelable("sub_history",hn);
                b.putInt("position",position);
                f.setArguments(b);
				fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
				fm.executePendingTransactions();
		}
	}
	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {
        if (pageIndex == 0 ) {
            ((MainActivity) getActivity()).showHideProgressForLoder(false);
        }else{
            showLoader();
        }
		switch (id) {
			case LoaderConstant.HISTORY_LIST:
				return new LoaderServices(getContext(), LoaderMethod.HISTORY_LIST,args);
			default:
				return null;
		}
	}
	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {
        if(pageIndex==0 ){
            ((MainActivity)getActivity()).showHideProgressForLoder(true);
        }else{
            hideloader();
        }
		if(data != null && data instanceof ArrayList) {

        } else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }

			if(list == null) {
				list = (ArrayList<HistoryNew>) data;
			}else{
				list.addAll((ArrayList<HistoryNew>) data);
			}
            isLoading = false;
			adapter.Refresh(list);

		getActivity().getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {

	}

    public void showLoader() {

        Animation rotateXaxis = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_x_axis);
        rotateXaxis.setInterpolator(new LinearInterpolator());
        ivLoader.setAnimation(rotateXaxis);
        footerView.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);

    }
    public void hideloader() {

        layout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        boolean isLast = preferences.getBoolean(Preferences.ISLAST,true);
//				if(!isLoading && (i+i1 >=i2-3)&& !isLast){
//					isLoading = true;
//					increase Page Index;
//					Call ApI
//				}


        if (!isLoading &&(firstVisibleItem + visibleItemCount >= totalItemCount-3) && !isLast) {
            isLoading = true;
            preferences.saveBoolean(Preferences.ISLAST,true);
            preferences.commit();
            pageIndex++;
            System.out.println(pageIndex + "   onScroll");
            Bundle bundle = new Bundle();
            bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST, "anand@securet.in",pageIndex+"", "10"));
            bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST, bundle, EventsFragment.this);

        }

    }
}
