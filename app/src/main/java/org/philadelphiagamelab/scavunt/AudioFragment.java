package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AudioFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "audioResourceID";
    private static final String ARG_PARAM2 = "layoutResourceID";

    private Task toRepresent;
    private int audioResourceID;
    private int layoutResourceID;

    private MediaPlayer mPlayer;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AudioFragment.
     */

    public static AudioFragment newInstance(int audioResourceIDIn, int layoutResourceIDIn) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, audioResourceIDIn);
        args.putInt(ARG_PARAM2, layoutResourceIDIn);
        fragment.setArguments(args);
        return fragment;
    }
    public AudioFragment() {} // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            audioResourceID = savedInstanceState.getInt(ARG_PARAM1);
            layoutResourceID = savedInstanceState.getInt(ARG_PARAM2);
        }
        if (getArguments() != null) {
            audioResourceID = getArguments().getInt(ARG_PARAM1);
            layoutResourceID = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.audio_default_fragment, container, false);

        mPlayer = MediaPlayer.create(getActivity(), audioResourceID);
        mPlayer.start();

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
    public void onPause(){
        super.onPause();
        mPlayer.pause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_PARAM1, audioResourceID);
        outState.putInt(ARG_PARAM2, layoutResourceID);

        super.onSaveInstanceState(outState);
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        layoutResourceID = toRepresent.getLayoutID();
        audioResourceID = toRepresent.getResourceID("audio");
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
