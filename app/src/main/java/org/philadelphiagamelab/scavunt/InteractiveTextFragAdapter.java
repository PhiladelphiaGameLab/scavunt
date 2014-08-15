package org.philadelphiagamelab.scavunt;

/**
 * Created by tianton1 on 8/15/2014.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InteractiveTextFragAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;


    public InteractiveTextFragAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.interactive_text_box_left, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View textBoxLeft = inflater.inflate(R.layout.interactive_text_box_left, parent, false);
        View textBoxRight = inflater.inflate(R.layout.interactive_text_box_right,parent,false);

        TextView textView = (TextView) textBoxLeft.findViewById(R.id.label);
        TextView textView_reversed = (TextView) textBoxRight.findViewById(R.id.label_reversed);

        ImageView imageView = (ImageView) textBoxLeft.findViewById(R.id.icon_interaction_textbox_left);
        //ImageView imageView_reversed = (ImageView) rowView_reversed.findViewById(R.id.icon_rowReverse);

        String s = values.get(position);
        if ( s.startsWith("Me"))
        // if the string starts with Me, it shows the text on the right
        {
            textView_reversed.setText(values.get(position));
            //imageView_reversed.setImageResource(R.drawable.no);
            return textBoxRight;

        } else {
            textView.setText(values.get(position));
            imageView.setImageResource(R.drawable.test_hacker);
            return textBoxLeft;
        }
    }
}
