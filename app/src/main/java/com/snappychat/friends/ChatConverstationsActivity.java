package com.snappychat.friends;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.snappychat.R;
import com.snappychat.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jelson on 12/8/16.
 */

public class ChatConverstationsActivity extends AppCompatActivity implements ChatConverstationsFragment.OnListFragmentInteractionListener{
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_chat_conversations_container);

        if (fragment == null) {
            fragment = new ChatConverstationsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_chat_conversations_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CHAT", "State was called OnStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("CHAT", "State was called OnStop()");
    }

    @Override
    public void onDeleteChatConversation(final String creator, final String receiver) {
        final ChatConverstationsFragment f = (ChatConverstationsFragment) fragment;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure that you want to remove this chat conversation?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        f.deleteChatConversation(creator, receiver);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        ChatConverstationsFragment fragment = (ChatConverstationsFragment) fm.findFragmentById(R.id.fragment_chat_conversations_container);
        if(fragment != null){
            fragment.getChatsList();
        }

    }
}

