package org.philadelphiagamelab.scavunt;

import java.util.Map;

/**
 * Created by aaronmsegal on 7/28/14.
 *
 * TODO: Look at bellow todos. Make class/classes to parse resources from urls
 *
 */
public class Task {

    public static enum ActivityType {
        RECEIVE_TEXT, RECEIVE_AUDIO, RECEIVE_VIDEO, RECEIVE_IMAGE, TAKE_PICTURE, RECORD_VIDEO,
        RESPONSE_TEXT
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
        initializeVisibility();
    }

    private void initializeVisibility() {
        if( delay <= 0) {
            if( activationType == ActivationType.INSTANT) {
                visible = true;
            }
            else {;
                visible = false;
            }
        }
        else {
            if( activationType == ActivationType.INSTANT) {
                delayVisibility();
            }
            else {
                visible = false;
            }
        }
    }

    private void delayVisibility() {
        visible = false;

        android.os.Handler handler = new android.os.Handler();

        Runnable makeVisible = new Runnable() {
            @Override
            public void run() {
                visible = true;
            }
        };

        handler.postDelayed(makeVisible, delay);
    }

    public void updateVisibility( boolean parentEventInRange ) {
        if(activationType != ActivationType.INSTANT) {
            if (parentEventInRange) {
                if( delay <= 0) {
                    visible = true;
                }
                else {
                    delayVisibility();
                }
            } else if( activationType == ActivationType.IN_RANGE_ONLY) {
                visible = false;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getLayout() {
        //TODO: Add code to only send layouts from types which have them, otherwise null/error
        return  resourceURLs.get(0);
    }

    public String getResourceID(String key) {
        //TODO: Add more control over which tasks have which resources and how they are got. Maybe more enums?
        return resourceURLs.get(key);
    }

    public void setComplete (Boolean completeStatus) {
        this.complete = completeStatus;
        //TODO: ONLY COMMENTED OUT FOR SERVER TESTS!  When added back to the project uncomment line
        //ClusterManager.checkProgression();
    }

    public boolean isComplete() { return complete; }

    public boolean isVisible() { return visible; }


}