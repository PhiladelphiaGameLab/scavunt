package org.philadelphiagamelab.scavunt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

    private static int notificationCount = 1;
    private boolean notificationSent;
    private static Notification.Builder taskNotificationBuilder;

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, Map<String, Integer> resourceIDsIn, int layoutIDIn, long delayIn, boolean completeIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceIDs = resourceIDsIn;
        this.complete = completeIn;
        this.delay = delayIn;
        this.visible = false;
        this.notificationSent = false;
    }

    public Task (String titleIn, ActivityType activityTypeIn, ActivationType activationTypeIn, Map<String, Integer> resourceIDsIn, int layoutIDIn, long delayIn) {
        this.title = titleIn;
        this.activityType = activityTypeIn;
        this.activationType = activationTypeIn;
        this.layoutID = layoutIDIn;
        this.resourceIDs = resourceIDsIn;
        this.complete = true;
        this.delay = delayIn;
        this.visible = false;
        this.notificationSent = false;
    }

    public void initializeVisibility() {
        if( delay <= 0) {
            if( activationType == ActivationType.INSTANT) {
                makeVisibleAndSendNotification();
            }
            else {
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
                makeVisibleAndSendNotification();
            }
        };

        handler.postDelayed(makeVisible, delay);
    }

    public void updateVisibility( boolean parentEventInRange ) {
        if(activationType != ActivationType.INSTANT) {
            if (parentEventInRange) {
                if( delay <= 0) {
                    makeVisibleAndSendNotification();
                }
                else {
                    delayVisibility();
                }
            } else if( activationType == ActivationType.IN_RANGE_ONLY) {
                visible = false;
            }
        }
    }

    private void makeVisibleAndSendNotification(){
        visible = true;
        String messageNotification;

        //Send notification base on the situation
        if (activityType == ActivityType.SERVICE_RECEIVE_AUDIO){
            messageNotification = "You have a new incoming call";
            sendNotification(messageNotification);
        }
        else if (activityType == ActivityType.RECEIVE_AUDIO){
            messageNotification = "You have a new voice mail";
            sendNotification(messageNotification);
        }
        else if (activityType == ActivityType.RECEIVE_TEXT){
            messageNotification = "You have a new text message";
            sendNotification(messageNotification);
        }
    }

    private void sendNotification(String message) {
        Context context = MyActivity.getMyActivityContext();

        //Intent intent = new Intent(context,MyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

        if(!notificationSent) {
            // Build notification
            // Actions are just fake
            if (taskNotificationBuilder == null) {
                taskNotificationBuilder = new Notification.Builder(context)
                        .setContentTitle("Please check your scavunt")
                        .setContentText(message + ": " + title)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                //.addAction(R.drawable.ic_launcher, "Call", pIntent)
                //.addAction(R.drawable.ic_launcher, "More", pIntent)
                //.addAction(R.drawable.ic_launcher, "And more", pIntent)
                ;
            } else {
                taskNotificationBuilder
                        .setContentText(message + ": " + title)
                        .setNumber(++notificationCount);
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // hide the notification after its selected
            Notification notification = taskNotificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, notification);

            notificationSent = true;
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
