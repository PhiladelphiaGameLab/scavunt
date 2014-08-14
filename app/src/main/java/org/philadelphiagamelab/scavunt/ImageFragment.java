package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ImageFragment extends Fragment {
    private static final String ARG_PARAM1 = "imageResourceID";
    private static final String ARG_PARAM2 = "layoutResourceID";

    private Task toRepresent;
    private int imageResourceID;
    private int layoutResourceID;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageResourceIDIn id for the image.
     * @param layoutResourceIDIn id for the layout.
     * @return A new instance of fragment ImageFragment.
     */

    public static ImageFragment newInstance(int imageResourceIDIn, int layoutResourceIDIn) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, imageResourceIDIn);
        args.putInt(ARG_PARAM2, layoutResourceIDIn);
        fragment.setArguments(args);
        return fragment;
    }
    public ImageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            imageResourceID = savedInstanceState.getInt(ARG_PARAM1);
            layoutResourceID = savedInstanceState.getInt(ARG_PARAM2);
        }
        else if (getArguments() != null) {
            imageResourceID = getArguments().getInt(ARG_PARAM1);
            layoutResourceID = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layoutResourceID, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_imageFrag);
        imageView.setImageResource(imageResourceID);

        //Sets Task to complete once viewed once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }

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
        outState.putInt(ARG_PARAM1, imageResourceID);
        outState.putInt(ARG_PARAM2, layoutResourceID);

        super.onSaveInstanceState(outState);
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        layoutResourceID = toRepresent.getLayoutID();
        imageResourceID = toRepresent.getResourceID("image");
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
