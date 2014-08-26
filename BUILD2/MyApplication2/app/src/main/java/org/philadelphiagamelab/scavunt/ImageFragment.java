package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aaronmsegal on 8/20/14.
 */
public class ImageFragment extends Fragment {

    private Task toRepresent;
    private String title;
    private String imageURL;

    public static final String imageURLTag = "image";


    //default layout, TODO: allow for customized layouts
    private int layoutResourceID = R.layout.receive_image;

    //Factory Method
    public static ImageFragment newInstance() {
        ImageFragment imageFragment = new ImageFragment();
        return imageFragment;
    }
    //Required public constructor
    public ImageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.receive_image, container, false);

        TextView titleView = (TextView) view.findViewById(R.id.image_title);
        titleView.setText(title);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(view.getContext());
        imgLoader.DisplayImage(imageURL, imageView);

        //For testing only
        //TextView textView = (TextView) view.findViewById(R.id.image_resource_url);
        //textView.setText(imageURL);

        Log.d("BUILDING IMAGE FRAG:", title + ":" + imageURL);

        //Sets Task to complete when viewed at least once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }

        return view;
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        title = toRepresent.getTitle();
        imageURL = toRepresent.getResourceURLS(imageURLTag);
    }

}
