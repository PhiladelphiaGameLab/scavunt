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
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, Type typeIn, int resourceIDIn, boolean visibleIn,
                 boolean completeIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.visible = visibleIn;
        this.complete = completeIn;
    }

    public Task (String titleIn, Type typeIn, int resourceIDIn, boolean visibleIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.visible = visibleIn;
        this.complete = false;
    }

    public Task (String titleIn, Type typeIn, int resourceIDIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.resourceID = resourceIDIn;
        this.visible = false;
        this.complete = false;
    }

    public Task (String titleIn, Type typeIn, boolean visibleIn, boolean completeIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.visible = visibleIn;
        this.complete = completeIn;
    }


    public Task (String titleIn, Type typeIn, boolean visibleIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.visible = visibleIn;
        this.complete = false;
    }

    public Task (String titleIn, Type typeIn) {
        this.title = titleIn;
        this.type = typeIn;
        this.visible = false;
        this.complete = false;
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
