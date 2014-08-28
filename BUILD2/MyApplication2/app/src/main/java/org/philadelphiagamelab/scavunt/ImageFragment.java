package org.philadelphiagamelab.scavunt;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by aaronmsegal on 8/20/14.
 */
public class ImageFragment extends Fragment {

    private Task toRepresent;
    private String title;
    private String imageFilePath;

    public static final String imageFilePathTag = "image";


    //default layout
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

        View view = inflater.inflate(layoutResourceID, container, false);

        //Title
        TextView titleView = (TextView) view.findViewById(R.id.image_title);
        titleView.setText(title);


        //Image
        File imageFile = new File(imageFilePath);
        if(imageFile.exists()){

            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Log.e("File not found: ", imageFile.getAbsolutePath());
        }

        //Sets Task to complete when viewed at least once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }

        return view;
    }

    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        title = toRepresent.getTitle();
        imageFilePath = toRepresent.getResource(imageFilePathTag);
    }

}
