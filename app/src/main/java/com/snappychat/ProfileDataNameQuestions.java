package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ProfileDataNameQuestions extends Fragment {



    private OnFragmentInteractionListener mListener;

    private EditText FirstName;
    private EditText LastName;
    private EditText NickName;
    private Button Next;

    public ProfileDataNameQuestions() {

    }


    public static ProfileDataNameQuestions newInstance(String param1, String param2) {
        ProfileDataNameQuestions fragment = new ProfileDataNameQuestions();
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
        Log.v("Inside Fragment one","Inside Fragment one");
        View view =  inflater.inflate(R.layout.fragment_profile_data_name_questions, container, false);

        FirstName = (EditText) view.findViewById(R.id.first_name);
        LastName = (EditText) view.findViewById(R.id.last_name);
        NickName = (EditText) view.findViewById(R.id.nick_name);
        Next = (Button) view.findViewById(R.id.name_question_next);



        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
