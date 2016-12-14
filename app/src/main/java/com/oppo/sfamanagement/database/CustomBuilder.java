package com.oppo.sfamanagement.database;

import android.R.color;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.model.Store;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

/**
 * Description of class : To create custom Alert Dialog Box Single Choice Item.
 */
public class CustomBuilder 
{
	private Context context;
	private CustomDialog customDialog;
	private ListView listView;
	private TextView tvNoSearchFound;
	private FilterListAdapter filterListAdapter;
	private Vector<View> vecVisibleCountryCells;
	private ArrayList<Object> vecData;
	private Object selObj;
	private OnClickListener listener;
	private String title;
	private boolean isCancellable;
	private boolean isSearchInvisible = false;
	private String replace ="../";
	private OnCancelListener onCancelListener;
	private final String SELECT = "Select";
	
	/**
	 *  Constructor 
	 * @param context
	 * @param title
	 * @param isCancellable
	 */
	public CustomBuilder(Context context, String title, boolean isCancellable)
	{
		this.context = context;
		this.title = title;
		this.isCancellable = isCancellable;
	}
	/**
	 * Method to set Single Choice Items 
	 * @param vecData
	 * @param selObj
	 * @param listener
	 */
	@SuppressWarnings("unchecked")
	public void setSingleChoiceItems(Object vecData, Object selObj, OnClickListener listener)
	{
		this.vecData  = (ArrayList<Object>)vecData;
		this.selObj   = selObj;
		this.listener = listener;
		if(selObj == null)
			this.selObj = new Object();
	}
	
	/**
	 * Method to set Single Choice Items 
	 * @param vecData
	 * @param selObj
	 * @param listener
	 */

	@SuppressWarnings("unchecked")
	public void setSingleChoiceItems(Object vecData, Object selObj, OnClickListener listener, boolean isSearchInvisible)
	{
		this.vecData  = (ArrayList<Object>)vecData;
		this.selObj   = selObj;
		this.listener = listener;
		this.isSearchInvisible = isSearchInvisible;
		if(selObj == null)
			this.selObj = new Object();
	}
	
	//Methof to show the Single Choice Itemsm Dialog
	public void show()
	{
		if(vecData == null)
			return;
		
		vecVisibleCountryCells = new Vector<View>();
		
		//Inflating the country_popup Layout
		View mView 		= LayoutInflater.from(context).inflate(R.layout.custom_builder, null);

		customDialog 	= new CustomDialog(context, mView,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, isCancellable);
		
		//Finding the ID's
		LinearLayout llView			= (LinearLayout) mView.findViewById(R.id.llView);
		TextView tvTitleBuider 		= (TextView) mView.findViewById(R.id.tvTitleBuider);
		final EditText etSearch = (EditText) mView.findViewById(R.id.etSearch);
		RelativeLayout llPopupClose 		= (RelativeLayout) mView.findViewById(R.id.llPopupClose);
		final ImageView ivSearchCross 		= (ImageView) mView.findViewById(R.id.ivSearchCross);
		LinearLayout llSearch 		= (LinearLayout) mView.findViewById(R.id.llSearch);

		if(isSearchInvisible)
		{
			llSearch.setVisibility(View.GONE);
			etSearch.setVisibility(View.GONE);
//			ivDiv.setVisibility(View.VISIBLE);
		}
		else
		{
			llSearch.setVisibility(View.VISIBLE);
			etSearch.setVisibility(View.VISIBLE);
//			ivDiv.setVisibility(View.GONE);
		}
		
		if(title.contains(SELECT))
		{
			tvTitleBuider.setText(title);
			etSearch.setHint(title);
		}
		else
		{
			tvTitleBuider.setText("Select "+title);
			etSearch.setHint("Search "+title);
		}
		tvNoSearchFound = (TextView) mView.findViewById(R.id.tvNoSearchFound);
		listView = (ListView) mView.findViewById(R.id.lvSelectCountry);
//		listView.setDivider(context.getResources().getDrawable(R.drawable.mk_dashed_line));
		listView.setFadingEdgeLength(0);
		listView.setCacheColorHint(0);
		listView.setVerticalScrollBarEnabled(false);
		listView.setSmoothScrollbarEnabled(true);
		
		
		filterListAdapter = new FilterListAdapter(vecData);
		listView.setSelector(color.transparent);
		//Setting the Adapter
		listView.setAdapter(filterListAdapter);
		listView.setVisibility(View.VISIBLE);


		llPopupClose.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				dismiss();
				v.setClickable(true);
				v.setEnabled(true);
				
				if(onCancelListener != null)
					onCancelListener.onCancel();
				
				return true;
			}
		});
		
		//Functionality for listView Item Click
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				for(int i = 0; i < vecVisibleCountryCells.size(); i++)
				{
					View visibleCountryCell = vecVisibleCountryCells.get(i);
					visibleCountryCell.findViewById(R.id.ivSelected).setBackgroundResource(R.drawable.radio_unchecked);
				}

				view.findViewById(R.id.ivSelected).setBackgroundResource(R.drawable.radio_checked);
				
				listener.onClick(CustomBuilder.this, view.getTag());
			}
		});
		ivSearchCross.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				etSearch.setText("");
				ivSearchCross.setVisibility(View.GONE);
				tvNoSearchFound.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				filterListAdapter.refresh(vecData);
				return true;
			}
		});
		//Functionality for etSelectItem
		etSearch.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				if(!s.toString().equalsIgnoreCase(""))
				{
					ArrayList<Object> vecTemp = new ArrayList<Object>();
					for(int i = 0; vecData != null && i < vecData.size(); i++)
					{
						Object obj = vecData.get(i);
						String field = "";
						
						if(obj instanceof String) {
							field = ((String) obj);
							if(field.toLowerCase().contains(s.toString().toLowerCase()))
							{
								vecTemp.add(vecData.get(i));
							}
						} else if (obj instanceof Store) {
							field = ((Store) obj).getStoreName();
							if (field.toLowerCase().contains(s.toString().toLowerCase())) {
                                vecTemp.add(vecData.get(i));
							}
						}


					}
					if(vecTemp.size() > 0)
					{
						tvNoSearchFound.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						filterListAdapter.refresh(vecTemp);
					}
					else
					{
						tvNoSearchFound.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					}
				}
				else
				{
					tvNoSearchFound.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					filterListAdapter.refresh(vecData);
				}
			}
		});
		
		if (!customDialog.isShowing())
			customDialog.showCustomDialog();
	}
	
	public void dismiss()
	{
		customDialog.dismiss();
	}
