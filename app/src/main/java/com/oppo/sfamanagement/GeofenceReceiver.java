package com.oppo.sfamanagement;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.oppo.sfamanagement.database.EventDataSource;

import java.util.Date;
import java.util.List;

public class GeofenceReceiver extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private SharedPreferences myPrefs;
	private SharedPreferences.Editor prefsEditor;
	public GeofenceReceiver() {
		super("GeofenceReceiver");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
		myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
		if (geoEvent.hasError()) {
			Log.d(MainActivity.TAG, "Error GeofenceReceiver.onHandleIntent");
		} else {
			Log.d(MainActivity.TAG, "GeofenceReceiver : Transition -> "+ geoEvent.getGeofenceTransition());

			int transitionType = geoEvent.getGeofenceTransition();

			if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
			{
				prefsEditor =  myPrefs.edit();
				String transitionName = "";
				switch (transitionType) {

					case Geofence.GEOFENCE_TRANSITION_ENTER:
						transitionName = "enter";
						prefsEditor.putBoolean("inLocation", true);
						break;

					case Geofence.GEOFENCE_TRANSITION_EXIT:
						transitionName = "exit";
						prefsEditor.putBoolean("inLocation", false);
						break;
				}
				prefsEditor.commit();
//				List<Geofence> triggerList = geoEvent.getTriggeringGeofences();

//				for (Geofence geofence : triggerList)
//				{
////					SimpleGeofence sg = SimpleGeofenceStore.getInstance("All Smart TechnoSolution", 17.463459, 78.482243,100).getSimpleGeofences().get(geofence.getRequestId());
//
////					Toast.makeText(getBaseContext(),transitionName,Toast.LENGTH_LONG).show();
////					String date = DateFormat.format("yyyy-MM-dd hh:mm:ss",new Date()).toString();
////					EventDataSource eds = new EventDataSource(getApplicationContext());
////					eds.create(transitionName, date, geofence.getRequestId());
////					eds.close();
//
//				//	GeofenceNotification geofenceNotification = new GeofenceNotification(
//				//			this);
//				//	geofenceNotification
//				//			.displayNotification(sg, transitionType);
//				}
			}
		}
	}
}
