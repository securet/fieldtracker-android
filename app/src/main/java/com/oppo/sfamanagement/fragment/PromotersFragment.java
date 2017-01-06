package com.oppo.sfamanagement.fragment;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.oppo.sfamanagement.EventsFragment;
import com.oppo.sfamanagement.LoginActivity;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.adapter.ListViewPromoterListAdapter;
import com.oppo.sfamanagement.database.AppsConstant;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.Promoter;
import com.oppo.sfamanagement.webmethods.LoaderConstant;
import com.oppo.sfamanagement.webmethods.LoaderMethod;
import com.oppo.sfamanagement.webmethods.LoaderServices;
import com.oppo.sfamanagement.webmethods.Services;
import com.oppo.sfamanagement.webmethods.UrlBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class PromotersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    ListViewPromoterListAdapter adapter;
    Button btAddPromoter;
    ListView listView;
    ArrayList<Promoter> list = new ArrayList<>();
    ImageView ivLoader;
    Preferences preferences;
    private int pageIndex = -1;
    private int pageSize = 10;
    private boolean isLoading = false;
    private View footerView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageIndex = 0;
        System.out.println(pageIndex + "  before scroll");
        Bundle b = new Bundle();
        b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.PROMOTER_LIST,pageIndex+"",String.valueOf(pageSize)));
        b.putString(AppsConstant.METHOD, AppsConstant.GET);
        getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_LIST,b,PromotersFragment.this).forceLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promoters,container,false);
        preferences = new Preferences(getContext());
        listView = (ListView) view.findViewById(R.id.lvPromotersList);
        btAddPromoter = (Button) view.findViewById(R.id.btAddPromoter);

        footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view,null);
        ivLoader = (ImageView) footerView.findViewById(R.id.footer_1);
        footerView.setVisibility(View.INVISIBLE);
        btAddPromoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new AddPromoterFragment();
                fm.beginTransaction().replace(R.id.flMiddle,fragment).addToBackStack(null).commit();
                fm.executePendingTransactions();
            }
        });

        adapter = new ListViewPromoterListAdapter(getActivity(), R.layout.promoter_list_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        return view;
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        isLoading = true;
            if (pageIndex == 0 ) {
                ((MainActivity) getActivity()).showHideProgressForLoder(false);
            }else{
                showLoader();
            }
        switch (id) {
            case LoaderConstant.PROMOTER_LIST:
                return new LoaderServices(getContext(), LoaderMethod.PROMOTER_LIST,args);
            default:
                return null;
        }
       // listView.addFooterView();
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(pageIndex==0 ){
            ((MainActivity)getActivity()).showHideProgressForLoder(true);
        }else{
            hideloader();
        }

        if(data != null && data instanceof ArrayList){
            if (list == null) {
                list = (ArrayList<Promoter>)data;
            } else {
                list.addAll((ArrayList<Promoter>) data);
            }
        }  else {

            Toast.makeText(getContext(),
                    "Error in response. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
        isLoading = false;
        adapter.refresh(list);
        getActivity().getLoaderManager().destroyLoader(loader.getId());

    }
    public void showLoader() {

            Animation rotateXaxis = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_x_axis);
            rotateXaxis.setInterpolator(new LinearInterpolator());
            ivLoader.setAnimation(rotateXaxis);
            footerView.setVisibility(View.VISIBLE);
    }
    public void hideloader() {
        footerView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Promoter p = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("promoter", p);
        FragmentManager fm = getFragmentManager();
        /*if (!p.getStatusId().equals(null) &&
                !preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExecutiveOnPremise") &&
                !preferences.getString(Preferences.ROLETYPEID,"").equalsIgnoreCase("FieldExectiveOffPremise") &&
                !p.getStatusId().equalsIgnoreCase("ReqCompleted")) {
*/
            Fragment fragment = new EditPromoterFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.flMiddle, fragment).addToBackStack(null).commit();
            fm.executePendingTransactions();
        /*} else {
            Fragment fragment = new FieldExecutivePromoterFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.flMiddle, fragment).addToBackStack(null).commit();
            fm.executePendingTransactions();
        }*/
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        switch(view.getId())
        {
            case R.id.lvPromotersList:

                final int lastItem = firstVisibleItem + visibleItemCount;
                boolean isLast = preferences.getBoolean(Preferences.PROMOTERISLAST,false);
                if(totalItemCount>0 && !isLoading && (lastItem >= totalItemCount-3) && !isLast)
                {
                    isLoading = true;
                    pageIndex++;
                    System.out.println("index   "  + pageIndex);
                    Bundle b = new Bundle();
                    b.putString(AppsConstant.URL, UrlBuilder.getPromoterList(Services.PROMOTER_LIST,String.valueOf(pageIndex),String.valueOf(pageSize)));
                    b.putString(AppsConstant.METHOD, AppsConstant.GET);
                    getActivity().getLoaderManager().initLoader(LoaderConstant.PROMOTER_LIST,b,PromotersFragment.this).forceLoad();
                }
        }
    }
}
