package com.allsmart.fieldtracker.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.allsmart.fieldtracker.R;
import com.allsmart.fieldtracker.activity.MainActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.allsmart.fieldtracker.utils.CalenderUtils;
import com.allsmart.fieldtracker.storage.EventDataSource;
import com.allsmart.fieldtracker.storage.Preferences;
import com.allsmart.fieldtracker.model.TimeInOutDetails;

public class GeofenceReceiver extends IntentService {
	private Preferences preferences;
	private EventDataSource dataSource;
	public GeofenceReceiver() {
		super("GeofenceReceiver");
	}

    private TimeInOutDetails getTimeInOutDetails(String strComments,String strType) {
        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateFormate);
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
        Intent uploadTraIntent=new Intent(this,UploadTransactions.class);
        this.startService(uploadTraIntent);
    }
	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
		preferences = new Preferences(this);
		dataSource = new EventDataSource(this);
		if (geoEvent.hasError()) {
			Log.d(MainActivity.TAG, "Error GeofenceReceiver.onHandleIntent");
		} else {
			Log.d(MainActivity.TAG, "GeofenceReceiver : Transition -> "+ geoEvent.getGeofenceTransition());

			int transitionType = geoEvent.getGeofenceTransition();

			if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
			{
				String strComments = "";
				String clockType = "";
				switch (transitionType) {

					case Geofence.GEOFENCE_TRANSITION_ENTER:
						strComments = "InLocation";
						clockType = "clockIn";
						preferences.saveBoolean(Preferences.INLOCATION, true);
                        Toast.makeText(getApplicationContext(),"Time In",Toast.LENGTH_SHORT).show();
                        break;

					case Geofence.GEOFENCE_TRANSITION_EXIT:
						strComments = "OutLocation";
						clockType = "clockOut";
						Toast.makeText(getApplicationContext(),"Time out",Toast.LENGTH_SHORT).show();
						preferences.saveBoolean(Preferences.INLOCATION, false);
						break;
				}
				sendNotification(transitionType+"",preferences.getString(Preferences.SITENAME,""));
				if(!TextUtils.isEmpty(clockType)) {
					///*dataSource.insertTimeInOutDetails(getTimeInOutDetails(strComments,clockType));
                    String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
                    TimeInOutDetails details = dataSource.getToday();
                    String lastDate = CalenderUtils.getDateMethod(details.getClockDate(),CalenderUtils.DateMonthDashedFormate);
					String comments = details.getComments();
                    if (clockDate.equalsIgnoreCase(lastDate)) {
                        if(strComments.equalsIgnoreCase("InLocation")){
							if (comments.equalsIgnoreCase("OutLocation")) {
								dataSource.insertTimeInOutDetails(getTimeInOutDetails("InLocation", "clockIn"));
								uploadData();
							}
                        } else if (strComments.equalsIgnoreCase("OutLocation")) {
							if(comments.equalsIgnoreCase("TimeIn") || comments.equalsIgnoreCase("InLocation")) {
                            	dataSource.insertTimeInOutDetails(getTimeInOutDetails("OutLocation","clockOut"));
								uploadData();
                        	}
						}
                    }
            	}
				preferences.commit();
			}
		}
	}
	private void sendNotification(String transitionType, String locationName) {

		// Create an explicit content Intent that starts the main Activity
		Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

		// Construct a task stack
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

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
				getApplicationContext());

		// Set the notification contents
		builder.setSmallIcon(R.drawable.applogo)
				.setContentTitle(transitionType + ": " + locationName)
				.setContentText("Fencing event Occured")
				.setContentIntent(notificationPendingIntent);

		// Get an instance of the Notification manager
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Issue the notification
		mNotificationManager.notify(0, builder.build());
	}
}
