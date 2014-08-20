package org.philadelphiagamelab.scavunt;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/30/14.
 *
 *
 */
public class EventCluster {

    private int id;
    private ArrayList<Event> events;
    private ArrayList<EventCluster> visibleClusters;


    public EventCluster (int idIn, ArrayList<Event> eventsIn) {
        this.id = idIn;
        this.events = eventsIn;
    }

    public int getID() {
        return id;
    }

    public int getAmountOfEvents() {
        return events.size();
    }

    public Event getEvent(int index) {
        return events.get(index);
    }

    public int getEventsVisible() {
        int eventsVisible = 0;
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).isVisible()) {
                eventsVisible++;
            }
        }
        return eventsVisible;
    }

    public boolean isComplete() {
        if (events != null) {
            for(int i = 0; i < events.size(); i++) {
                if (!events.get(i).isComplete()) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Event> makeEventsVisible() {
        for(int i = 0; i < events.size(); i++) {
            events.get(i).setVisible(true);
        }
        return events;
    }
}