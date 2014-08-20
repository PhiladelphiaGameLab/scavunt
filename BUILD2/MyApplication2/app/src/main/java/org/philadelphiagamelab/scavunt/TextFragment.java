package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aaronmsegal on 8/20/14.
 */
public class TextFragment extends Fragment {

    private static final String defaultText = "Place holder text.";

    private String textString;
    private Task toRepresent;

    //default layout, TODO: allow for customized layouts
    private int layoutResourceID = R.layout.receive_text;

    //Factory Method
    public static TextFragment newInstance() {
        TextFragment textFragment = new TextFragment();
        return textFragment;
    }
    //Required public constructor
    public TextFragment() {
        textString = defaultText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(layoutResourceID, container, false);
        TextView text = (TextView) view.findViewById(R.id.text_fragment_text_view);
        text.setMovementMethod(new ScrollingMovementMethod());

        text.setText(textString);

        //Sets Task to complete when viewed at least once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }

        return view;
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        textString = toRepresent.getResourceURLS("text");
    }
}
