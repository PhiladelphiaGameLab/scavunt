package org.philadelphiagamelab.scavunt;

import android.location.Location;
import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/28/14.
 */
public class Event {

    private String title;
    private Location location;
    private float range;
    private ArrayList<Task> childrenTasks;
    private ArrayList<Task> tasksToComplete;
    private boolean inRange;
    private boolean visible;

    //in meters
    public static final float defaultRange = LocationsManager.activationRangeNormal;

    public Event (String titleIn, Location locationIn, float defaultRangeIn,
                  ArrayList<Task> childrenTasksIn, ArrayList<Task> tasksToCompleteIn) {
        this.title = titleIn;
        this.location = locationIn;
        this.range = defaultRangeIn;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = tasksToCompleteIn;
        this.inRange = false;
        this.visible = false;
    }

    public Event (String titleIn, Location locationIn, float defaultRangeIn,
                  ArrayList<Task> childrenTasksIn) {
        this.title = titleIn;
        this.location = locationIn;
        this.range = defaultRangeIn;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = null;
        this.inRange = false;
        this.visible = false;
    }

    public Event (String titleIn, Location locationIn, ArrayList<Task> childrenTasksIn,
                  ArrayList<Task> tasksToCompleteIn) {
        this.title = titleIn;
        this.location = locationIn;
        this.range = defaultRange;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = tasksToCompleteIn;
        this.inRange = false;
        this.visible = false;
    }

    public Event (String titleIn, Location locationIn, ArrayList<Task> childrenTasksIn) {
        this.title = titleIn;
        this.location = locationIn;
        this.range = defaultRange;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = null;
        this.inRange = false;
        this.visible = false;
    }

    public Event (String titleIn, ArrayList<Task> childrenTasksIn,
                  ArrayList<Task> tasksToCompleteIn) {
        this.title = titleIn;
        this.location = null;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = tasksToCompleteIn;
        this.inRange = true;
        this.visible = false;
    }

    public Event (String titleIn, ArrayList<Task> childrenTasksIn) {
        this.title = titleIn;
        this.location = null;
        this.childrenTasks = childrenTasksIn;
        this.tasksToComplete = null;
        this.inRange = true;
        this.visible = false;
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public float getRange() {
        return range;
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
    public Task getTask(int index) {
        return childrenTasks.get(index);
    }

    public boolean updateInRange(Location userLocation) {
        // If event has no location or is in range set inRange to true
        // first check protects against null reference in second check
        if(location == null || location.distanceTo(userLocation) < range) {
            inRange = true;
        }
        else {
            inRange = false;
        }
        return inRange;
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
        if (tasksToComplete != null) {
            for(int i = 0; i < tasksToComplete.size(); i++) {
                if (!tasksToComplete.get(i).isComplete()) {
                    return false;
                }
            }
        }
        return true;
    }
}
