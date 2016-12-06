package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ProileDataProfQuestions extends Fragment {

    private EditText Location;
    private EditText Profession;
    private Button Next;

    private OnFragmentInteractionListener mListener;

    public ProileDataProfQuestions() {

    }


    public static ProileDataProfQuestions newInstance(String param1, String param2) {
        ProileDataProfQuestions fragment = new ProileDataProfQuestions();
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
        View view =  inflater.inflate(R.layout.fragment_proile_data_prof_loc, container, false);

        Location = (EditText)view.findViewById(R.id.interests);
        Profession = (EditText)view.findViewById(R.id.profession);
        Next = (Button)view.findViewById(R.id.proffesion_question_next);
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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
