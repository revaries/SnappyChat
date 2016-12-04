package com.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.snappychat.model.Timeline;
import com.snappychat.model.User;

public class MainActivity extends AppCompatActivity implements TimelineFragment.OnListFragmentInteractionListener{

    public static final String TAG = "MainActivity";
    public static final String FROM_LOGIN = "login";
    //This is temporary and it should be set at login
    private static final String CURRENT_USER_ID = "jesantos0527@gmail.com";
    public static final String USER = "USER";
    private User userLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            userLoggedIn = (User)savedInstanceState.getSerializable(USER);
        if(getIntent().getSerializableExtra(USER) != null)
            userLoggedIn = (User) getIntent().getSerializableExtra(USER);

        setContentView(R.layout.activity_main);

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
        User userLoggedIn = (User) getIntent().getSerializableExtra(MainActivity.USER);
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
        savedInstanceState.putSerializable(USER, userLoggedIn);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userLoggedIn = (User)savedInstanceState.getSerializable(USER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends_bar:
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("CURRENT_USER_ID", CURRENT_USER_ID);
                startActivity(intent);
                return true;
            case R.id.search_bar:
                intent = new Intent(this, SearchUserActivity.class);
                intent.putExtra(USER,userLoggedIn);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onBackPressed()
    {
        if (getIntent().getExtras().getBoolean(FROM_LOGIN))
            moveTaskToBack(true); // exist app
        else
            finish();

    }

    @Override
    public void onListFragmentInteraction(Timeline timeline) {

    }
}
