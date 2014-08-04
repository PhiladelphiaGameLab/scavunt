package org.philadelphiagamelab.scavunt;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/28/14.
 */
public final class ClusterManager {

    private static ArrayList<EventCluster> clusters;
    private static EventCluster currentCluster;
    private static ArrayList<Event> visibleEvents;

    //Run at start of game, gets story from story builder and sets events in first cluster visible
    public static void startUp() {
        clusters = StoryBuilder.getClusters();

        //Sets first cluster of events visible and assigns them to visibleEvents
        visibleEvents = clusters.get(0).makeEventsVisible();
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
