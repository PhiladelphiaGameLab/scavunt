package org.philadelphiagamelab.scavunt;


import com.google.android.gms.maps.GoogleMap;

 /**
 * Created by aaronmsegal on 7/25/14.
 *
 * Hold default location info and maybe eventually manage active list of event locations?
 * We may want to split this into more classes or add complexity and locations become more than
 * just a lat and lng and instead constitute actual events.
 */
public final class LocationsManager {

    public static final Double defaultLatitude = 39.9514285d;
    public static final Double defaultLongitude = -75.1629362d;
    public static final Float defaultZoom = 12.5f;
    public static final int defaultMapType = GoogleMap.MAP_TYPE_HYBRID;

    //in meters
    public static final Float activationRangeNormal = 40.0f;
}