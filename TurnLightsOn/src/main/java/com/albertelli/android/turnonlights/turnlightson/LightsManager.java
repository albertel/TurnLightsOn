package com.albertelli.android.turnonlights.turnlightson;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/* Notes
    - AlarmManager - to get an Intent at 5pm to turn on location manager
    - boot up intent - to register the alarm, and if right time start running immeadiately
    - settings
        - settable IP
        - set times?
        - set location
        - set distance
    - need app init sequence with bridge
 */
public class LightsManager {
    private static final String TAG = "LightsManager";

    public static final String ACTION_LOCATION = "com.albertelli.android.turnonlights.ACTION_LOCATION";

    private static LightsManager sLightsManager;
    private Context mAppContext;
    private LocationManager mLocationManager;

    private LightsManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LightsManager get(Context c) {
        if (sLightsManager == null) {
            sLightsManager = new LightsManager(c.getApplicationContext());
        }
        return sLightsManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;

        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
                mLocationManager.removeUpdates(pi);
                pi.cancel();
        }
    }

    public boolean isTrackingLocation() {
        return getLocationPendingIntent(false) != null;
    }
}
