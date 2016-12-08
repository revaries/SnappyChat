package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappychat.model.User;

public class ProfileDataUserInterestsQuestions extends Fragment {

    private EditText aboutMe;
    private EditText userInterests;
    private Button next;
    private Button back;
    private Button save;
    private TextView warning;
    private OnFragmentInteractionListener mListener;
    private String operationToPerform;
    private User interestsUser;

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
        aboutMe = (EditText) view.findViewById(R.id.about_me);
        userInterests = (EditText) view.findViewById(R.id.interests);
        next = (Button) view.findViewById(R.id.interest_question_next);
        back = (Button) view.findViewById(R.id.interest_question_back);
        save = (Button) view.findViewById(R.id.interest_question_save);
        warning = (TextView) view.findViewById(R.id.interests_warning);

        interestsUser = ((ProfileDataCollector)getActivity()).getUserObject();
        operationToPerform = ((ProfileDataCollector)getActivity()).getOperation();
        aboutMe.setOnFocusChangeListener(cellCleaner);
        userInterests.setOnFocusChangeListener(cellCleaner);

        InitialCellValues();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateNameFields()) {
                    interestsUser.setAboutMe(aboutMe.getText().toString());
                    interestsUser.setInterests(userInterests.getText().toString());
                    ((ProfileDataCollector) getActivity()).nextpagehandler("interests");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileDataCollector)getActivity()).prevpagehandler("interests");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateNameFields()) {
                    interestsUser.setAboutMe(aboutMe.getText().toString());
                    interestsUser.setInterests(userInterests.getText().toString());
                    ((ProfileDataCollector) getActivity()).savePageHandeler();
                }
            }
        });

        if (operationToPerform.equals(ProfileDataCollector.NEW))
        {
            next.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        }
        else if (operationToPerform.equals(ProfileDataCollector.EDIT))
        {
            next.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }
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
            if (view == aboutMe)
            {
                if ((aboutMe.getText().toString()).equals("About Me")) {
                    aboutMe.setText("");
                }
            }
            else if (view == userInterests)
            {
                if ((userInterests.getText().toString()).equals("Interests")){
                    userInterests.setText("");
                }
            }
        }
    };

    private void InitialCellValues()
    {
        if (interestsUser.getInterests()==null)
        {
            userInterests.setText("Interests");
        }
        else
        {
            userInterests.setText(interestsUser.getInterests());
        }
        if (interestsUser.getAboutMe()==null)
        {
            aboutMe.setText("About Me");
        }
        else
        {
            aboutMe.setText(interestsUser.getAboutMe());
        }

    }


    private boolean ValidateNameFields() {
        boolean flag = true;
        String stringInterests = userInterests.getText().toString();
        String stringAboutMe = aboutMe.getText().toString();
        if (stringInterests.length() == 0 || stringInterests.equals("Interests")) {
            warning.setText("Please Fill All the Fields");
            userInterests.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        } else {
            userInterests.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (stringAboutMe.length() == 0 || stringAboutMe.equals("About Me")) {
            warning.setText("Please Fill All the Fields");
            aboutMe.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        } else {
            aboutMe.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (flag) {
            warning.setText("");
        }
        return flag;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
