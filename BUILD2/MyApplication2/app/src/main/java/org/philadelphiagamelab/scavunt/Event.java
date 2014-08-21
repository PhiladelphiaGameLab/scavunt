package org.philadelphiagamelab.scavunt;

import android.location.Location;
import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/28/14.
 *
 *
 */
public class Event {

    private String title;
    private Location location;
    private float distance;
    private ArrayList<Task> childrenTasks;
    private boolean inRange;
    private boolean visible;

    public Event (String titleIn, Location locationIn, float distanceIn,
                  ArrayList<Task> childrenTasksIn) {
        this.title = titleIn;
        this.location = locationIn;
        this.distance = distanceIn;
        this.childrenTasks = childrenTasksIn;
        this.inRange = false;
        this.visible = false;
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public float getDistance() {
        return distance;
    }

    public int getNumberOfTasksVisible() {
        int taskVisibleCount = 0;

        for( int i = 0; i < childrenTasks.size(); i++) {
            if( childrenTasks.get(i).isVisible() ) {
                taskVisibleCount++;
            }
        }

        return taskVisibleCount;
    }
    public Task getVisibleTask(int index) {
        ArrayList<Task> visibleTasks = new ArrayList<Task>();
        for(int i = 0; i < childrenTasks.size(); i++) {
            if(childrenTasks.get(i).isVisible()) {
                visibleTasks.add(childrenTasks.get(i));
            }
        }
        return visibleTasks.get(index);
    }

    public boolean updateInRange(Location userLocation) {
        // If event has no location or is in range set inRange to true
        // first check protects against null reference in second check
        //TODO:currently the server cannot return a null location, distance must be <=0
        if(distance <=0 ||location == null || location.distanceTo(userLocation) < distance) {
            //update if not already in range
            if( !inRange ) {
                inRange = true;
                updateTasks();
            }
        }
        else {
            //update if previously in range
            if( inRange ) {
                inRange = true;
                updateTasks();
            }
        }
        return inRange;
    }

    public void updateTasks() {
        for(int i = 0; i < childrenTasks.size(); i++) {
            childrenTasks.get(i).updateVisibility(inRange);
        }
    }

    public boolean isInRange() {
        return inRange;
    }

    public void setVisible (Boolean activeStatus) {
        this.visible = activeStatus;
    }
    public boolean isVisible () {
        return visible;
    }

    public boolean isComplete() {
        if (childrenTasks != null) {
            for(int i = 0; i < childrenTasks.size(); i++) {
                if (!childrenTasks.get(i).isComplete()) {
                    return false;
                }
            }
        }
        return true;
    }
}
