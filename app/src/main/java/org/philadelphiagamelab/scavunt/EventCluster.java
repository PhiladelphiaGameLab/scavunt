package org.philadelphiagamelab.scavunt;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/30/14.
 *
 * TODO: get rid of toActivate, progression will be linear and handled by clustermanager
 */
public class EventCluster {

    private String title;
    private ArrayList<Event> events;
    private ArrayList<Event> eventsToComplete;
    private EventCluster toActivate;
    private ArrayList<EventCluster> visibleClusters;


    public EventCluster (String titleIn, ArrayList<Event> eventsIn,
                         ArrayList<Event> eventsToCompleteIn,
                         EventCluster toActivateIn) {

        this.title = titleIn;
        this.events = eventsIn;
        this.eventsToComplete = eventsToCompleteIn;
        this.toActivate = toActivateIn;
    }

    public EventCluster (String titleIn, ArrayList<Event> eventsIn) {
        this.title = titleIn;
        this.events = eventsIn;
        this.eventsToComplete = null;
        this.toActivate = null;
    }

    public String getTitle() {
        return title;
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
        if (eventsToComplete != null) {
            for(int i = 0; i < eventsToComplete.size(); i++) {
                if (!eventsToComplete.get(i).isComplete()) {
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

    public EventCluster getToActivate() {
        return toActivate;
    }
}
