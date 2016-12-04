package com.snappychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String FROM_LOGIN = "login";
    //This is temporary and it should be set at login
    private static final String CURRENT_USER_ID = "jesantos0527@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /*
    @Override
    public void onBackPressed()
    {
        if (getIntent().getExtras().getBoolean(FROM_LOGIN_KEY))
            moveTaskToBack(true); // exist app
        else
            finish();

    }*/
}
