package com.allsmart.fieldtracker.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.constants.AppsConstant;
import com.allsmart.fieldtracker.constants.LoaderConstant;
import com.allsmart.fieldtracker.constants.LoaderMethod;
import com.allsmart.fieldtracker.constants.Services;
import com.allsmart.fieldtracker.model.Contact;
import com.allsmart.fieldtracker.service.LoaderServices;
import com.allsmart.fieldtracker.utils.UrlBuilder;

public class ContactSupportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {

	private TextView email,phone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contact_support_fragment, container,false);
        email = (TextView) rootView.findViewById(R.id.tvContactEmail);
        phone = (TextView) rootView.findViewById(R.id.tvContactPhone);

		Bundle b = new Bundle();
		b.putString(AppsConstant.METHOD,AppsConstant.GET);
		b.putString(AppsConstant.URL, UrlBuilder.getUrl(Services.CONTACT_SUPPORT));
		getActivity().getLoaderManager().initLoader(LoaderConstant.CONTACT_SUPPORT,b,ContactSupportFragment.this).forceLoad();

        return rootView;
	}

	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {
        ((MainActivity)getActivity()).showHideProgressForLoder(false);
		switch (id) {
			case LoaderConstant.CONTACT_SUPPORT:
				return new LoaderServices(getContext(), LoaderMethod.CONTACT_SUPPORT,args);
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {
        ((MainActivity)getActivity()).showHideProgressForLoder(true);
        switch (loader.getId()){
            case LoaderConstant.CONTACT_SUPPORT:
                if(data != null && data instanceof Contact) {
                    Contact contact = (Contact) data;
                    email.setText(contact.getEmailAddress());
                    phone.setText(contact.getContactNum());
                } else {
                    ((MainActivity)getActivity()).displayMessage("Error in getting contact details");
                }
                break;
        }
      //  if(getActivity() != null && getActivity() instanceof MainActivity) {
            getActivity().getLoaderManager().destroyLoader(loader.getId());
      //  }
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {

	}
}
