package org.philadelphiagamelab.scavunt;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/28/14.
 *
 *
 */
public final class ClusterManager {

    private static ArrayList<EventCluster> clusters;
    private static EventCluster currentCluster;
    private static ArrayList<Event> visibleEvents;
    private static int currentClusterIndex = 0;

    //Run at start of game, gets story from story builder and sets events in first cluster visible
    //Actually called from PlayGame onCreate,
    public static void startUp() {

        clusters = GameHolder.getClusters();

        //TODO: add code to pull currentClusterIndex from local database when it exists

        //This might not work, but if the index is not zero then visibleEvents should also
        // already have some events stored inside
        if(currentClusterIndex == 0) {
            visibleEvents = new ArrayList<Event>();
            currentCluster = clusters.get(currentClusterIndex);
            visibleEvents.addAll(currentCluster.makeEventsVisible());
        }
    }

    public static void checkProgression() {

        if(currentCluster.isComplete()) {

            //if not at end of progression
            if((currentClusterIndex + 1) < clusters.size()) {
                currentCluster = clusters.get(++currentClusterIndex);

                //get visible events from new current cluster
                ArrayList<Event> temp = currentCluster.makeEventsVisible();

                //Add already visible events to new ArrayList of new visible events
                temp.addAll(visibleEvents);

                visibleEvents = temp;
            }
            else {
                //game over
            }

        }
    }

    public static int getNumberOfEventsVisible() {
        return visibleEvents.size();
    }

    public static Event getVisibleEvent(int i) {
        return visibleEvents.get(i);
    }

    public static ArrayList<Event> getVisibleEventsWithLocations() {

        ArrayList<Event> eventsWithLocations = new ArrayList<Event>();

        for (int i = 0; i < visibleEvents.size(); i++) {
            if(visibleEvents.get(i).getLocation() != null) {
                eventsWithLocations.add(visibleEvents.get(i));
            }
        }

        return eventsWithLocations;
    }
}
