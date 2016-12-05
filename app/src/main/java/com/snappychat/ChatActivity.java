package com.snappychat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.snappychat.model.User;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

/**
 * Created by Jelson on 11/27/16.
 */


public class ChatActivity extends AppCompatActivity {
    public static final String USER_RECEIVER = "User receiver";
    public static Boolean isActive = false;
    public User userSender;
    public User userReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userSender = (User) getIntent().getSerializableExtra(USER_LOGGED_IN);
        userReceiver = (User) getIntent().getSerializableExtra(USER_RECEIVER);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new ChatFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CHAT", "State was called OnStart()");
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = true;
        Log.d("CHAT", "State was called OnStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        Log.d("CHAT", "State was called OnResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = true;
        Log.d("CHAT", "State was called OnPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActive = false;
        Log.d("CHAT", "State was called OnDestroy()");
    }

}

