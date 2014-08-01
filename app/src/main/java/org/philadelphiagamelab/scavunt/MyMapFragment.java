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
 * Created by aaronmsegal on 7/24/14.
 * Manages map fragment initialization and state
 */
public class MyMapFragment extends MapFragment {

    GoogleMap googleMap;
    Double lattitude;
    Double longitude;
    Float zoom;
    int mapType;
    ArrayList<Event> eventsWithMarkers;
    ArrayList<Marker> markers;

    public MyMapFragment() {
        super();
    }

    public static MyMapFragment newInstanace(Double lattitudeIn, Double longitudeIn, Float zoomIn,
                                             int mapTypeIn,
                                             ArrayList<Event> eventsWithMarkersIn) {
        MyMapFragment instance = new MyMapFragment();
        instance.lattitude = lattitudeIn;
        instance.longitude = longitudeIn;
        instance.zoom = zoomIn;
        instance.mapType = mapTypeIn;
        instance.eventsWithMarkers = eventsWithMarkersIn;

        return  instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View v = super.onCreateView(inflater, container, savedState);

        if(savedState!= null) {
            lattitude = savedState.getDouble("Lat", LocationsManager.defaultLatitude);
            longitude = savedState.getDouble("Lng", LocationsManager.defaultLongitude);
            zoom = savedState.getFloat("Zoom", LocationsManager.defaultZoom);
            mapType = LocationsManager.defaultMapType;
        }

        initilizeMap();

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
        if (markers != null) {
            clearMarkers();
        }
        markers = new ArrayList<Marker>();
        for( int i = 0; i < eventsWithMarkers.size(); i++) {
            // Get location for current marker
            Location markerLocation = eventsWithMarkers.get(i).getLocation();
            /**
             * Add marker at the above location, setting title from the events title and snippet to
             * event's inRange bool
             * TODO: Change icon based on inRange
             */
            Marker temp = googleMap.addMarker(new MarkerOptions()
                    .position( new LatLng( markerLocation.getLatitude(), markerLocation.getLongitude()))
                    .title(eventsWithMarkers.get(i).getTitle())
                    .snippet(Boolean.toString(eventsWithMarkers.get(i).isInRange()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_mark)));

            markers.add(temp);
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(googleMap != null) {
            outState.putDouble("Lat", googleMap.getCameraPosition().target.latitude);
            outState.putDouble("Lng", googleMap.getCameraPosition().target.longitude);
            outState.putFloat("Zoom", googleMap.getCameraPosition().zoom);
        }

        super.onSaveInstanceState(outState);
    }
}
