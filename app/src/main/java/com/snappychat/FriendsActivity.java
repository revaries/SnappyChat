package com.snappychat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.snappychat.friends.CurrentFriendsFragment;
import com.snappychat.friends.InvitationSentFragment;
import com.snappychat.friends.PagerAdapter;
import com.snappychat.friends.PendingRequestsFragment;
import com.snappychat.friends.SearchUserFragment;
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
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        userLoggedIn = (User) getIntent().getSerializableExtra(USER_LOGGED_IN);
        //String currentUserEmail = getIntent().getStringExtra("CURRENT_USER_ID");
        //Log.d(TAG, "Email: "+currentUserEmail);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //initialise the list of fragments
        final Vector<Fragment> fragments = new Vector<Fragment>();

        //fill up the list with out fragments
        fragments.add(CurrentFriendsFragment.newInstance(userLoggedIn));
        fragments.add(PendingRequestsFragment.newInstance(userLoggedIn));
        fragments.add(InvitationSentFragment.newInstance(userLoggedIn));
        fragments.add(SearchUserFragment.newInstance(userLoggedIn,1));


        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (userLoggedIn,getSupportFragmentManager(),tabLayout.getTabCount(),fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "TAB Selected "+Integer.toString(tab.getPosition()));
                Log.d(TAG, "TAB Text "+tab.getText());
                switch (tab.getPosition()) {
                    case 0:
                        CurrentFriendsFragment tab1 = (CurrentFriendsFragment) fragments.get(0);
                        tab1.getFriendsList();
                        break;
                    case 1:
                        PendingRequestsFragment tab2 = (PendingRequestsFragment) fragments.get(1);
                        tab2.getPendingFriendsList();
                        break;
                    case 2:
                        InvitationSentFragment tab3 = (InvitationSentFragment) fragments.get(2);
                        tab3.getInvitationFriendsList();
                        break;
                    case 3:
                        SearchUserFragment tab4 = (SearchUserFragment) fragments.get(3);
                        tab4.clearFields();
                        break;
                    default:
                        break;
                }
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
                        SearchUserFragment fragment = (SearchUserFragment) adapter.getItem(viewPager.getCurrentItem());
                        if (fragment != null) {
                            fragment.addFriend(item,edittext.getText().toString());
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
                        CurrentFriendsFragment fragment = (CurrentFriendsFragment) adapter.getItem(viewPager.getCurrentItem());
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
    public void onCancelRequest(User item) {
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

}
