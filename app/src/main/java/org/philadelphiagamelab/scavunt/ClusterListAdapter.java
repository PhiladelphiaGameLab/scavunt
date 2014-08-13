package org.philadelphiagamelab.scavunt;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

/**
 * Created by aaronmsegal on 7/30/14.
 */
public class ClusterListAdapter extends BaseExpandableListAdapter {

    public LayoutInflater inflater;

    public ClusterListAdapter(LayoutInflater inflaterIn) {
        inflater = inflaterIn;
    }

    @Override
    public int getGroupCount() {
        return ClusterManager.getNumberOfEventsVisible();
    }

    @Override
    public int getChildrenCount(int eventIndex) {
        return ClusterManager.getVisibleEvent(eventIndex).getNumberOfTasksVisible();
    }

    @Override
    public Object getGroup(int eventIndex) {
        return ClusterManager.getVisibleEvent(eventIndex);
    }

    @Override
    public Object getChild(int eventIndex, int taskIndex) {
        return ClusterManager.getVisibleEvent(eventIndex).getVisibleTask(taskIndex);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int eventIndex, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_event, null);
        }

        Event event = (Event) getGroup(eventIndex);
        ((CheckedTextView) convertView).setText(event.getTitle() + "\nIn Range: "
                + Boolean.toString(event.isInRange()));
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public View getChildView(int eventIndex, int taskIndex, boolean isLastTask, View convertView,
                             ViewGroup parent) {

        Event event = (Event) getGroup(eventIndex);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_task, null);
        }

        final Task task = (Task) getChild(eventIndex, taskIndex);

        TextView text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(task.getTitle() + " Complete:" + Boolean.toString(task.isComplete()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListFragment.onTaskClicked(task);
            }
        });

        if(task.getActivityType() == Task.ActivityType.SERVICE_RECEIVE_AUDIO && !task.isComplete()){
            Intent intent = new Intent(convertView.getContext(), AudioService.class);
            convertView.getContext().startService(intent);
            task.setComplete(true);
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
