package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by aaronmsegal on 8/28/14.
 */
public class VideoFragment extends Fragment {

    public static final String VIDEO_FILEPATH_TAG = "video";

    private Task toRepresent;
    private String title;
    private String videoFilePath;

    //default layout
    private int layoutResourceID = R.layout.receive_video;

    private VideoView videoView;
    private GestureDetector gestureDetector;

    //Factory Method
    public static VideoFragment newInstance() {
        VideoFragment videoFragment = new VideoFragment();
        return videoFragment;
    }
    //Required public constructor
    public VideoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
    //TODO: Maybe move some stuff out of onCreateView and into onCreate for better performance?
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(layoutResourceID, container, false);

        //Title
        TextView titleView = (TextView) view.findViewById(R.id.video_title);
        titleView.setText(title);


        //Video
        File videoFile = new File(videoFilePath);
        if(videoFile.exists()){
            videoView = (VideoView)view.findViewById(R.id.displayVideo);
            videoView.setVideoPath(videoFile.getAbsolutePath());
            videoView.setMediaController(new MediaController(getActivity()));
            videoView.setOnCompletionListener(completionListener);
            videoView.seekTo(1);
        }
        else {
            Log.e("File not found: ", videoFile.getAbsolutePath());
        }

        //Gesture control
        view.setOnTouchListener(touchListener);
        gestureDetector = new GestureDetector(getActivity(), gestureListener);

        return view;
    }

    private MediaPlayer.OnCompletionListener completionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Sets Task to complete once video viewed to completion
                    if(toRepresent != null && !toRepresent.isComplete()) {
                        toRepresent.setComplete(true);
                    }
                }
            };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    };

    private GestureDetector.SimpleOnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                    return true;
                }
            };

    @Override
    public void onResume () {
        super.onResume();
        videoView.requestFocus();
    }

    @Override
    public void onPause () {
        super.onPause();
        videoView.pause();
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        title = toRepresent.getTitle();
        videoFilePath = toRepresent.getResource(VIDEO_FILEPATH_TAG);
    }

}
