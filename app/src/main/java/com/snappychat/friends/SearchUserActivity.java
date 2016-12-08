package com.snappychat.friends;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.profile.ProfileViewActivity;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class SearchUserActivity extends AppCompatActivity implements SearchUserFragment.OnListFragmentInteractionListener {
    private User userLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        userLoggedIn = (User) getIntent().getSerializableExtra(USER_LOGGED_IN);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    protected int getLayoutResId() {
        return R.layout.activity_search_user;
    }
    protected Fragment createFragment() {
        return SearchUserFragment.newInstance(userLoggedIn,1);
    }

    @Override
    public void onChatRequested(User userReceiver) {

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(USER_LOGGED_IN,userLoggedIn);
        intent.putExtra(ChatActivity.USER_RECEIVER,userReceiver);
        startActivity(intent);

    }

    @Override
    public void onFriendAdded(final User item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        builder1.setMessage("Send a message");
        builder1.setCancelable(true);
        builder1.setView(edittext);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = getSupportFragmentManager();
                        SearchUserFragment fragment = (SearchUserFragment) fm.findFragmentById(R.id.fragmentContainer);
                        if (fragment != null) {
                            fragment.addFriend(item, edittext.getText().toString());
                        }
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

    @Override
    public void onFriendRemoved(final User item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure that you want to remove this friend?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = getSupportFragmentManager();
                        SearchUserFragment fragment = (SearchUserFragment) fm.findFragmentById(R.id.fragmentContainer);
                        if (fragment != null) {
                            fragment.deleteFriend(item);
                        }
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

    @Override
    public void onProfileRequested(User user) {
        Intent intent = new Intent(this,ProfileViewActivity.class);
        intent.putExtra(ProfileViewActivity.USER_TYPE,"friend");
        intent.putExtra(USER_LOGGED_IN,user);
        startActivity(intent);
    }
}
