package com.snappychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.snappychat.model.User;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String FROM_LOGIN = "login";
<<<<<<< HEAD
    //This is temporary and it should be set at login
    private static final String CURRENT_USER_ID = "jesantos0527@gmail.com";
=======
    public static final String USER = "USER";
    private User user;
>>>>>>> 307c320c945495ce40438a72902b1c7f1138c797

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            user = (User)savedInstanceState.getSerializable(USER);
        if(getIntent().getSerializableExtra(USER) != null)
            user = (User) getIntent().getSerializableExtra(USER);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(USER, user);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = (User)savedInstanceState.getSerializable(USER);
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
                intent.putExtra(USER,user);
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
}
