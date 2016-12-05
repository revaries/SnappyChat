package com.snappychat.friends;

/**
 * Created by Jelson on 12/2/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.snappychat.SearchUserFragment;
import com.snappychat.model.User;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    User userLoggedIn;

    public PagerAdapter(User user, FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.userLoggedIn = user;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentFriendsFragment tab1 = CurrentFriendsFragment.newInstance(userLoggedIn);
                return tab1;
            case 1:
                PendingRequestsFragment tab2 = PendingRequestsFragment.newInstance(userLoggedIn);
                return tab2;
            case 2:
                InvitationSentFragment tab3 = InvitationSentFragment.newInstance(userLoggedIn);
                return tab3;
            case 3:
                //SearchFriendsFragment tab4 = new SearchFriendsFragment();
                SearchUserFragment tab4 = SearchUserFragment.newInstance(userLoggedIn,1);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
