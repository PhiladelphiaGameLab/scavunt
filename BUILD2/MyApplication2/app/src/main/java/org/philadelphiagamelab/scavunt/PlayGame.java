package org.philadelphiagamelab.scavunt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 8/18/14.
 */
public class PlayGame extends Activity implements
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    // A request to connect to Location Services
    private LocationRequest locationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;

    // Fragments
    CustomMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.play_game);

        // Create a new global location parameters object
        locationRequest = LocationRequest.create();
        // Set the update interval and fastest interval
        locationRequest.setInterval(LocationUtilities.UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(LocationUtilities.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        /*
        * Create a new location client, using the enclosing class (PlayGame) to
        * handle callbacks.
        */
        locationClient = new LocationClient(this, this, this);

        //Setup fragments
        if (savedInstanceState == null) {

            ClusterManager.startUp();

            mapFragment = CustomMapFragment.newInstanace(
                    LocationUtilities.defaultLatitude,
                    LocationUtilities.defaultLongitude,
                    LocationUtilities.defaultZoom,
                    LocationUtilities.defaultMapType,
                    ClusterManager.getVisibleEventsWithLocations());

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_1, mapFragment, "mapFragment1")
                    .commit();

        }

        /* Server test code
        setContentView(R.layout.display_game);
        ArrayList<EventCluster> clusters = GameHolder.getClusters();

        String displayText = "";

        for(int c = 0; c < clusters.size(); c++) {
            displayText += "Cluster: " + clusters.get(c).getID() + "\n";

            for(int e = 0; e < clusters.get(c).getAmountOfEvents(); e++) {
                displayText += "\tEvent: " + clusters.get(c).getEvent(e).getTitle() + "\n";

                for(int t = 0; t < clusters.get(c).getEvent(e).getNumberOfTasksVisible(); t++ ) {
                    displayText += "\t\tTask: " + clusters.get(c).getEvent(e).getVisibleTask(t).getTitle() + "\n";
                }
            }
        }

        TextView displayGame = (TextView) findViewById(R.id.game_text_display);
        displayGame.setText(displayText);
        //*/
    }

    @Override
    public void onStop() {

        // If the client is connected
        if (locationClient != null && locationClient.isConnected()) {
            stopPeriodicUpdates();
            // After disconnect() is called, the client is considered "dead".
            locationClient.disconnect();
        }

        /*
        // TODO: Modify this, so the services stop properly
        Intent stopMusicService = new Intent(this, AudioService.class);
        stopService(stopMusicService);
        */


        super.onStop();
    }

    @Override
    public void onPause() {

        /*
        // TODO: Modify this, so the services stop properly
        Intent stopMusicService = new Intent(this, AudioService.class);
        stopService(stopMusicService);
        */

        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onConnected()
         */

        if(locationClient != null) {
            locationClient.connect();
        }
    }

    @Override
    public void onResume() {
        servicesConnected();
        super.onResume();
    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtilities.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtilities.APPTAG, getString(R.string.resolved));
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtilities.APPTAG, getString(R.string.no_resolution));
                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtilities.APPTAG,
                        getString(R.string.unknown_activity_request_code, requestCode));
                break;
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtilities.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = ErrorDialogFragment.newInstance(dialog);
                errorFragment.show(getFragmentManager(), LocationUtilities.APPTAG);
            }
            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        startPeriodicUpdates();
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        stopPeriodicUpdates();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtilities.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }


    /**
     * Called by LocationListener when the location has changed.
     *
     * This is where the users position is compared against event locations
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {
        for(int i = 0; i < ClusterManager.getNumberOfEventsVisible(); i++) {
            ClusterManager.getVisibleEvent(i).updateInRange(location);
        }

        updateFragments();
    }

    /**
     * Updates fragments in response to call from onLocationChanged
     */
    private void updateFragments() {
        if(mapFragment != null) {
            mapFragment.updateMarkers();
        }
        /*
        if(listFragment != null) {
            listFragment.updateList();
        }
        */
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates(this);
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtilities.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment that will show the error dialog
            ErrorDialogFragment errorFragment = ErrorDialogFragment.newInstance(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getFragmentManager(), LocationUtilities.APPTAG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_map) {
            //onMapClicked(item);
            return true;
        }
        if (id == R.id.action_checkList) {
            //onListClicked(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
