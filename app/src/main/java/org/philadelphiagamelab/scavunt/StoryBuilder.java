package org.philadelphiagamelab.scavunt;

import android.location.Location;
import java.util.ArrayList;

/**
 * Created by aaronmsegal on 7/30/14.
 */
public class StoryBuilder {

    private static ArrayList<EventCluster> clusters;


    public static ArrayList<EventCluster> getClusters() {

        clusters = new ArrayList<EventCluster>();

        Task event1Video = new Task("video", Task.Type.RECEIVE_VIDEO, R.raw.test_video, 0);
        Task event1Text = new Task("text", Task.Type.RECEIVE_TEXT, R.string.testString, 0);
        ArrayList<Task> event1Tasks = new ArrayList<Task>();
        event1Tasks.add(event1Video);
        event1Tasks.add(event1Text);
        ArrayList<Task> event1TasksToComplete = event1Tasks;
        Event event1 = new Event("15th st. Station", new Location("Dummy"), event1Tasks, event1TasksToComplete);
        event1.getLocation().setLatitude(39.952472);
        event1.getLocation().setLongitude(-75.165297);

        Task event2Audio = new Task("audio", Task.Type.RECEIVE_AUDIO, R.raw.space_cruise, 0);
        ArrayList<Task> event2Tasks = new ArrayList<Task>();
        event2Tasks.add(event2Audio);
        ArrayList<Task> event2TasksToComplete = event2Tasks;
        Event event2 = new Event("16th & Market", new Location("Dummy"), event2Tasks, event1TasksToComplete);
        event2.getLocation().setLatitude(39.952730);
        event2.getLocation().setLongitude(-75.166932);

        Task event3Text = new Task("text", Task.Type.RECEIVE_TEXT, R.string.testString, 0);
        ArrayList<Task> event3Tasks = new ArrayList<Task>();
        event3Tasks.add(event3Text);
        ArrayList<Task> event3TasksToComplete = event3Tasks;
        Event event3 = new Event("16th & Chestnut", new Location("Dummy"), event3Tasks, event3TasksToComplete);
        event3.getLocation().setLatitude(39.951329);
        event3.getLocation().setLongitude(-75.167129);

        Task event4Image1 = new Task("image", Task.Type.RECEIVE_IMAGE, R.drawable.ic_launcher, 0);
        Task event4Image2 = new Task("image", Task.Type.RECEIVE_IMAGE, R.drawable.ic_launcher, 0);
        ArrayList<Task> event4Tasks = new ArrayList<Task>();
        event4Tasks.add(event4Image1);
        event4Tasks.add(event4Image2);
        ArrayList<Task> event4TasksToComplete = event4Tasks;
        Event event4 = new Event("15th & Chestnut", new Location("Dummy"), event4Tasks, event4TasksToComplete);
        event4.getLocation().setLatitude(39.951094);
        event4.getLocation().setLongitude(-75.165556);

        ArrayList<Event> events2 = new ArrayList<Event>();
        events2.add(event3);
        events2.add(event4);
        EventCluster cluster2 = new EventCluster("cluster2", events2);

        ArrayList<Event> events1 = new ArrayList<Event>();
        events1.add(event1);
        events1.add(event2);
        ArrayList<EventCluster> toActivate = new ArrayList<EventCluster>();
        toActivate.add(cluster2);
        EventCluster cluster1 = new EventCluster("cluster1", events1, events1, toActivate);

        clusters.add(cluster1);
        clusters.add(cluster2);

        return clusters;
    }
}
