package com.oppo.sfamanagement.broadcasters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.oppo.sfamanagement.MainActivity;
import com.oppo.sfamanagement.R;
import com.oppo.sfamanagement.database.CalenderUtils;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.TimeInOutDetails;
import com.oppo.sfamanagement.service.UploadTransactions;

/**
 * Created by allsmartlt218 on 16-01-2017.
 */

public class GeofenceBroadcaster extends BroadcastReceiver{
    protected Preferences preferences;
    protected EventDataSource dataSource;
    protected Context context;

    private TimeInOutDetails getTimeInOutDetails(String strComments, String strType) {
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
        Intent uploadTraIntent=new Intent(context,UploadTransactions.class);
        context.startService(uploadTraIntent);

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = new Preferences(context);
        this.context = context;
        GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
        dataSource = new EventDataSource(context);

        if(geoEvent.hasError()) {
            Log.d(MainActivity.TAG, "Error GeofenceReceiver.onHandleIntent");
        } else {
            Log.d(MainActivity.TAG, "GeofenceReceiver : Transition -> "+ geoEvent.getGeofenceTransition());

            int transitionType = geoEvent.getGeofenceTransition();

            if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                String strComments = "";
                String clockType = "";
                switch (transitionType) {

                    case Geofence.GEOFENCE_TRANSITION_ENTER:
                        strComments = "InLocation";
                        clockType = "clockIn";
                        preferences.saveBoolean(Preferences.INLOCATION, true);
                        Toast.makeText(context,"Time In",Toast.LENGTH_SHORT).show();
                        break;

                    case Geofence.GEOFENCE_TRANSITION_EXIT:
                        strComments = "OutLocation";
                        clockType = "clockOut";
                        Toast.makeText(context,"Time out",Toast.LENGTH_SHORT).show();
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
                .setContentText("Fencing event Occured")
                .setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    }

