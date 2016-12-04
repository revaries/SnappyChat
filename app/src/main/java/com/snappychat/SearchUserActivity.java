package com.snappychat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.snappychat.model.User;

public class SearchUserActivity extends AppCompatActivity implements SearchUserFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
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
        return SearchUserFragment.newInstance(1);
    }

    @Override
    public void onListFragmentInteraction(User item) {

    }
}
