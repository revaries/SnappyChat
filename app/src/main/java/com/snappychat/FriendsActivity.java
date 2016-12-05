package com.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.snappychat.friends.CurrentFriendsFragment;
import com.snappychat.friends.InvitationSentFragment;
import com.snappychat.friends.PagerAdapter;
import com.snappychat.friends.PendingRequestsFragment;
import com.snappychat.model.FriendCard;
import com.snappychat.model.User;

import java.util.Vector;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

/**
 * Created by Jelson on 12/2/2016.
 */

public class FriendsActivity extends AppCompatActivity implements SearchUserFragment.OnListFragmentInteractionListener, InvitationSentFragment.OnListFragmentInteractionListener, PendingRequestsFragment.OnListFragmentInteractionListener{
    private static String TAG = "FRIENDS_ACTIVITY";
    private User userLoggedIn;
    ViewPager viewPager;
    PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userLoggedIn = (User) getIntent().getSerializableExtra(USER_LOGGED_IN);
        //String currentUserEmail = getIntent().getStringExtra("CURRENT_USER_ID");
        //Log.d(TAG, "Email: "+currentUserEmail);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Invitations"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //initialise the list of fragments
        Vector<Fragment> fragments = new Vector<Fragment>();

        //fill up the list with out fragments
        fragments.add(CurrentFriendsFragment.newInstance(userLoggedIn));
        fragments.add(PendingRequestsFragment.newInstance(userLoggedIn));
        fragments.add(InvitationSentFragment.newInstance(userLoggedIn));
        fragments.add(SearchUserFragment.newInstance(userLoggedIn,1));


        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (userLoggedIn,getSupportFragmentManager(), tabLayout.getTabCount(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "TAB Selected "+Integer.toString(tab.getPosition()));
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        SearchUserFragment fragment = (SearchUserFragment) adapter.getItem(viewPager.getCurrentItem());
        if (fragment != null) {
            fragment.addFriend(item);
        }
    }

    @Override
    public void onCancelRequest(FriendCard item) {
        InvitationSentFragment fragment = (InvitationSentFragment) adapter.getItem(viewPager.getCurrentItem());
        if (fragment != null) {
            fragment.cancelRequest(item);
        }
}

    @Override
    public void onPendingChanged(FriendCard item, boolean answer) {
        PendingRequestsFragment fragment = (PendingRequestsFragment) adapter.getItem(viewPager.getCurrentItem());
        if (fragment != null) {
            fragment.modifyPendingRequest(item, answer);
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
