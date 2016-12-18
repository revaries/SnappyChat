package com.snappychat;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.snappychat.friends.ChatConverstationsActivity;
import com.snappychat.friends.FriendsActivity;
import com.snappychat.friends.SearchUserActivity;
import com.snappychat.model.Timeline;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;
import com.snappychat.profile.ProfileViewActivity;
import com.snappychat.timeline.AddTimelineActivity;
import com.snappychat.timeline.TimelineFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TimelineFragment.OnListFragmentInteractionListener{

    public static final String TAG = "MainActivity";
    public static final String FROM_LOGIN = "login";
    //This is temporary and it should be set at login
    public static final String CURRENT_USER_LOGGED_IN_ID = "jesantos0527@gmail.com";
    public static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private User userLoggedIn;
    private static User userforShare;

    private FloatingActionButton addTimelineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            userLoggedIn = (User)savedInstanceState.getSerializable(USER_LOGGED_IN);
        if(getIntent().getSerializableExtra(USER_LOGGED_IN) != null)
            userLoggedIn = (User) getIntent().getSerializableExtra(USER_LOGGED_IN);

        userforShare = new LoginActivity().getLoginuser();

        setContentView(R.layout.activity_main);
        /*
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }*/

        addTimelineButton =(FloatingActionButton) findViewById(R.id.addTimeline);
        addTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddTimelineActivity.class);
                i.putExtra(USER_LOGGED_IN,userLoggedIn);
                startActivity(i);
            }
        });
    }

    protected Fragment createFragment() {
        User userLoggedIn = (User) getIntent().getSerializableExtra(MainActivity.USER_LOGGED_IN);
        return TimelineFragment.newInstance(userLoggedIn,1);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(USER_LOGGED_IN, userLoggedIn);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userLoggedIn = (User)savedInstanceState.getSerializable(USER_LOGGED_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        getSupportActionBar().setTitle("SnappyChat");
        getSupportActionBar().setSubtitle("Timeline");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends_bar:
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra(USER_LOGGED_IN,userLoggedIn);
                startActivity(intent);
                return true;
            case R.id.search_bar:
                intent = new Intent(this, SearchUserActivity.class);
                intent.putExtra(USER_LOGGED_IN,userLoggedIn);
                startActivity(intent);
                return true;
            case R.id.chat_messages:
                intent = new Intent(this, ChatConverstationsActivity.class);
                intent.putExtra(USER_LOGGED_IN,userLoggedIn);
                startActivity(intent);
                return true;
            case R.id.sign_out:
                //FirebaseAuth.getInstance().signOut();
                new UpdateUserTask().execute(userLoggedIn.getEmail(),false);
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.STATUS,"signOut");
                startActivity(intent);
                //finish();
                return true;
            case R.id.profile_menu_option:
                intent = new Intent(this,ProfileViewActivity.class);
               // userLoggedIn = ServiceHandler.getUser(userLoggedIn.getEmail());
                intent.putExtra(USER_LOGGED_IN,userLoggedIn);
                //intent.putExtra("from","menuoption");
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public User getUserforShare()
    {
        return userforShare;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new UpdateUserTask().execute(userLoggedIn.getEmail(),true);
        Fragment fragment = createFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed()
    {
        if (getIntent().getExtras().getBoolean(FROM_LOGIN)) {
            new UpdateUserTask().execute(userLoggedIn.getEmail(),false);
            moveTaskToBack(true); // exist app

        }else
            finish();

    }

    @Override
    public void onListFragmentInteraction(Timeline timeline) {

    }

    private class UpdateUserTask extends AsyncTask<Object,Void,Void> {
        @Override
        protected Void doInBackground(Object... params) {
            //Update user status to online
            JSONObject user = new JSONObject();
            try {
                user.put("status",(Boolean)params[1]);
                ServiceHandler.updateUser((String)params[0],user);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException",e);
            }
            return null;
        }


    }
}
