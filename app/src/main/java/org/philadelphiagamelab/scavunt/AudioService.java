package org.philadelphiagamelab.scavunt;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    public AudioService() {
    }

    private MediaPlayer mPlayer;
    private AudioManager audioManager;
    private int currentPos;
    private int audioID;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_SHORT).show();
        //audioID = intent.getStringExtra(Task.SOUND_RESOURCE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //audioID = intent.getStringExtra(Task.SOUND_RESOURCE);
        //Uri mySong = Uri.parse("android.resource://org.philadelphiagamelab.scavunt/"+R.raw.test_song);

        audioID = R.raw.song1_short;

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(audioID);

        mPlayer =  new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        try {
            mPlayer.setDataSource(  assetFileDescriptor.getFileDescriptor(),
                                    assetFileDescriptor.getStartOffset(),
                                    assetFileDescriptor.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnPreparedListener(this);

        audioManager = (AudioManager) getApplication().getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,2,flags);

        // mPlayer.seekTo(currentPos);
        mPlayer.prepareAsync();
        //mPlayer.start();
        mPlayer.setOnCompletionListener(this);


        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        if(mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                currentPos = mPlayer.getCurrentPosition();
            }
            mPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "In onPrepared", Toast.LENGTH_LONG).show();
        mPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this, "Song finished",Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}