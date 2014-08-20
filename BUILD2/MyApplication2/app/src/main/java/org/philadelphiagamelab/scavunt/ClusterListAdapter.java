package org.philadelphiagamelab.scavunt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.zip.Inflater;

/**
 * Created by aaronmsegal on 8/19/14.
 */
public class ClusterListAdapter extends BaseAdapter {

    private static LayoutInflater inflater;

    public ClusterListAdapter(LayoutInflater inflaterIn) {
        this.inflater = inflaterIn;
    }

    @Override
    public int getCount() {

        int events = ClusterManager.getNumberOfEventsVisible();
        int count = events;

        for( int e = 0; e < events; e++) {
            count += ClusterManager.getVisibleEvent(e).getNumberOfTasksVisible();
        }

        return count;
    }

    @Override
    public Object getItem(int position) {

        int positionCount = position;
        //code for returning event or task object, may be a bad idea
        for(int e = 0; e < ClusterManager.getNumberOfEventsVisible(); e++) {

            Event currentEvent = ClusterManager.getVisibleEvent(e);

            if(positionCount == 0) {
                return currentEvent;
            }

            positionCount--;

            for(int t =0; t < currentEvent.getNumberOfTasksVisible(); t++) {

                if(positionCount == 0) {
                    return currentEvent.getVisibleTask(t);
                }

                positionCount--;

            }
        }

        //if it is, just do this instead
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int positionCount = position;

        for(int e = 0; e < ClusterManager.getNumberOfEventsVisible(); e++) {

            Event currentEvent = ClusterManager.getVisibleEvent(e);

            if(positionCount == 0) {
                return getEventListView(currentEvent, convertView);
            }

            positionCount--;

            for(int t =0; t < currentEvent.getNumberOfTasksVisible(); t++) {

                if(positionCount == 0) {
                    return getTaskListView(currentEvent.getVisibleTask(t), convertView);
                }

                positionCount--;

            }
        }

        return null;
    }

    private View getEventListView(Event toRepresent, View convertView) {

        //TODO: add code for background color

        //if(convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_item, null);
        //}

        TextView textView = (TextView)convertView.findViewById(R.id.event_list_item_text);
        textView.setText(toRepresent.getTitle());

        return convertView;
    }

    private View getTaskListView(Task toRepresent, View convertView) {

        //TODO: add code for background color

        //if(convertView == null) {
            convertView = inflater.inflate(R.layout.task_list_item, null);
        //}

        TextView textView = (TextView)convertView.findViewById(R.id.task_list_item_text);
        textView.setText(toRepresent.getTitle());

        return convertView;
    }
}
