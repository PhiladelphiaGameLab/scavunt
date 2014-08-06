package org.philadelphiagamelab.scavunt;

import android.location.Location;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaronmsegal on 7/30/14.
 */
public class StoryBuilder {

    private static ArrayList<EventCluster> clusters;


    public static ArrayList<EventCluster> getClusters() {

        clusters = new ArrayList<EventCluster>();

        //event 1: video task 1
        Map<String, Integer> event1VideoResources = new HashMap<String, Integer>();
        event1VideoResources.put("video", R.raw.test_video);
        Task event1Video = new Task("video",
                Task.ActivityType.RECEIVE_VIDEO,
                Task.ActivationType.INSTANT,
                event1VideoResources,
                R.layout.video_default_fragment,
                10000);
        //event 1: text task 1
        Map<String, Integer> event1TextResources = new HashMap<String, Integer>();
        event1TextResources.put("text", R.string.testString);
        Task event1Text = new Task("text",
                Task.ActivityType.RECEIVE_TEXT,
                Task.ActivationType.INSTANT,
                event1TextResources,
                R.layout.text_fragment_default,
                0,
                false);
        //event 1: image task 1
        Map<String, Integer> event1ImageResources = new HashMap<String, Integer>();
        event1ImageResources.put("image", R.drawable.test_image);
        Task event1Image = new Task("image",
                Task.ActivityType.RECEIVE_IMAGE,
                Task.ActivationType.IN_RANGE_ONLY,
                event1ImageResources,
                R.layout.image_fragment_default,
                0);
        //event 1: take picture task 1
        Map<String, Integer> event1PictureResources = new HashMap<String, Integer>();
        event1PictureResources.put("imageView", R.id.take_picture_fragment_1);
        Task event1TakePicture = new Task("Take a picture",
                Task.ActivityType.TAKE_PICTURE,
                Task.ActivationType.IN_RANGE_ONLY,
                event1PictureResources,
                R.layout.take_picture_default_fragment,
                0);
        //event 1: take picture task 2
        Map<String, Integer> event1PictureResources2 = new HashMap<String, Integer>();
        event1PictureResources.put("imageView", R.id.take_picture_fragment_2);
        Task event1TakePicture2 = new Task("Take a picture",
                Task.ActivityType.TAKE_PICTURE,
                Task.ActivationType.IN_RANGE_ONLY,
                event1PictureResources,
                R.layout.take_picture_default_fragment,
                0);
        //Build event 1
        ArrayList<Task> event1Tasks = new ArrayList<Task>();
        event1Tasks.add(event1Video);
        event1Tasks.add(event1Text);
        event1Tasks.add(event1Image);
        event1Tasks.add(event1TakePicture);
        event1Tasks.add(event1TakePicture2);
        ArrayList<Task> event1TasksToComplete = event1Tasks;
        Event event1 = new Event("15th st. Station", new Location("Dummy"), event1Tasks, event1TasksToComplete);
        event1.getLocation().setLatitude(39.952472);
        event1.getLocation().setLongitude(-75.165297);


        //event 2: audio task 1
        Map<String, Integer> event2AudioResources = new HashMap<String, Integer>();
        event2AudioResources.put("audio", R.raw.space_cruise);
        Task event2Audio = new Task("audio",
                Task.ActivityType.RECEIVE_AUDIO,
                Task.ActivationType.INSTANT,
                event2AudioResources,
                R.layout.audio_default_fragment,
                0);
        //Build event 2
        ArrayList<Task> event2Tasks = new ArrayList<Task>();
        event2Tasks.add(event2Audio);
        ArrayList<Task> event2TasksToComplete = event2Tasks;
        Event event2 = new Event("16th & Market", new Location("Dummy"), event2Tasks, event1TasksToComplete);
        event2.getLocation().setLatitude(39.952730);
        event2.getLocation().setLongitude(-75.166932);


        //event 3: text task 1
        Map<String, Integer> event3TextResources = new HashMap<String, Integer>();
        event3TextResources.put("text", R.string.testString);
        Task event3Text = new Task("text",
                Task.ActivityType.RECEIVE_TEXT,
                Task.ActivationType.INSTANT,
                event3TextResources,
                R.layout.text_fragment_default,
                0,
                false);
        //Build event3
        ArrayList<Task> event3Tasks = new ArrayList<Task>();
        event3Tasks.add(event3Text);
        ArrayList<Task> event3TasksToComplete = event3Tasks;
        Event event3 = new Event("16th & Chestnut", new Location("Dummy"), event3Tasks, event3TasksToComplete);
        event3.getLocation().setLatitude(39.951329);
        event3.getLocation().setLongitude(-75.167129);

        //event 4: image task 1
        Map<String, Integer> event4ImageResources = new HashMap<String, Integer>();
        event4ImageResources.put("image", R.drawable.test_image);
        Task event4Image1 = new Task("image",
                Task.ActivityType.RECEIVE_IMAGE,
                Task.ActivationType.INSTANT,
                event4ImageResources,
                R.layout.image_fragment_default,
                0);
        Task event4Image2 = new Task("image",
                Task.ActivityType.RECEIVE_IMAGE,
                Task.ActivationType.INSTANT,
                event4ImageResources,
                R.layout.image_fragment_default,
                0);
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
        EventCluster cluster1 = new EventCluster("cluster1", events1, events1, cluster2);

        clusters.add(cluster1);
        clusters.add(cluster2);

        return clusters;
    }
}
