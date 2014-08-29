package org.philadelphiagamelab.scavunt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by aaronmsegal on 8/18/14.
 *
 * TODO: check to enable  MyLocation Source
 */
public class PlayGame extends Activity implements
        ClusterListFragment.OnTaskListItemInteractionListener,
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    // A request to connect to Location Services
    private LocationRequest locationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;

    // Fragments
    CustomMapFragment mapFragment;
    ClusterListFragment clusterListFragment;
    TextFragment textFragment;
    ImageFragment imageFragment;
    VideoFragment videoFragment;
    AudioFragment audioFragment;

    // Fragment TAGS
    private static String activeFragmentTag;
    private static final String MAP_TAG = "mapFragment";
    private static final String LIST_TAG = "listFragment";
    private static final String TEXT_TAG = "textFragment";
    private static final String IMAGE_TAG = "imageFragment";
    private static final String RESPONSE_TAG = "responseText";
    private static final String SERVICE_AUDIO = "serviceAudioTag";
    private static final String RECEIVE_AND_REPONSE = "receiveAndResponse";
    private static final String AUDIO_TAG = "audioFragment";
    private static final String VIDEO_TAG = "videoFragment";
    private static final String TAKE_PICTURE_TAG = "takePictureFragment";
    private static final String RECORD_VIDEO_TAG = "recordVideoFragment";

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

        ClusterManager.activityOnCreate();

        FragmentManager fragmentManager = getFragmentManager();

        audioFragment = (AudioFragment) fragmentManager.findFragmentByTag(AUDIO_TAG);
        if(audioFragment == null) {
            audioFragment = AudioFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container_1, audioFragment, AUDIO_TAG)
                    .detach(audioFragment)
                    .commit();
        }

        videoFragment = (VideoFragment) fragmentManager.findFragmentByTag(VIDEO_TAG);
        if(videoFragment == null) {
            videoFragment = VideoFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container_1, videoFragment, VIDEO_TAG)
                    .detach(videoFragment)
                    .commit();
        }

        imageFragment = (ImageFragment) fragmentManager.findFragmentByTag(IMAGE_TAG);
        if(imageFragment == null) {
            imageFragment = ImageFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_1, imageFragment, IMAGE_TAG)
                    .detach(imageFragment)
                    .commit();
        }

        textFragment = (TextFragment) fragmentManager.findFragmentByTag(TEXT_TAG);
        if(textFragment == null) {
            textFragment = TextFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_1, textFragment, TEXT_TAG)
                    .detach(textFragment)
                    .commit();
        }

        clusterListFragment = (ClusterListFragment) fragmentManager.findFragmentByTag(LIST_TAG);
        if(clusterListFragment == null) {
            clusterListFragment = ClusterListFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_1, clusterListFragment, LIST_TAG)
                    .detach(clusterListFragment)
                    .commit();

        }

        mapFragment = (CustomMapFragment) fragmentManager.findFragmentByTag(MAP_TAG);
        if (mapFragment == null) {
            mapFragment = CustomMapFragment.newInstanace(
                    LocationUtilities.defaultLatitude,
                    LocationUtilities.defaultLongitude,
                    LocationUtilities.defaultZoom,
                    LocationUtilities.defaultMapType,
                    ClusterManager.getVisibleEventsWithLocations());
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_1, mapFragment, MAP_TAG)
                    .commit();

            activeFragmentTag = MAP_TAG;
        }

        updateFragments();
    }

    @Override
    public void onStop() {
        // If the client is connected
        if (locationClient != null && locationClient.isConnected()) {
            stopPeriodicUpdates();
            // After disconnect() is called, the client is considered "dead".
            locationClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Connect the client. Don't re-start any requests here, wait for onConnected()
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
        if(clusterListFragment != null) {
            clusterListFragment.updateList();
        }
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
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_map) {
            changeFragment(activeFragmentTag, MAP_TAG);
            return true;
        }
        if (id == R.id.action_checkList) {
            changeFragment(activeFragmentTag, LIST_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(String toDetach, String toAttach) {

        if(!toDetach.equals(toAttach)) {
            if (toDetach.equals(activeFragmentTag)) {
                getFragmentManager()
                        .beginTransaction()
                        .attach(getFragmentManager().findFragmentByTag(toAttach))
                        .detach(getFragmentManager().findFragmentByTag(toDetach))
                        .commit();

                activeFragmentTag = toAttach;
            } else {
                Log.e("SWAP FRAGMENT ERROR: ", activeFragmentTag + " is attached, not: " + toDetach);
            }
        }
        /*
        else {
            Log.e("SWAP FRAGMENT MESSAGE: ", toAttach + " already attached");
        }
        */
    }

    @Override
    public void onTaskListItemInteraction(Task clicked) {
        String newTag = null;

        Task.ActivityType activityType = clicked.getActivityType();
        Log.d("New Activity Type: ", activityType.toString());

        if(activityType == Task.ActivityType.RECEIVE_TEXT) {
            newTag = TEXT_TAG;
            textFragment.updateTask(clicked);
        }
        else if(activityType == Task.ActivityType.RECEIVE_IMAGE) {
            newTag = IMAGE_TAG;
            imageFragment.updateTask(clicked);
        }
        else if(activityType == Task.ActivityType.RECEIVE_VIDEO) {
            newTag = VIDEO_TAG;
            videoFragment.updateTask(clicked);
        }
        else if(activityType == Task.ActivityType.RECEIVE_AUDIO) {
            newTag = AUDIO_TAG;
            audioFragment.updateTask(clicked);
        }

        if(newTag != null) {
            changeFragment(LIST_TAG, newTag);
        }
        else {
            Log.d("crap", "crap");
        }
    }
}
