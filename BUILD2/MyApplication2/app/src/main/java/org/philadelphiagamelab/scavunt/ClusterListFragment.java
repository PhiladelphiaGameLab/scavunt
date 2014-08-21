package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by aaronmsegal on 8/19/14.
 *
 * TODO:Decide about interface need
 */
public class ClusterListFragment extends Fragment {

    //Activity Interface for opening task fragments
    private static OnTaskListItemInteractionListener listFragmentInteractionListener;

    //List view created in fragment
    private ListView listView;

    //Adapter
    private ClusterListAdapter clusterListAdapter;

    //Factory method
    public static ClusterListFragment newInstance() {
        ClusterListFragment fragment = new ClusterListFragment();
        return fragment;
    }
    //Mandatory empty constructor
    public ClusterListFragment() {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listFragmentInteractionListener = (OnTaskListItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + ":This class must implement OnEventListFragmentInteractionListener");
        }
    }
    /**
     * Interface to allow for task fragments to open based on clicks
     */
    public interface OnTaskListItemInteractionListener {
        public void onTaskListItemInteraction(Task clicked);
    }
    public static void onTaskClicked(Task toActivate) {
        listFragmentInteractionListener.onTaskListItemInteraction(toActivate);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listFragmentInteractionListener = null;
    }

    public void updateList() {
        if(clusterListAdapter != null) {
            //Toast.makeText(getActivity(), "data set changed", Toast.LENGTH_SHORT).show();
            clusterListAdapter.notifyDataSetChanged();
        }
    }
}
