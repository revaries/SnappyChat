package com.snappychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String FROM_LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed()
    {
      /*  if (getIntent().getExtras().getBoolean(FROM_LOGIN_KEY))
            moveTaskToBack(true); // exist app
        else
            finish();
           */
    }
}
