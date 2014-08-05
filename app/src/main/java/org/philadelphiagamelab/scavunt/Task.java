package org.philadelphiagamelab.scavunt;


/**
 * Created by aaronmsegal on 7/28/14.
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
    private int layoutID;
    private int resourceID;
    private long delay;
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, int resourceIDIn, int layoutIDIn, long delayIn, boolean completeIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceID = resourceIDIn;
        this.complete = completeIn;
        this.delay = delayIn;
        initializeVisibility();
    }

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, int resourceIDIn, int layoutIDIn, long delayIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceID = resourceIDIn;
        this.complete = true;
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

    public Task (String titleIn, ActivityType activityTypeIn, int resourceIDIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.resourceID = resourceIDIn;
        this.delay = 0;
        this.visible = true;
        this.complete = true;
    }

    public String getTitle() {
        return title;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public int getLayoutID() { return  layoutID; }

    public int getResourceID() {
        return resourceID;
    }

    public void setComplete (Boolean completeStatus) {
        this.complete = completeStatus;
        ClusterManager.checkProgression();
    }

    public boolean isComplete() { return complete; }

    public boolean isVisible() { return visible; }


}
