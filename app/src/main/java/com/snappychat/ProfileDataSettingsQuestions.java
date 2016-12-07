package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.snappychat.model.User;


public class ProfileDataSettingsQuestions extends Fragment {


    private RadioGroup notificationSettings;
    private RadioButton visibilityPrivate;
    private RadioButton visibilityFriends;
    private RadioButton visibilityPublic;

    private RadioGroup visibilitySettings;
    private RadioButton notificationYes;
    private RadioButton notifcationNo;

    private Button next;
    private Button back;
    private User settingsUser;
    private OnFragmentInteractionListener mListener;
    private String visibility;
    private boolean notifications;


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

        notificationSettings = (RadioGroup) view.findViewById(R.id.profie_settings_emailnotifications);
        notificationYes = (RadioButton) view.findViewById(R.id.email_notification_yes);
        notifcationNo = (RadioButton) view.findViewById(R.id.email_notification_no);

        visibilitySettings = (RadioGroup) view.findViewById(R.id.profile_settings_visibiity_radio);
        visibilityPrivate = (RadioButton) view.findViewById(R.id.profile_settings_private);
        visibilityFriends = (RadioButton) view.findViewById(R.id.profile_settings_friendsonly);
        visibilityPublic = (RadioButton) view.findViewById(R.id.profile_settings_public);


        next = (Button) view.findViewById(R.id.profile_settings_next);
        back = (Button) view.findViewById(R.id.profile_settings_back);


        settingsUser = ((ProfileDataCollector)getActivity()).getUserObject();

        //Setting Keyboard Off for this Fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        SetInitialValues();



        next.setOnClickListener(radioClickListener);
        back.setOnClickListener(radioClickListener);
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

    private void SetInitialValues()
    {
        if (settingsUser.getVisibility()==null)
        {
            visibilitySettings.check(visibilityFriends.getId());
            visibility = "friends";
        }
        else
        {
            visibility = settingsUser.getVisibility();
            if (visibility.equals("private"))
            {
                visibilitySettings.check(visibilityPrivate.getId());
            }
            else if (visibility.equals("friends"))
            {
                visibilitySettings.check(visibilityFriends.getId());
            }
            else {
                visibilitySettings.check(visibilityFriends.getId());
            }

        }

        if (settingsUser.getNotification()==null)
        {
            notificationSettings.check(notificationYes.getId());
            notifications = true;
        }
        else
        {
            notifications = settingsUser.getNotification();
            if (notifications)
            {
                notificationSettings.check(notificationYes.getId());
            }
            else
            {
                notificationSettings.check(notifcationNo.getId());
            }
        }

    }


    private View.OnClickListener radioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == next)
            {
                int visibilityid = visibilitySettings.getCheckedRadioButtonId();
                int notificationid = notificationSettings.getCheckedRadioButtonId();

                switch (visibilityid){
                    case R.id.profile_settings_friendsonly:
                        visibility = "friends";
                        break;
                    case R.id.profile_settings_private:
                        visibility = "private";
                        break;
                    case R.id.profile_settings_public:
                        visibility = "public";
                        break;

                }
                switch (notificationid){
                    case R.id.email_notification_yes:
                        notifications = true;
                        break;
                    case R.id.email_notification_no:
                        notifications = false;
                        break;
                }
                settingsUser.setVisibility(visibility);
                settingsUser.setNotification(notifications);
                ((ProfileDataCollector)getActivity()).nextpagehandler("settings");
            }
            else
            {
                ((ProfileDataCollector)getActivity()).prevpagehandler("settings");
            }
        }
    };

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}