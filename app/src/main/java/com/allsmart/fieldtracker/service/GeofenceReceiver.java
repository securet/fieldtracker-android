package com.allsmart.fieldtracker.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.allsmart.fieldtracker.constants.AppsConstant;
import com.google.android.gms.location.Geofence;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.utils.CalenderUtils;
import com.allsmart.fieldtracker.storage.EventDataSource;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.TimeInOutDetails;

/**
 * Created by allsmartlt218 on 16-01-2017.
 */

public class GeofenceReceiver extends BroadcastReceiver{
	protected Preferences preferences;
	protected EventDataSource dataSource;
	protected Context context;

	private TimeInOutDetails getTimeInOutDetails(String strComments, String strType) {
		String clockDate = CalenderUtils.getCurrentDateWithZone();
		TimeInOutDetails details = new TimeInOutDetails();
		details.setUsername(preferences.getString(Preferences.USERNAME,""));
		details.setClockDate(clockDate);
		details.setActionType(strType);
		details.setComments(strComments);
		details.setLatitude(preferences.getString(Preferences.USERLATITUDE,""));
		details.setLongitude(preferences.getString(Preferences.USERLONGITUDE,""));
		details.setActionImage("");
		details.setIsPushed("false");
		return details;
	}

	public void uploadData() {
		Intent uploadTraIntent=new Intent(context,UploadTransactions.class);
		context.startService(uploadTraIntent);

	}
	@Override
	public void onReceive(Context context, Intent intent) {
		preferences = new Preferences(context);
		this.context = context;
		//GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
		int geoEvent = intent.getIntExtra(AppsConstant.GEOEVENT,-1);
		dataSource = new EventDataSource(context);

		if(geoEvent != 1 && geoEvent != 2) {
			Log.d(MainActivity.TAG, "Error GeofenceReceiver.onReceiver" + geoEvent);
		} else {
			Log.d(MainActivity.TAG, "GeofenceReceiver : Transition -> "+ geoEvent);

			int transitionType = geoEvent;

			if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
				String strComments = "";
				String clockType = "";
				switch (transitionType) {

					case Geofence.GEOFENCE_TRANSITION_ENTER:
						strComments = "InLocation";
						clockType = "clockIn";
						preferences.saveBoolean(Preferences.INLOCATION, true);
						//     Toast.makeText(context,"Time In",Toast.LENGTH_SHORT).show();

						break;

					case Geofence.GEOFENCE_TRANSITION_EXIT:
						strComments = "OutLocation";
						clockType = "clockOut";
						//           Toast.makeText(context,"Time out",Toast.LENGTH_SHORT).show();
						preferences.saveBoolean(Preferences.INLOCATION, false);

						break;
				}

				if(!TextUtils.isEmpty(clockType)) {
					///*dataSource.insertTimeInOutDetails(getTimeInOutDetails(strComments,clockType));
					String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
					TimeInOutDetails details = dataSource.getToday();
					if(details != null && !TextUtils.isEmpty(details.getClockDate())) {
						String lastDate = CalenderUtils.getDateMethod(details.getClockDate(),CalenderUtils.DateMonthDashedFormate);
						String comments = details.getComments();
						Log.d(MainActivity.TAG,clockDate + "            from db" + lastDate);

						if(preferences.getBoolean(Preferences.ISONPREMISE,false)) {
							if (clockDate.equalsIgnoreCase(lastDate)) {
								if(strComments.equalsIgnoreCase("InLocation")){
									if (comments.equalsIgnoreCase("OutLocation")) {
										dataSource.insertTimeInOutDetails(getTimeInOutDetails("InLocation", "clockIn"));
										uploadData();
										//
										String storeName = preferences.getString(Preferences.SITENAME,"");
										if(!storeName.equalsIgnoreCase("Off Site")) {
											sendNotification(transitionType+"",preferences.getString(Preferences.SITENAME,""),"You are entering");
										}
									}
								} else if (strComments.equalsIgnoreCase("OutLocation")) {
									if(comments.equalsIgnoreCase("TimeIn") || comments.equalsIgnoreCase("InLocation")) {
										dataSource.insertTimeInOutDetails(getTimeInOutDetails("OutLocation","clockOut"));
										uploadData();
										//
										String storeName = preferences.getString(Preferences.SITENAME,"");
										if(!storeName.equalsIgnoreCase("Off Site")) {
											sendNotification(transitionType+"",preferences.getString(Preferences.SITENAME,""),"You are Leaving");
										}
									}
								}
							}
						}
					}
				}
				preferences.commit();
			}
		}

	}
	private void sendNotification(String transitionType, String locationName, String message) {

		// Create an explicit content Intent that starts the main Activity
		Intent notificationIntent = new Intent(context, MainActivity.class);

		// Construct a task stack
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		// Adds the main Activity to the task stack as the parent
		stackBuilder.addParentStack(MainActivity.class);

		// Push the content Intent onto the stack
		stackBuilder.addNextIntent(notificationIntent);

		// Get a PendingIntent containing the entire back stack
		PendingIntent notificationPendingIntent = stackBuilder
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get a notification builder that's compatible with platform versions
		// >= 4
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);

		// Set the notification contents
		builder.setSmallIcon(R.drawable.applogo)
				.setContentTitle(transitionType + ": " + locationName)
				.setContentText(message)
				.setContentIntent(notificationPendingIntent);

		// Get an instance of the Notification manager
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Issue the notification
		mNotificationManager.notify(0, builder.build());
	}

}

