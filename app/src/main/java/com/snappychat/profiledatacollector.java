package com.snappychat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.snappychat.model.User;

public class profiledatacollector extends AppCompatActivity implements ProfileDataNameQuestions.OnFragmentInteractionListener,ProfileDataSettingsQuestions.OnFragmentInteractionListener,ProfileDataUserInterestsQuestions.OnFragmentInteractionListener,ProileDataProfQuestions.OnFragmentInteractionListener{

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
        snappyuser = (User) incoming.getSerializableExtra("user");


        ProfileNameQuestionFragment = new ProfileDataNameQuestions();
        ProfileSettingsQuestionFragment = new ProfileDataSettingsQuestions();
        ProfileInterestQuestionFragment = new ProfileDataUserInterestsQuestions();
        ProfileProfessionQuestionFragment = new ProileDataProfQuestions();


        Bundle bundle = new Bundle();

       bundle.putSerializable("user",snappyuser);

        ProfileNameQuestionFragment.setArguments(bundle);
        ProfileSettingsQuestionFragment.setArguments(bundle);
        ProfileInterestQuestionFragment.setArguments(bundle);
        ProfileProfessionQuestionFragment.setArguments(bundle);

        snappyfragmanager = getSupportFragmentManager();
        snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
