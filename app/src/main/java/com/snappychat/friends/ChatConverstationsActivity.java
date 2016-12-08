package com.snappychat.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.snappychat.R;

/**
 * Created by Jelson on 12/8/16.
 */

public class ChatConverstationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_chat_conversations_container);

        if (fragment == null) {
            fragment = new ChatConverstationsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_chat_conversations_container, fragment)
                    .commit();
        }

    }
}

