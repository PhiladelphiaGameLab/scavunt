package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 8/18/14.
 */
public class PlayGame extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
