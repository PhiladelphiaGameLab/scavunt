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

    private static final String ARG_PRAM1 = "stringResourceID";
    private static final String ARG_PRAM2 = "layoutResourceID";

    private int stringResourceID;
    private int layoutResourceID;

    private OnFragmentInteractionListener mListener;

    /**
     * Factory method
     *
     * @param stringResourceIDIn
     * @param layoutResourceIDIn
     * @return
     */
    public static TextFragment newInstance (int stringResourceIDIn, int layoutResourceIDIn) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRAM1, stringResourceIDIn);
        args.putInt(ARG_PRAM2, layoutResourceIDIn);
        fragment.setArguments(args);
        return fragment;
    }
    //Required empty public constructor
    public TextFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            stringResourceID = getArguments().getInt(ARG_PRAM1);
            layoutResourceID = getArguments().getInt(ARG_PRAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResourceID, container, false);
        TextView text = (TextView) view.findViewById(R.id.textView);
        text.setText(stringResourceID);

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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }
}
