package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class VideoFragment extends Fragment implements MediaPlayer.OnCompletionListener {
    private static final String ARG_PARAM1 = "videoResourceID";
    private static final String ARG_PARAM2 = "layoutResourceID";

    private int videoResourceID;
    private int layoutResourceID;

    private VideoView mVideoView;
    private  Task toRepresent;
    private OnFragmentInteractionListener mListener;

    public static VideoFragment newInstance(int videoResourceIDIn,int layoutResourceIDIn) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, videoResourceIDIn);
        args.putInt(ARG_PARAM2, layoutResourceIDIn);
        fragment.setArguments(args);
        return fragment;
    }
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            videoResourceID = savedInstanceState.getInt(ARG_PARAM1);
            layoutResourceID = savedInstanceState.getInt(ARG_PARAM2);
        }
        else if (getArguments() != null) {
            videoResourceID = getArguments().getInt(ARG_PARAM1);
            layoutResourceID = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.video_default_fragment, container, false);
        mVideoView = (VideoView)view.findViewById(R.id.videoView_fragVideoDefault);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoResourceID));
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.requestFocus();
        mVideoView.setOnCompletionListener(this);
        mVideoView.start();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_PARAM1, videoResourceID);
        outState.putInt(ARG_PARAM2, layoutResourceID);

        super.onSaveInstanceState(outState);
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        layoutResourceID = toRepresent.getLayoutID();
        videoResourceID = toRepresent.getResourceID("video");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(getActivity(), "Video finished", Toast.LENGTH_SHORT).show();
        //Sets Task to complete once viewed once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
