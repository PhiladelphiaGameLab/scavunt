package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aaronmsegal on 7/31/14.
 */
public class TextFragment extends Fragment {

    private Task toRepresent;

    private OnFragmentInteractionListener mListener;

    /**
     * Factory method
     */
    public static TextFragment newInstance () {
        TextFragment fragment = new TextFragment();
        return fragment;
    }
    //Required empty public constructor
    public TextFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(toRepresent.getResourceID(), container, false);

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

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
    }

    //currently not used
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }
}
