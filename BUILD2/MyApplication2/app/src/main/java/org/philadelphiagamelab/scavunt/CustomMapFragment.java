package org.philadelphiagamelab.scavunt;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 8/18/14.
 */
public class CustomMapFragment extends MapFragment {

    GoogleMap googleMap;
    Double lattitude;
    Double longitude;
    Float zoom;
    int mapType;
    ArrayList<Event> eventsWithMarkers;
    ArrayList<Marker> markers;

    public CustomMapFragment() {
        super();
    }

    public static CustomMapFragment newInstanace(
            Double lattitudeIn,
            Double longitudeIn,
            Float zoomIn,
            int mapTypeIn,
            ArrayList<Event> eventsWithMarkersIn) {
        CustomMapFragment instance = new CustomMapFragment();
        instance.lattitude = lattitudeIn;
        instance.longitude = longitudeIn;
        instance.zoom = zoomIn;
        instance.mapType = mapTypeIn;
        instance.eventsWithMarkers = eventsWithMarkersIn;

        return  instance;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View v = super.onCreateView(inflater, container, savedState);

        if(googleMap == null) {
            initilizeMap();
        }

        return v;
    }

    private void initilizeMap() {
        googleMap = getMap();
        googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(new LatLng(lattitude, longitude), zoom));
        googleMap.setMapType(mapType);

        eventsWithMarkers = ClusterManager.getVisibleEventsWithLocations();

        updateMarkers();
    }

    /**
     * Removes markers from marks arrayList and calls each markers remove() function to remove it
     * from googleMap
     */
    private void clearMarkers() {
        for(int i = 0; i < markers.size(); i++) {
            markers.remove(i).remove();
        }
    }

    /**
     * Calls clearMarkers and then adds new markers based on most current info.
     */
    public void updateMarkers() {
        if (googleMap!=null) {
            if (markers != null) {
                clearMarkers();
            }
            markers = new ArrayList<Marker>();
            for (int i = 0; i < eventsWithMarkers.size(); i++) {
                // Get location for current marker
                Location markerLocation = eventsWithMarkers.get(i).getLocation();

                /**
                 * Add marker at the above location, setting title from the events title and snippet to
                 * event's inRange bool
                 */
                Float markerColor;
                Event eventToMark = eventsWithMarkers.get(i);
                //TODO: should complete events have a marker displayed?
                if(eventToMark.isComplete()) {
                    markerColor = BitmapDescriptorFactory.HUE_AZURE;
                }
                else if(eventToMark.isInRange()) {
                    markerColor = BitmapDescriptorFactory.HUE_GREEN;
                }
                else {
                    markerColor = BitmapDescriptorFactory.HUE_RED;
                }

                Marker temp = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(markerLocation.getLatitude(), markerLocation.getLongitude()))
                        .title(eventToMark.getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));

                markers.add(temp);
            }
        }
    }

    @Override
    public void onDetach() {

        //Where save should happen probably

        super.onDetach();
    }
}
