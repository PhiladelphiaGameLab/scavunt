package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.security.Key;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TakePictureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TakePictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TakePictureFragment extends Fragment {
    private static final String ARG_PARAM1 = "bitmapKeyID";
    private static final String ARG_PARAM2 = "layoutResourceID";

    private Integer bitmapKeyID;
    private int layoutResourceID;

    private OnFragmentInteractionListener mListener;
    Boolean coloredPicture = true;
    Task toRepresent = null;
    ImageView thisImageView;
    String savedPictures;
    Bitmap userTokenPicture;
    View view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bitmapKeyIDIn Parameter 1.
     * @param layoutResourceIDIn Parameter 2.
     * @return A new instance of fragment TakePhotoFragment.
     */
    public static TakePictureFragment newInstance(Integer bitmapKeyIDIn, int layoutResourceIDIn) {
        TakePictureFragment fragment = new TakePictureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, bitmapKeyIDIn);
        args.putInt(ARG_PARAM2, layoutResourceIDIn);
        fragment.setArguments(args);
        return fragment;
    }
    public TakePictureFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            bitmapKeyID = savedInstanceState.getInt(ARG_PARAM1);
            layoutResourceID = savedInstanceState.getInt(ARG_PARAM2);
        }
        if (getArguments() != null) {
            bitmapKeyID = getArguments().getInt(ARG_PARAM1);
            layoutResourceID = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState !=null) {
            bitmapKeyID = savedInstanceState.getInt(ARG_PARAM1);
            layoutResourceID = savedInstanceState.getInt(ARG_PARAM2);
        }

        view = inflater.inflate(layoutResourceID, container, false);
        thisImageView = (ImageView)view.findViewById(R.id.imageView_takePictureFragment);

        if (getImageBitmap(view.getContext()) !=null) {
            userTokenPicture = getImageBitmap(view.getContext());
            thisImageView.setImageBitmap(userTokenPicture);
        }

/*
        FrameLayout fragmentLayout = (FrameLayout) view;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(68, 68, 68, 68);
        // If a ImageView with ID has been created, just find it and make it visible
        if (view.findViewById(imageViewID) != null) {
            thisImageView = (ImageView) view.findViewById(imageViewID);
            thisImageView.setVisibility(View.VISIBLE);
        }
        else {
        // If the ID hasn't already associate with a ImageView, make a new one
            thisImageView = new ImageView(view.getContext());
            thisImageView.setId(imageViewID);
            fragmentLayout.addView(thisImageView,layoutParams);
            thisImageView.setVisibility(View.VISIBLE);
        }
*/


        Button takeColoredPicture = (Button) view.findViewById(R.id.button_takeColorPicture);
        Button takeGreyPicture = (Button) view.findViewById(R.id.button_takeGreyPicture);

        takeColoredPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                coloredPicture = true;
                startActivityForResult(intent, 0);
            }
        });

        takeGreyPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                coloredPicture = false;
                startActivityForResult(intent, 0);
            }
        });

        //Sets Task to complete once viewed once
        if(toRepresent != null && !toRepresent.isComplete()) {
            toRepresent.setComplete(true);
        }

        return view;
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        userTokenPicture = (Bitmap) data.getExtras().get("data");

        if(!coloredPicture) {
            Bitmap grayOutedPicture = bitmapGrayOut(userTokenPicture);
            thisImageView.setImageBitmap(grayOutedPicture);
            saveImage(view.getContext(),grayOutedPicture);
        }
        else {
            thisImageView.setImageBitmap(userTokenPicture);
            saveImage(view.getContext(),userTokenPicture);
        }
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
        thisImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_PARAM1, bitmapKeyID);
        outState.putInt(ARG_PARAM2, layoutResourceID);

        super.onSaveInstanceState(outState);
    }


    public void updateTask(Task toRepresentIn) {
        toRepresent = toRepresentIn;
        layoutResourceID = toRepresent.getLayoutID();
        bitmapKeyID = toRepresent.getResourceID("imageKey");
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


    // Color filters for bitmap

    public Bitmap bitmapGrayOut(Bitmap bmp){
        Bitmap operation= Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                int highestValue = 0 ;

                if (r >= g){
                    highestValue = r;
                }
                else if (r < g){
                    highestValue = g;
                }

                if (highestValue <= b){
                    highestValue = b;
                }

                r = highestValue;
                g = highestValue;
                b = highestValue;

                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        return operation;
    }




    public Bitmap getImageBitmap(Context context)
    {
        Bitmap bitmap = null;
        SharedPreferences savedSession = context.getSharedPreferences(savedPictures, Context.MODE_PRIVATE);

        String saveimage=savedSession.getString(bitmapKeyID.toString(), "");
        if( !saveimage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(saveimage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

    public boolean saveImage(Context context, Bitmap realImage)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(savedPictures, Context.MODE_PRIVATE).edit();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        editor.putString(bitmapKeyID.toString(), encodedImage);
        return editor.commit();
    }
}

