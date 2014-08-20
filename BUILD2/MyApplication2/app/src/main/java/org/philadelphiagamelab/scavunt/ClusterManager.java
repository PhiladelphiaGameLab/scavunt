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
    public static void startUp() {
        clusters = GameHolder.getClusters();
        currentCluster = clusters.get(currentClusterIndex);

        //Sets first cluster of events visible and assigns them to visibleEvents
        visibleEvents = currentCluster.makeEventsVisible();
    }

    public static void checkProgression() {
        if (currentCluster.isComplete() && clusters.get(++currentClusterIndex) != null) {
            currentCluster = clusters.get(currentClusterIndex);
            ArrayList<Event> temp = currentCluster.makeEventsVisible();
            for(int i = 0; i < visibleEvents.size(); i++) {
                temp.add(visibleEvents.get(i));
            }
            visibleEvents = temp;
        }
        else {
            //story over
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
