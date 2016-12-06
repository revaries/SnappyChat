package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;


public class ProfileDataSettingsQuestions extends Fragment {


    private RadioGroup NotificationSettings;
    private RadioGroup VisibilitySettings;
    private Button Next;

    private OnFragmentInteractionListener mListener;

    public ProfileDataSettingsQuestions() {

    }

    public static ProfileDataSettingsQuestions newInstance(String param1, String param2) {
        ProfileDataSettingsQuestions fragment = new ProfileDataSettingsQuestions();
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

        View view   =  inflater.inflate(R.layout.fragment_profile_data_settings_questions, container, false);

        NotificationSettings = (RadioGroup) view.findViewById(R.id.profie_settings_emailnotifications);
        VisibilitySettings = (RadioGroup) view.findViewById(R.id.profile_settings_visibiity_radio);
        Next = (Button) view.findViewById(R.id.profile_settings_next);

        return  view;
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
