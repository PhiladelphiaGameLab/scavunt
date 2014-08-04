package org.philadelphiagamelab.scavunt;


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
    private int resourceID;
    private long delay;
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, Type typeIn, int resourceIDIn, long delayIn,
                 boolean completeIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.complete = completeIn;
        this.delay = delayIn;
        if( delay <= 0) {
            this.visible = true;
        }
        else {
            this.visible = false;
            // TODO: implement visibility delay
        }
    }

    public Task (String titleIn, Type typeIn, int resourceIDIn, long delayIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.complete = true;
        this.delay = delayIn;
        if( delay <= 0) {
            this.visible = true;
        }
        else {
            this.visible = false;
            // TODO: implement visibility delay
        }
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

    public int getResourceID() {
        return resourceID;
    }

    public void setComplete (Boolean completeStatus) {
        this.complete = completeStatus;
    }

    public boolean getComplete() { return complete; }


}
