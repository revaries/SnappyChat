package com.snappychat.friends;

/**
 * Created by Jelson on 12/2/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.snappychat.model.User;

import java.util.Vector;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    User userLoggedIn;
    Vector<Fragment> fragments;

    public PagerAdapter(User user, FragmentManager fm, int NumOfTabs, Vector<Fragment> fragments) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.userLoggedIn = user;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //CurrentFriendsFragment tab1 = CurrentFriendsFragment.newInstance(userLoggedIn);
                return fragments.get(0);
            case 1:
                //PendingRequestsFragment tab2 = PendingRequestsFragment.newInstance(userLoggedIn);
                return fragments.get(1);
            case 2:
                //InvitationSentFragment tab3 = InvitationSentFragment.newInstance(userLoggedIn);
                return fragments.get(2);
            case 3:
                //SearchFriendsFragment tab4 = new SearchFriendsFragment();
                //SearchUserFragment tab4 = SearchUserFragment.newInstance(userLoggedIn,1);
                return fragments.get(3);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
