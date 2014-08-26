package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aaronmsegal on 8/18/14.
 *
 * TODO: create logic and functionality to decided between loading game from local memory if entry
 * TODO: exists, or load from server and create local version
 *
 * TODO: Do something with game info?
 */
public class LoadGame extends Activity{

    ServerInterface serverInterface = new ServerInterface();

    //URL
    private static final String URL = "http://wibblywobblytracker.com/scavunt_web/HuntServer.php";
    //What should be queried for test:
    //private static final String URL = "http://wibblywobblytracker.com/scavunt_web/HuntServer.php?gamename=test";

    //php game name tag
    public static final String PHP_TAG_GAME_NAME = "gamename";

    //JSON node names
    //Game
    private static final String TAG_GAME_ARRAY = "game";
    private static final String TAG_GAME_ID = "id";
    private static final String TAG_GAME_NAME = "game_name";
    private static final String TAG_GAME_DESCRIPTION = "game_desc";
    private static final String TAG_GAME_USER_ID = "user_id";
    private static final String TAG_GAME_NUMBER_OF_EVENTS = "num_events";

    //Events
    private static final String TAG_EVENT_ARRAY_BASE = "event";
    private static final String TAG_EVENT_ID = "id";
    private static final String TAG_EVENT_GAME_ID = "game_id";
    private static final String TAG_EVENT_CLUSTER = "cluster";
    private static final String TAG_EVENT_LAT = "lat";
    private static final String TAG_EVENT_LOG = "log";
    private static final String TAG_EVENT_NAME = "event_name";
    private static final String TAG_EVENT_DISTANCE = "distance";
    private static final String TAG_EVENT_NUMBER_OF_TASKS = "num_tasks";

    //Tasks
    private static final String TAG_TASK_ARRAY_BASE = "_task";
    private static final String TAG_TASK_ID = "id";
    private static final String TAG_TASK_EVENT_ID = "event_id";
    private static final String TAG_TASK_TYPE = "task_type";
    private static final String TAG_TASK_NAME = "task_name";
    private static final String TAG_TASK_ACTIVITY_TYPE = "activity_type";
    private static final String TAG_TASK_MUST_COMPLETE = "complete";
    private static final String TAG_TASK_DELAY = "delay";
    private static final String TAG_TASK_NUMBER_OF_MEDIA = "num_media";

    //Media
    private static final String TAG_MEDIA_ARRAY_BASE = "_media";
    private static final String TAG_MEDIA_ID = "id";
    private static final String TAG_MEDIA_URL = "media_url";
    private static final String TAG_MEDIA_TYPE = "media_type";
    private static final String TAG_MEDIA_TASK_ID = "task_id";
    private static final String TAG_MEDIA_USER_ID = "user_id";

