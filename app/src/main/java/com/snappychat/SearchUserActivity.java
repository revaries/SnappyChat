package com.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.snappychat.model.User;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class SearchUserActivity extends AppCompatActivity implements SearchUserFragment.OnListFragmentInteractionListener{
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
    public void onFriendAdded(User item) {
        FragmentManager fm = getSupportFragmentManager();
        SearchUserFragment fragment = (SearchUserFragment) fm.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fragment.addFriend(item);
        }
    }
}
