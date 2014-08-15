package org.philadelphiagamelab.scavunt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

//TODO: TakePictureFragment is broken and crashes onrecreate, it has been remove from this activity
// but needs to be fixed and added back in

//TODO: change onLocationChange change and associated updates to a service

public class MyActivity extends Activity implements
        ListFragment.OnFragmentInteractionListener,
        TextFragment.OnFragmentInteractionListener,
        ImageFragment.OnFragmentInteractionListener,
        AudioFragment.OnFragmentInteractionListener,
        VideoFragment.OnFragmentInteractionListener,
        TakePictureFragment.OnFragmentInteractionListener,
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {


    private static Context context;

    private  int locationTestCounter;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;

    // Fragments
    MyMapFragment mapFragment;
    ListFragment listFragment;
    TextFragment textFragment1;
    ImageFragment imageFragment1;
    AudioFragment audioFragment1;
    VideoFragment videoFragment1;
    TakePictureFragment takePictureFragment1;

    //test TextView for displaying closest location
    private TextView testDisplay;

    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        if( servicesConnected() ) {

            setContentView(R.layout.activity_my);

            //Temporary TextView for testing
            testDisplay = (TextView) findViewById(R.id.position_display);

            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

            // Set the update interval and fastest interval
            mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Open Shared Preferences
            mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            // Get an editor
            mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
            mLocationClient = new LocationClient(this, this, this);

            //Sets up Map and List fragments
            if (savedInstanceState == null) {

                ClusterManager.startUp();

                mapFragment = MyMapFragment.newInstanace(
                        LocationUtils.defaultLatitude,
                        LocationUtils.defaultLongitude,
                        LocationUtils.defaultZoom,
                        LocationUtils.defaultMapType,
                        ClusterManager.getVisibleEventsWithLocations());

                listFragment = ListFragment.newInstance();

                textFragment1 = TextFragment.newInstance(R.string.testString, R.layout.text_fragment_default);
                imageFragment1 = ImageFragment.newInstance(R.drawable.test_image, R.layout.image_fragment_default);
                audioFragment1 = AudioFragment.newInstance(R.raw.test_song, R.layout.audio_default_fragment);
                videoFragment1 = VideoFragment.newInstance(R.raw.test_video, R.layout.video_default_fragment);
                takePictureFragment1 = TakePictureFragment.newInstance(R.id.bitmapKey1, R.layout.take_picture_default_fragment);

                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_1, textFragment1, "textFragment1")
                        .detach(textFragment1)
                        .add(R.id.container_1, imageFragment1, "imageFragment1")
                        .detach(imageFragment1)
                        .add(R.id.container_1, audioFragment1, "audioFragment1")
                        .detach(audioFragment1)
                        .add(R.id.container_1, videoFragment1, "videoFragment1")
                        .detach(videoFragment1)
                        .add(R.id.container_1,takePictureFragment1,"takePictureFragment")
                        .detach(takePictureFragment1)
                        .add(R.id.container_1, listFragment, "listFragment1")
                        .detach(listFragment)
                        .add(R.id.container_1, mapFragment, "mapFragment1")
                        .commit();
            }
        }
    }



    /*
    * Called when the Activity is no longer visible at all.
    * Stop updates and disconnect.
    */
    @Override
    public void onStop() {

        // If the client is connected
        if (mLocationClient != null && mLocationClient.isConnected()) {
            stopPeriodicUpdates();
            // After disconnect() is called, the client is considered "dead".
            mLocationClient.disconnect();
        }

        // TODO: Modify this, so the services stop properly
        Intent stopMusicService = new Intent(this, AudioService.class);
        stopService(stopMusicService);


        super.onStop();
    }

    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // TODO: Modify this, so the services stop properly
        Intent stopMusicService = new Intent(this, AudioService.class);
        stopService(stopMusicService);

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onConnected()
         */

        if(mLocationClient != null) {
            mLocationClient.connect();
        }

    }

    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {

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
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));
                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtils.APPTAG,
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
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = ErrorDialogFragment.newInstance(dialog);
                errorFragment.show(getFragmentManager(), LocationUtils.APPTAG);
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
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

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

        //Toast Testing
        locationTestCounter += 1;
        if (locationTestCounter >= 2){
            Toast.makeText(this,location.getLatitude() + ":"+ location.getLongitude(),Toast.LENGTH_SHORT).show();
            locationTestCounter =0;
        }


        //For Testing Only
        ArrayList<Event> eventsWithMarkerLocations = ClusterManager.getVisibleEventsWithLocations();
        int closestIndex = 0;
        double closestDistance =
                location.distanceTo(eventsWithMarkerLocations.get(0).getLocation());
        for(int i = 1; i < eventsWithMarkerLocations.size(); i++) {
            if(location.distanceTo(eventsWithMarkerLocations.get(i).getLocation()) < closestDistance) {
                closestDistance = location.distanceTo(eventsWithMarkerLocations.get(0).getLocation());
                closestIndex = i;
            }
        }
        testDisplay.setText("Closest: " + eventsWithMarkerLocations.get(closestIndex).getTitle()
                + "\nDistance(m): " + Double.toString(closestDistance));
        //end test code


        //Real Code
        //Loop though all visible events and update in range based on location
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
        if(listFragment != null) {
            listFragment.updateList();
        }
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
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
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment that will show the error dialog
            ErrorDialogFragment errorFragment = ErrorDialogFragment.newInstance(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getFragmentManager(), LocationUtils.APPTAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("SCAVUNT");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_map) {
            onMapClicked(item);
            return true;
        }
        if (id == R.id.action_task_checkList) {
            onTaskListClicked(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapClicked(MenuItem menuItem) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment currentFragment = getCurrentFragment();

            if(currentFragment != mapFragment){
                fragmentTransaction.detach(currentFragment).attach(mapFragment);
                currentFragment = mapFragment;
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

    public void onTaskListClicked(MenuItem menuItem) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment currentFragment = getCurrentFragment();

            if(currentFragment != listFragment){
                fragmentTransaction.detach(currentFragment).attach(listFragment);
                currentFragment = listFragment;
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

    public Fragment getCurrentFragment(){
        Fragment tempCurrentFragment = null;
        if(mapFragment != null && mapFragment.isVisible()){
            tempCurrentFragment = mapFragment;
        }
        else if(listFragment != null && listFragment.isVisible()){
            tempCurrentFragment = listFragment;
        }
        else if(textFragment1 != null && textFragment1.isVisible()){
            tempCurrentFragment = textFragment1;
        }
        else if(audioFragment1 != null && audioFragment1.isVisible()){
            tempCurrentFragment = audioFragment1;
        }
        else if(takePictureFragment1 != null && takePictureFragment1.isVisible()){
            tempCurrentFragment = takePictureFragment1;
        }
        else if (imageFragment1!=null && imageFragment1.isVisible()){
            tempCurrentFragment = imageFragment1;
        }
        else if(videoFragment1 != null && videoFragment1.isVisible()){
            tempCurrentFragment = videoFragment1;
        }

        return tempCurrentFragment;
    }

    @Override
    public void onFragmentInteraction(Task task) {
        //TEST CODE
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if(task.getActivityType() == Task.ActivityType.RECEIVE_TEXT) {
            textFragment1.updateTask(task);
            fragmentTransaction.detach(listFragment).attach(textFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //TEST CODE -- IMAGEVIEW
        else if(task.getActivityType() == Task.ActivityType.RECEIVE_IMAGE){
            imageFragment1.updateTask(task);
            fragmentTransaction.detach(listFragment).attach(imageFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //TEST CODE -- AUDIOVIEW
        else if(task.getActivityType() == Task.ActivityType.RECEIVE_AUDIO){
            audioFragment1.updateTask(task);
            fragmentTransaction.detach(listFragment).attach(audioFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //TEST CODE -- VIDEOVIEW
        else if(task.getActivityType() == Task.ActivityType.RECEIVE_VIDEO){
            videoFragment1.updateTask(task);
            fragmentTransaction.detach(listFragment).attach(videoFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //TEST CODE -- TAKE PICTURE


        else if(task.getActivityType() == Task.ActivityType.TAKE_PICTURE){
            takePictureFragment1.updateTask(task);
            fragmentTransaction.detach(listFragment).attach(takePictureFragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        //END TEST CODE
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    public static Context getMyActivityContext(){
        return context;
    }
}
