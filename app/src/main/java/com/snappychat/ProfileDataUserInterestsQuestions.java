package com.snappychat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappychat.model.User;

import java.net.Inet4Address;

public class ProfileDataUserInterestsQuestions extends Fragment {

    private EditText AboutMe;
    private EditText Interests;
    private Button Next;
    private Button Back;
    private TextView Warning;
    private OnFragmentInteractionListener mListener;
    private User user;

    public ProfileDataUserInterestsQuestions() {

    }


    public static ProfileDataUserInterestsQuestions newInstance(String param1, String param2) {
        ProfileDataUserInterestsQuestions fragment = new ProfileDataUserInterestsQuestions();
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

        View view =  inflater.inflate(R.layout.fragment_profile_data_user_interests_questions, container, false);

        //Inflating Views
        AboutMe = (EditText) view.findViewById(R.id.about_me);
        Interests = (EditText) view.findViewById(R.id.interests);
        Next = (Button) view.findViewById(R.id.interest_question_next);
        Back = (Button) view.findViewById(R.id.interest_question_back);
        Warning = (TextView) view.findViewById(R.id.interests_warning);

        AboutMe.setOnFocusChangeListener(cellCleaner);
        Interests.setOnFocusChangeListener(cellCleaner);

        user = ((ProfileDataCollector)getActivity()).getUserObject();
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
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

    private View.OnFocusChangeListener cellCleaner = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == AboutMe)
            {
                if (AboutMe.getText().toString()=="About Me") {
                    AboutMe.setText("");
                }
            }
            else if (view == Interests)
            {
                if (Interests.getText().toString()=="Interests"){
                    Interests.setText("");
                }
            }
        }
    };

    private void InitialCellValues()
    {
        if (user.getInterests()==null)
        {
            Interests.setText("");
        }
        else
        {
            Interests.setText(user.getInterests());
        }
        if (user.getAboutMe()==null)
        {
            AboutYou
        }

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
