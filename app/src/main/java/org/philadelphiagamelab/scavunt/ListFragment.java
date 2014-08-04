package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the Callbacks
 * interface.
 */
public class ListFragment extends Fragment {

    private static OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView.
     */
    private ExpandableListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ClusterListAdapter mAdapter;

    /**
     *
     * @return fragment ListFragment instance
     */
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ClusterListAdapter(getActivity().getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Set the adapter
        mListView = (ExpandableListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks (NOT RIGHT NOW)
        //mListView.setOnItemClickListener(this);

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

    public void updateList() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static void onTaskClicked(Task clicked) {
        mListener.onFragmentInteraction(clicked);
    }

    /**
     * Interface to allow for task fragments to open based on clicks
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Task taskClicked);
    }

}
