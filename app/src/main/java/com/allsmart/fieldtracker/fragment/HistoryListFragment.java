package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.adapter.ListViewHistoryAdapter;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.model.Manager;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.HistoryNew;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.utils.UrlBuilder;

import java.util.ArrayList;

public class HistoryListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Object>, AbsListView.OnScrollListener {
	protected ListViewHistoryAdapter adapter;
	protected ListView listView;
	ArrayList<HistoryNew> list= new ArrayList<>();
    private int pageIndex = -1;
    private ImageView ivLoader;
    private View footerView;
    private boolean isLoading = false;
    private Preferences preferences;
    private String username="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        pageIndex = 0;
        preferences = new Preferences(getContext());
        Bundle bundle = new Bundle();
        System.out.println(pageIndex);

        /*if(((MainActivity) getActivity()).isManager()) {
            if(((MainActivity)getActivity()).openPage == MainActivity.MAP) {
                Manager manager = getArguments().getParcelable("manager");
                if(manager != null *//*&& !TextUtils.isEmpty(manager.getEmail())*//*) {
                    bundle.putString(AppsConstant.URL,UrlBuilder.getHistoryList(Services.HISTORY_LIST,"manju+on@allsmart.in"*//*manager.getEmail()*//*,"0","10"));
                } else {
                    Log.d(MainActivity.TAG,"Error at Member History List  " + manager.getUsername());
                }
            }
        } else {*/
            username = preferences.getString(Preferences.USERNAME,"");
            if(!TextUtils.isEmpty(username)) {
                bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,username,"0","10"));
                bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
                getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST,bundle,HistoryListFragment.this);
            } else {
                Log.d(MainActivity.TAG,"Error at Field History List");
            }
     //   }

	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,false);
		listView = (ListView) rootView.findViewById(R.id.expandableList);

        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);

		adapter = new ListViewHistoryAdapter(getActivity(),R.layout.history_list_item,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
        hideloader();
		listView.setOnScrollListener(this);

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
                HistoryNew hn = null;
                if(list != null) {
                    hn = list.get(position);
                    if(hn != null && isNotNull(hn)) {
                        b.putParcelable("sub_history",hn);
                        b.putInt("position",position);
                        f.setArguments(b);
                        fm.beginTransaction().replace(R.id.flMiddle,f).addToBackStack(null).commit();
                        fm.executePendingTransactions();
                    } else {
                        ((MainActivity)getActivity()).displayMessage("No data to show time line");
                    }
                }



		}
	}

    private boolean isNotNull(HistoryNew hn) {
        if(hn.getDate() == null ||
                hn.getHistoryChildren() == null ||
                hn.getHours() == null ||
                hn.getTimeIn() == null ||
                hn.getTimeOut() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).isLoading = true;
        isLoading = true;
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
            if(getActivity() != null) {
                ((MainActivity)getActivity()).showHideProgressForLoder(true);
            }
        }else{
            hideloader();
        }
		if(data != null && data instanceof ArrayList) {
            if(preferences.getInt(Preferences.HISTORY_COUNT,0) == 0) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("No Entries");
                dialog.setMessage("No Entries for " + preferences.getString(Preferences.USERFULLNAME,""));
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                if(list == null) {
                    list = (ArrayList<HistoryNew>) data;
                }else{
                    list.addAll((ArrayList<HistoryNew>) data);
                }
            }
            if(list != null) {
                if(adapter != null) {
                    adapter.Refresh(list);
                }
            }
        } else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }


            isLoading = false;
        ((MainActivity)getActivity()).isLoading = false;

            if(getActivity() != null) {
                getActivity().getLoaderManager().destroyLoader(loader.getId());
            }
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {

	}

    public void showLoader() {

        Animation rotateXaxis = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_x_axis);
        rotateXaxis.setInterpolator(new LinearInterpolator());
        ivLoader.setAnimation(rotateXaxis);
        listView.addFooterView(footerView);
        footerView.setVisibility(View.VISIBLE);

    }
    public void hideloader() {
        listView.removeFooterView(footerView);
        footerView.setVisibility(View.GONE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int count = preferences.getInt(Preferences.HISTORY_COUNT,0);
        //System.out.println(count + "   count onScroll");
//				if(!isLoading && (i+i1 >=i2-3)&& !isLast){
//					isLoading = true;
//					increase Page Index;
//					Call ApI
//				}
        if (count > totalItemCount && totalItemCount > 0 && !isLoading && (firstVisibleItem + visibleItemCount >= totalItemCount - 3)) {
            ((MainActivity) getActivity()).isLoading = true;
            isLoading = true;
            pageIndex++;
            System.out.println(pageIndex + "   onScroll");
            Bundle bundle = new Bundle();
            /*if(((MainActivity) getActivity()).isManager()) {
                if(((MainActivity)getActivity()).openPage == MainActivity.MAP) {
                    Manager manager = getArguments().getParcelable("manager");
                    if(manager != null && !TextUtils.isEmpty(manager.getUsername())) {
                        bundle.putString(AppsConstant.URL,UrlBuilder.getHistoryList(Services.HISTORY_LIST,manager.getUsername(),pageIndex+"","10"));
                    } else {
                        Log.d(MainActivity.TAG,"Error at Member History List");
                    }
                }
            } else {*/
               /* username = preferences.getString(Preferences.USERNAME,"");
                if(!TextUtils.isEmpty(username)) {*/
                    bundle.putString(AppsConstant.URL, UrlBuilder.getHistoryList(Services.HISTORY_LIST,username,pageIndex+"","10"));
                /*} else {
                    Log.d(MainActivity.TAG,"Error at Field History List");
                }*/
      //      }
            bundle.putString(AppsConstant.METHOD, AppsConstant.GET);
            getActivity().getLoaderManager().initLoader(LoaderConstant.HISTORY_LIST, bundle, HistoryListFragment.this);
        }
    }
}
