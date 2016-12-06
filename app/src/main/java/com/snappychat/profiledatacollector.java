package com.snappychat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import com.snappychat.model.User;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class ProfileDataCollector extends AppCompatActivity implements ProfileDataNameQuestions.OnFragmentInteractionListener,ProfileDataSettingsQuestions.OnFragmentInteractionListener,ProfileDataUserInterestsQuestions.OnFragmentInteractionListener,ProileDataProfQuestions.OnFragmentInteractionListener{

    private User snappyuser;
    private Fragment ProfileNameQuestionFragment;
    private Fragment ProfileSettingsQuestionFragment;
    private Fragment ProfileInterestQuestionFragment;
    private Fragment ProfileProfessionQuestionFragment;
    private Fragment ProfilePictureFragment;
    private FragmentManager snappyfragmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledatacollector);

        Intent incoming = getIntent();
        snappyuser = (User) incoming.getSerializableExtra(USER_LOGGED_IN);


        ProfileNameQuestionFragment = new ProfileDataNameQuestions();
        ProfileSettingsQuestionFragment = new ProfileDataSettingsQuestions();
        ProfileInterestQuestionFragment = new ProfileDataUserInterestsQuestions();
        ProfileProfessionQuestionFragment = new ProileDataProfQuestions();



        snappyfragmanager = getSupportFragmentManager();
        snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();

    }


    public User getUserObject()
    {
        return snappyuser;
    }


    public void nextpagehandler(String pageid)
    {
        switch (pageid){
            case "name":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileInterestQuestionFragment).commit();
                break;
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileProfessionQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileSettingsQuestionFragment).commit();
                break;
            case "settings":
                callTimeLine();
                break;
            case "profilepicture":
                break;
        }

    }

    public void  prevpagehandler(String pageid)
    {
        switch (pageid){
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileInterestQuestionFragment).commit();
                break;
            case "settings":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileProfessionQuestionFragment).commit();
                break;
            case "profilepicture":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();
                break;
        }
    }

    private void callTimeLine()
    {
        Intent timelineIntent = new Intent(getBaseContext(),MainActivity.class);
        timelineIntent.putExtra(USER_LOGGED_IN,snappyuser);
        startActivity(timelineIntent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
