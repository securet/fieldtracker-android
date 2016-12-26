package com.oppo.sfamanagement;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.oppo.sfamanagement.database.CalenderUtils;
import com.oppo.sfamanagement.database.EventDataSource;
import com.oppo.sfamanagement.database.NetworkUtils;
import com.oppo.sfamanagement.database.Preferences;
import com.oppo.sfamanagement.model.TimeInOutDetails;
import com.oppo.sfamanagement.service.UploadTransactions;

import java.util.Date;
import java.util.List;

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
						break;

					case Geofence.GEOFENCE_TRANSITION_EXIT:
						strComments = "OutLocation";
						clockType = "clockOut";
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