    private static String gameName;
    private static ArrayList<EventCluster> clusters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_game);

        //gets game name to load form intent
        gameName = getIntent().getStringExtra(PHP_TAG_GAME_NAME);

        clusters = new ArrayList<EventCluster>();

        new LoadFromServer().execute();
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadFromServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            // Building Parameters
            List<NameValuePair> paramaters = new ArrayList<NameValuePair>();
            paramaters.add(new BasicNameValuePair(PHP_TAG_GAME_NAME, gameName));

            // getting JSON string from URL
            JSONObject jsonObject = serverInterface.makeHttpRequest(URL, ServerInterface.Method.GET, paramaters);

            Log.d("Game JSON String: ", jsonObject.toString());
            try {
                //Game
                JSONArray gameArray = jsonObject.getJSONArray(TAG_GAME_ARRAY);
                JSONObject gameJSON = gameArray.getJSONObject(0);


                //Clusters TODO: first cluster in data base should be 0 not 1, then change below (maybe not?)
                int clusterCount = 1;
                EventCluster currentCluster;

                //Events
                ArrayList<Event> clusterEvents = new ArrayList<Event>();
                for( int e = 0; e < gameJSON.getInt(TAG_GAME_NUMBER_OF_EVENTS); e++) {

                    String eventArrayTag = TAG_EVENT_ARRAY_BASE + Integer.toString(e);
                    JSONArray newEventArray = jsonObject.getJSONArray(eventArrayTag);
                    JSONObject eventJSON = newEventArray.getJSONObject(0);

                    //Tasks
                    ArrayList<Task> eventTasks = new ArrayList<Task>();
                    for( int t = 0; t < eventJSON.getInt(TAG_EVENT_NUMBER_OF_TASKS); t++ ) {

                        String taskArrayTag =
                                eventArrayTag + TAG_TASK_ARRAY_BASE + Integer.toString(t);
                        JSONArray newTaskArray = jsonObject.getJSONArray(taskArrayTag);
                        JSONObject taskJSON = newTaskArray.getJSONObject(0);


                        //Media
                        Map<String,String> taskMedia = new HashMap<String, String>();
                        for( int r = 0; r < taskJSON.getInt(TAG_TASK_NUMBER_OF_MEDIA); r++) {

                            String mediaArrayTag =
                                    taskArrayTag + TAG_MEDIA_ARRAY_BASE + Integer.toString(r);
                            JSONArray newMediaArray = jsonObject.getJSONArray(mediaArrayTag);
                            JSONObject media = newMediaArray.getJSONObject(0);

                            //Add media to task media map
                            taskMedia.put(
                                    media.getString(TAG_MEDIA_TYPE), media.getString(TAG_MEDIA_URL));
                        }

                        //ActivityType
                        Task.ActivityType activityType;
                        String activityTypeIn = taskJSON.getString(TAG_TASK_TYPE);
                        if(activityTypeIn.contains("receive_text")) {
                            activityType = Task.ActivityType.RECEIVE_TEXT;
                        }
                        else if(activityTypeIn.contains("receive_audio")) {
                            activityType = Task.ActivityType.RECEIVE_AUDIO;
                        }
                        else if(activityTypeIn.contains("receive_video")) {
                            activityType = Task.ActivityType.RECEIVE_VIDEO;
                        }
                        else if(activityTypeIn.contains("receive_image")) {
                            activityType = Task.ActivityType.RECEIVE_IMAGE;
                        }
                        else if(activityTypeIn.contains("take_picture")) {
                            activityType = Task.ActivityType.TAKE_PICTURE;
                        }
                        else if(activityTypeIn.contains("record_video")) {
                            activityType = Task.ActivityType.RECORD_VIDEO;
                        }
                        else if(activityTypeIn.contains("response_text")) {
                            activityType = Task.ActivityType.RESPONSE_TEXT;
                        }
                        else {
                            activityType = Task.ActivityType.RECEIVE_TEXT;
                            Log.e("Unrecognized activityType in task_type:", activityTypeIn);
                        }
                        //ActivationType
                        Task.ActivationType activationType;
                        String activationTypeIn = taskJSON.getString(TAG_TASK_ACTIVITY_TYPE);
                        if(activationTypeIn.contains("instant")) {
                            activationType = Task.ActivationType.INSTANT;
                        }
                        else if(activationTypeIn.contains("in_range_once")) {
                            activationType =Task.ActivationType.IN_RANGE_ONCE;
                        }
                        else if(activationTypeIn.contains("in_range_only")) {
                            activationType =Task.ActivationType.IN_RANGE_ONLY;
                        }
                        else {
                            activationType = Task.ActivationType.INSTANT;
                            Log.e("unrecognized activationType in task_activity_type:", activationTypeIn);
                        }

                        //Complete
                        //Task uses opposite for constructor, so non-required tasks start as
                        //complete and required tasks start as not complete
                        Boolean mustComplete = (taskJSON.getInt(TAG_TASK_MUST_COMPLETE) == 1);

                        Task newTask = new Task(taskJSON.getString(TAG_TASK_NAME),
                                activityType,
                                activationType,
                                taskMedia,
                                taskJSON.getInt(TAG_TASK_DELAY),
                                !mustComplete);
                        //Add new task to event's ArrayList
                        eventTasks.add(newTask);

                    }
                    Location newLocation = new Location("Dummy");
                    newLocation.setLatitude(eventJSON.getDouble(TAG_EVENT_LAT));
                    newLocation.setLongitude(eventJSON.getDouble(TAG_EVENT_LOG));

                    //Build Event
                    Event newEvent = new Event( eventJSON.getString(TAG_EVENT_NAME),
                            newLocation,
                            (float) eventJSON.getDouble(TAG_EVENT_DISTANCE),
                            eventTasks);

                    // If current event if from the next cluster, build currentCluster,
                    // add currentCluster to clusters ArrayList and reset ArrayList of events
                    if(eventJSON.getInt(TAG_EVENT_CLUSTER) > clusterCount) {
                        currentCluster = new EventCluster(clusterCount, clusterEvents);
                        clusters.add(currentCluster);
                        clusterEvents = new ArrayList<Event>();
                        clusterCount++;
                    }

                    //add event to clusterEvents
                    clusterEvents.add(newEvent);
                }
                //create last cluster of events and add to clusters
                currentCluster = new EventCluster(clusterCount, clusterEvents);
                clusters.add(currentCluster);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);

            GameHolder.setClusters(clusters);

            LinearLayout container = (LinearLayout) findViewById(R.id.button_holder);

            //set the properties for button
            Button startButton = new Button(getApplicationContext());
            startButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            startButton.setText("Start Game");
            startButton.setTextSize(40.0f);
            //Start Game
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                    startActivity(intent);
                }
            });

            TextView status = (TextView) findViewById(R.id.status);
            status.setText("Done Loading.");

            container.addView(startButton);

        }
    }


}
