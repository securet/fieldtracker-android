package com.oppo.sfamanagement.geofencing;

/**
 * Created by allsmartlt218 on 17-01-2017.
 */

public final class GeofenceUtils {

    // Used to track what type of geofence removal request was made.
    public enum REMOVE_TYPE {INTENT, LIST}

    // Used to track what type of request is in process
    public enum REQUEST_TYPE {ADD, REMOVE}

    /*
     * A log tag for the application
     */
    public static final String APPTAG = "Geofence Detection";

    // Intent actions
    public static final String ACTION_CONNECTION_ERROR =
            "com.oppo.sfamanagement.geofencing.ACTION_CONNECTION_ERROR";

    public static final String ACTION_CONNECTION_SUCCESS =
            "com.oppo.sfamanagement.geofencing.ACTION_CONNECTION_SUCCESS";

    public static final String ACTION_GEOFENCES_ADDED =
            "com.oppo.sfamanagement.geofencing.ACTION_GEOFENCES_ADDED";

    public static final String ACTION_GEOFENCES_REMOVED =
            "com.oppo.sfamanagement.geofencing.ACTION_GEOFENCES_DELETED";

    public static final String ACTION_GEOFENCE_ERROR =
            "com.oppo.sfamanagement.geofencing.ACTION_GEOFENCES_ERROR";

    public static final String ACTION_GEOFENCE_TRANSITION =
            "com.oppo.sfamanagement.geofencing.ACTION_GEOFENCE_TRANSITION";

    public static final String ACTION_GEOFENCE_TRANSITION_ERROR =
            "com.oppo.sfamanagement.geofencing.ACTION_GEOFENCE_TRANSITION_ERROR";

    // The Intent category used by all Location Services sample apps
    public static final String CATEGORY_LOCATION_SERVICES =
            "com.oppo.sfamanagement.geofencing.CATEGORY_LOCATION_SERVICES";

    // Keys for extended data in Intents
    public static final String EXTRA_CONNECTION_CODE =
            "com.oppo.sfamanagement.geofencing.EXTRA_CONNECTION_CODE";

    public static final String EXTRA_CONNECTION_ERROR_CODE =
            "com.oppo.sfamanagement.geofencing.EXTRA_CONNECTION_ERROR_CODE";

    public static final String EXTRA_CONNECTION_ERROR_MESSAGE =
            "com.oppo.sfamanagement.geofencing.EXTRA_CONNECTION_ERROR_MESSAGE";

    public static final String EXTRA_GEOFENCE_STATUS =
            "com.oppo.sfamanagement.geofencing.EXTRA_GEOFENCE_STATUS";

    /*
     * Keys for flattened geofences stored in SharedPreferences
     */
    public static final String KEY_LATITUDE = "com.oppo.sfamanagement.geofencing.KEY_LATITUDE";

    public static final String KEY_LONGITUDE = "com.oppo.sfamanagement.geofencing.KEY_LONGITUDE";

    public static final String KEY_RADIUS = "com.oppo.sfamanagement.geofencing.KEY_RADIUS";

    public static final String KEY_EXPIRATION_DURATION =
            "com.oppo.sfamanagement.geofencing.KEY_EXPIRATION_DURATION";

    public static final String KEY_TRANSITION_TYPE =
            "com.oppo.sfamanagement.geofencing.KEY_TRANSITION_TYPE";

    // The prefix for flattened geofence keys
    public static final String KEY_PREFIX =
            "com.oppo.sfamanagement.geofencing.KEY";

    // Invalid values, used to test geofence storage when retrieving geofences
    public static final long INVALID_LONG_VALUE = -999l;

    public static final float INVALID_FLOAT_VALUE = -999.0f;

    public static final int INVALID_INT_VALUE = -999;

    /*
     * Constants used in verifying the correctness of input values
     */
    public static final double MAX_LATITUDE = 90.d;

    public static final double MIN_LATITUDE = -90.d;

    public static final double MAX_LONGITUDE = 180.d;

    public static final double MIN_LONGITUDE = -180.d;

    public static final float MIN_RADIUS = 1f;

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // A string of length 0, used to clear out input fields
    public static final String EMPTY_STRING = new String();

    public static final CharSequence GEOFENCE_ID_DELIMITER = ",";

}
