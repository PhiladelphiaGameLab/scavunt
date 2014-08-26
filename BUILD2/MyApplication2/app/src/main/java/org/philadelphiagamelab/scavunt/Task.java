package org.philadelphiagamelab.scavunt;

import android.text.Layout;
import android.util.Log;

import java.util.Map;

/**
 * Created by aaronmsegal on 7/28/14.
 *
 * TODO: Look at bellow todos. Make class/classes to parse resources from urls
 *
 */
public class Task {

    //TODO: Implement other types, only text and image currently functioning
    public static enum ActivityType {
        RECEIVE_TEXT,  RECEIVE_IMAGE,
        RESPONSE_TEXT, SERVICE_RECEIVE_AUDIO, RECEIVE_AND_RESPONSE_TEXT,RECEIVE_AUDIO,
        RECEIVE_VIDEO, TAKE_PICTURE, RECORD_VIDEO,
    }


    public static enum ActivationType {
        INSTANT, IN_RANGE_ONCE, IN_RANGE_ONLY
    }

    private String title;
    private ActivityType activityType;
    private ActivationType activationType;
    private Map<String, String> resourceURLs;
    private long delay;
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, Map<String, String> resourceURLsIn, long delayIn, boolean completeIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.resourceURLs = resourceURLsIn;
        this.complete = completeIn;
        this.delay = delayIn;
        this.visible = false;
        if(delay <=0 && activationType == ActivationType.INSTANT) {
            visible = true;
        }
        Log.d("NEW TASK:", title+":"+activationType.toString());
    }

    private void delayVisibility() {

        android.os.Handler handler = new android.os.Handler();

        Runnable makeVisible = new Runnable() {
            @Override
            public void run() {
                visible = true;

            }
        };

        handler.postDelayed(makeVisible, delay);
    }

    public boolean updateVisibility(boolean parentEventInRnage) {
        if(activationType != ActivationType.INSTANT) {
            if(parentEventInRnage) {
                if(delay <= 0) {
                    visible = true;
                }
                else {
                    delayVisibility();
                }
            }
            else if( activationType == ActivationType.IN_RANGE_ONLY) {
                visible = false;
            }
        }
        else {
            if(delay >= 0) {
                delayVisibility();
            }
            else {
                visible = true;
            }
        }

        return visible;
    }

    public String getTitle() {
        return title;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    /*
    //wont be used until custom layouts allowed for
    public String getLayout() {
        // Add code to only send layouts from types which have them, otherwise null/error
        return  resourceURLs.get(0);
    }
    */

    public String getResourceURLS(String key) {
        //TODO: Add more control over which tasks have which resources and how they are got. Maybe more enums?
        return resourceURLs.get(key);
    }

    public void setComplete (Boolean completeStatus) {
        this.complete = completeStatus;

        ClusterManager.checkProgression();
    }

    public boolean isComplete() { return complete; }

    public boolean isVisible() { return visible; }


}