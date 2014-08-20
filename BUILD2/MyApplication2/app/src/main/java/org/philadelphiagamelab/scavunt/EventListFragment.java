package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by aaronmsegal on 8/19/14.
 */
public class EventListFragment extends Fragment {

    //Activity Interface
    OnEventListFragmentInteractionListener listFragmentInteractionListener;

    //List view created in fragment
    private ListView listView;

    //Adapter
    private ClusterListAdapter clusterListAdapter;

    //Factory method
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }
    //Mandatory empty constructor
    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clusterListAdapter = new ClusterListAdapter(getActivity().getLayoutInflater());

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cluster_list, container, false);


        listView = (ListView)view.findViewById(R.id.list);
        listView.setAdapter(clusterListAdapter);

        return view;
    }


        /**
         * Interface to allow for task fragments to open based on clicks
         */
    public interface OnEventListFragmentInteractionListener {
        public void onEventListFragmentInteraction();
    }
}
