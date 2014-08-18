package org.philadelphiagamelab.scavunt;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
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
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Fragments
    //MyMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.play_game);



        // Server test code
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
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
