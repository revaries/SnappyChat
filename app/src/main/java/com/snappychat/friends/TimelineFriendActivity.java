package com.snappychat.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.timeline.TimelineFragment;

public class TimelineFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_friend);
        getSupportActionBar().setTitle("SnappyChat");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    protected Fragment createFragment() {
        User userLoggedIn = (User) getIntent().getSerializableExtra(MainActivity.USER_LOGGED_IN);
        getSupportActionBar().setSubtitle("@"+userLoggedIn.getNickName()+" Timeline");
        return TimelineFragment.newInstance(userLoggedIn,1);
    }
}
