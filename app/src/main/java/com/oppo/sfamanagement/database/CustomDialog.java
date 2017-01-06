package com.oppo.sfamanagement.database;


import android.app.Dialog;
import android.content.Context;
import android.net.MailTo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;

import java.lang.ref.WeakReference;


/** class to create the Custom Dialog **/
public class CustomDialog extends Dialog
{
	//initializations
	boolean isCancellable = true;
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 */
	private WeakReference<MainActivity> baseActivity;
	
	public CustomDialog(Context context, View view) 
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 */
	public CustomDialog(Context context,View view, int lpW, int lpH) 
	{
		this(context, view, lpW, lpH, true);
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 * @param isCancellable
	 */
	public CustomDialog(Context context,View view, int lpW, int lpH, boolean isCancellable) 
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
		this.isCancellable = isCancellable;
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	
	public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable, int style) 
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
		this.isCancellable = isCancellable;
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	
	@Override
	public void onBackPressed()
	{
		if(isCancellable)
			super.onBackPressed();
	}
	@Override
	public void setCanceledOnTouchOutside(boolean cancel) 
	{
		super.setCanceledOnTouchOutside(cancel);
		//
	}
	
	public void showCustomDialog(){
		try {
			if(baseActivity != null && baseActivity.get()!=null && !baseActivity.get().isFinishing())
				show();
		} catch (Exception e) {
			Logger.e("Log",e);
			Crashlytics.log(1,getClass().getName(),"Error in Custom Dialog");
			Crashlytics.logException(e);
		}
	}
}
