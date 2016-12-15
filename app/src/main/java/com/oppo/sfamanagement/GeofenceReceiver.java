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
import com.oppo.sfamanagement.database.Preferences;

import java.util.Date;
import java.util.List;

public class GeofenceReceiver extends IntentService {
	private Preferences preferences;
	public GeofenceReceiver() {
		super("GeofenceReceiver");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
		preferences = new Preferences(this);
		if (geoEvent.hasError()) {
			Log.d(MainActivity.TAG, "Error GeofenceReceiver.onHandleIntent");
		} else {
			Log.d(MainActivity.TAG, "GeofenceReceiver : Transition -> "+ geoEvent.getGeofenceTransition());

			int transitionType = geoEvent.getGeofenceTransition();

			if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
			{
				switch (transitionType) {

					case Geofence.GEOFENCE_TRANSITION_ENTER:
						preferences.saveBoolean(Preferences.INLOCATION, true);
						break;

					case Geofence.GEOFENCE_TRANSITION_EXIT:
						preferences.saveBoolean(Preferences.INLOCATION, false);
						break;
				}
				preferences.commit();
			}
		}
	}
}
