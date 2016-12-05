package com.snappychat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.snappychat.friends.PagerAdapter;
import com.snappychat.model.User;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

/**
 * Created by Jelson on 12/2/2016.
 */

public class FriendsActivity extends AppCompatActivity implements SearchUserFragment.OnListFragmentInteractionListener{
    private static String TAG = "FRIENDS_ACTIVITY";
    private User userLoggedIn;
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

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (userLoggedIn,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "TAB Selected "+Integer.toString(tab.getPosition()));
//                for(int i=0; i< RecyclerAdapter.mTypeOfLayout.length; i++){
//                            if(i == tab.getPosition()){
//                                RecyclerAdapter.mTypeOfLayout[i] = true;
//                                Log.d(TAG, Integer.toString(i) + " set to TRUE");
//                    }else{
//                        RecyclerAdapter.mTypeOfLayout[i] = false;
//                        Log.d(TAG, Integer.toString(i) + " set to False");
//                    }
//                }
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
    public void onListFragmentInteraction(User item) {

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
