package com.snappychat.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.snappychat.R;
import com.snappychat.model.User;


public class ProfileDataProfilePictureFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    public ImageView profilePicture;
    private Button backProfilePic;
    private Button nextProfilePic;
    private Button saveProfilePic;
    private User picUser;
    private User userLoggedIn;
    private String operationToPerform;

    public ProfileDataProfilePictureFragment() {

    }

    public static ProfileDataProfilePictureFragment newInstance(String param1, String param2) {
        ProfileDataProfilePictureFragment fragment = new ProfileDataProfilePictureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_data_profile_picture, container, false);
        profilePicture = (ImageView) view.findViewById(R.id.profile_data_profile_picture);
        backProfilePic = (Button) view.findViewById(R.id.profile_pic_back);
        nextProfilePic = (Button) view.findViewById(R.id.profile_pic_next);
        saveProfilePic = (Button) view.findViewById(R.id.profile_pic_save);

        picUser = ((ProfileDataCollectorActivity)getActivity()).getUserObject();

        operationToPerform = ((ProfileDataCollectorActivity)getActivity()).getOperation();
        if (operationToPerform.equals(ProfileDataCollectorActivity.EDIT))
        {
            saveProfilePic.setVisibility(View.VISIBLE);
            nextProfilePic.setVisibility(View.INVISIBLE);
            backProfilePic.setVisibility(View.INVISIBLE);
        }
        else if (operationToPerform.equals(ProfileDataCollectorActivity.NEW)) {
            saveProfilePic.setVisibility(View.INVISIBLE);
            nextProfilePic.setVisibility(View.VISIBLE);
            backProfilePic.setVisibility(View.VISIBLE);
        }




        if (picUser.getImage()!=null)
        {

            byte[] imagearray = Base64.decode(picUser.getImage(), Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
            profilePicture.setImageBitmap(image);
        }




        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((ProfileDataCollectorActivity)getActivity()).profilePictureSelector();

            }
        });
        nextProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ((ProfileDataCollectorActivity) getActivity()).nextpagehandler("profilepicture");

            }
        });

        backProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileDataCollectorActivity)getActivity()).prevpagehandler("profilepicture");
            }
        });

        saveProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileDataCollectorActivity)getActivity()).savePageHandeler();
            }
        });
        return view;
    }


    public void setProfilePicture(String imageDetails)
    {
        byte[] imagearray = Base64.decode(imageDetails, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
        profilePicture.setImageBitmap(image);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
