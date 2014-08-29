package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by aaronmsegal on 8/29/14.
 */
public class AudioFragment extends Fragment {

    public static final String AUDIO_FILEPATH_TAG = "audio";
    private static final int STREAM_TYPE_ID = AudioManager.STREAM_MUSIC;

    private Task toRepresent;
    private String title;
    private String audioFilePath;

    //default layout
    private int layoutResourceID = R.layout.receive_audio;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private GestureDetector gestureDetector;

    //Factory Method
    public static AudioFragment newInstance() {
        AudioFragment audioFragment = new AudioFragment();
        return audioFragment;
    }
    //Required public constructor
    public AudioFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(layoutResourceID, container, false);

        //Title
        TextView titleView = (TextView) view.findViewById(R.id.audio_title);
        titleView.setText(title);

        //Audio
        File audioFile = new File(audioFilePath);
        if(audioFile.exists()) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(onCompletionListener);
                mediaPlayer.setAudioStreamType(STREAM_TYPE_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            audioManager = (AudioManager)getActivity()
                                .getSystemService(getActivity().AUDIO_SERVICE);


        }

        //Gesture control
        view.setOnTouchListener(touchListener);
        gestureDetector = new GestureDetector(getActivity(), gestureListener);

        return view;
    }

    private MediaPlayer.OnCompletionListener onCompletionListener =
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
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    return true;
                }
            };

    @Override
    public void onResume () {
        super.onResume();
        audioManager.setStreamSolo(STREAM_TYPE_ID,true);
        mediaPlayer.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        mediaPlayer.pause();
        audioManager.setStreamSolo(STREAM_TYPE_ID,false);
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        title = toRepresent.getTitle();
        audioFilePath = toRepresent.getResource(AUDIO_FILEPATH_TAG);
    }
}