//	public void setTypeFace(ViewGroup group)
//	{
//	     int count = group.getChildCount();
//	     View v;
//	     for(int i = 0; i < count; i++) {
//	         v = group.getChildAt(i);
//	         if(v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
//	             ((TextView)v).setTypeface(AppsConstant.RobotoSlabRegular);
//	         else if(v instanceof ViewGroup)
//	        	 setTypeFace((ViewGroup)v);
//	     }
//	}
	
	private class FilterListAdapter extends BaseAdapter
	{
		private ArrayList<Object> vecData;

		public FilterListAdapter(ArrayList<Object> vecData)
		{
			this.vecData = vecData;
		}

		@Override
		public int getCount() 
		{
			if(vecData == null)
				return 0;
			else 
				return vecData.size();
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			Object obj = vecData.get(position);
			
			//Inflating the country_cell Layout
			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.custom_builder_cell, null);
				vecVisibleCountryCells.add(convertView);
			}
			
			TextView tvSelectUrCountry  = (TextView)convertView.findViewById(R.id.tvSelectName);
			ImageView ivSelected = (ImageView)convertView.findViewById(R.id.ivSelected);
			
			String name = ""; boolean isShowAsSelected = false;
			//in Case of OrderListDO
			if(obj instanceof String)
			{
				name = ((String)obj);
				ivSelected.setVisibility(View.VISIBLE);
				if(selObj instanceof String && ((String)selObj).equalsIgnoreCase(name))
					isShowAsSelected = true;
			} else if (obj instanceof Store) {
                name = ((Store) obj).getStoreName();
                ivSelected.setVisibility(View.VISIBLE);
               /* if (selObj instanceof Store && ((Store) selObj).getStoreName().equalsIgnoreCase(name)) {
                    isShowAsSelected = true;
                }*/

            }
			tvSelectUrCountry.setText(name);
			if(isShowAsSelected)
				ivSelected.setBackgroundResource(R.drawable.radio_checked);
			else
				ivSelected.setBackgroundResource(R.drawable.radio_unchecked);

			convertView.setTag(obj);
			
			convertView.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return convertView;
		}
		//Method to refresh the List View
		private void refresh(ArrayList<Object> vecData)
		{
			this.vecData = vecData;
			this.notifyDataSetChanged();
		}
	}
	public interface OnClickListener
	{
		void onClick(CustomBuilder builder, Object selectedObject);
	}
	
	public interface OnCancelListener 
	{
		void onCancel();
	}
	
	public void setCancelListener(OnCancelListener onCancelListener)
	{
		this.onCancelListener = onCancelListener;
	}
}
