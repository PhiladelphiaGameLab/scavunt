package org.philadelphiagamelab.scavunt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

/**
 * Created by aaronmsegal on 7/28/14.
 *
 * TODO: Refactor constructors to single form for creation from database, add complete paramater
 *
 */
public class Task {

    public static enum ActivityType {
        RECEIVE_TEXT, RECEIVE_AUDIO, RECEIVE_VIDEO, RECEIVE_IMAGE, TAKE_PICTURE, RECORD_VIDEO,
        RESPONSE_TEXT, SERVICE_RECEIVE_AUDIO
    }

    public static enum ActivationType {
        INSTANT, IN_RANGE_ONCE, IN_RANGE_ONLY
    }

    private String title;
    private ActivityType activityType;
    private ActivationType activationType;
    private Map<String, Integer> resourceIDs;
    private int layoutID;
    private long delay;
    private boolean visible;
    private boolean complete;

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, Map<String, Integer> resourceIDsIn, int layoutIDIn, long delayIn, boolean completeIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceIDs = resourceIDsIn;
        this.complete = completeIn;
        this.delay = delayIn;
        initializeVisibility();
    }

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, Map<String, Integer> resourceIDsIn, int layoutIDIn, long delayIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceIDs = resourceIDsIn;
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

                    //Test: send a notification when a task is visible
                    Context context = MyActivity.getMyActivityContext();

                    Intent intent = new Intent(context,MyActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                    // Build notification
                    // Actions are just fake
                    Notification noti = new Notification.Builder(context)
                            .setContentTitle("New message")
                            .setContentText("Please check your Stalkerish.").setSmallIcon(R.drawable.ic_launcher)
                            .setContentIntent(pendingIntent)
                                    //.addAction(R.drawable.ic_launcher, "Call", pIntent)
                                    //.addAction(R.drawable.ic_launcher, "More", pIntent)
                                    //.addAction(R.drawable.ic_launcher, "And more", pIntent)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, noti);


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

    public int getLayoutID() { return  layoutID; }

    public int getResourceID(String key) {
        return resourceIDs.get(key);
    }

    public void setComplete (Boolean completeStatus) {
        this.complete = completeStatus;
        ClusterManager.checkProgression();
    }

    public boolean isComplete() { return complete; }

    public boolean isVisible() { return visible; }


}
