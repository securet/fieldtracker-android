package com.oppo.sfamanagement.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.oppo.sfamanagement.MainActivity;
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

    }

