package org.philadelphiagamelab.scavunt;


import java.util.logging.Handler;

/**
 * Created by aaronmsegal on 7/28/14.
 */
public class Task {

    public static enum Type {
        RECEIVE_TEXT, RECEIVE_AUDIO, RECEIVE_VIDEO, RECEIVE_IMAGE, TAKE_PICTURE, RECORD_VIDEO,
        RESPONSE_TEXT
    }

    private String title;
    private Type type;
    private int layoutID;
    private int resourceID;
    private long delay;
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, Type typeIn, int resourceIDIn, int layoutIDIn, long delayIn, boolean completeIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.layoutID = layoutIDIn;
        this.resourceID = resourceIDIn;
        this.complete = completeIn;
        this.delay = delayIn;
        if( delay <= 0) {
            this.visible = true;
        }
        else {
            delayVisibility();
        }
    }

    public Task (String titleIn, Type typeIn, int resourceIDIn, int layoutIDIn, long delayIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.layoutID = layoutIDIn;
        this.resourceID = resourceIDIn;
        this.complete = true;
        this.delay = delayIn;
        if( delay <= 0) {
            this.visible = true;
        }
        else {
            delayVisibility();
        }
    }

    private void delayVisibility() {
        this.visible = false;

        android.os.Handler handler = new android.os.Handler();

        Runnable makeVisible = new Runnable() {
            @Override
            public void run() {
                visible = true;
            }
        };

        handler.postDelayed(makeVisible, delay);
    }

    public Task (String titleIn, Type typeIn, int resourceIDIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.delay = 0;
        this.visible = true;
        this.complete = true;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
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
