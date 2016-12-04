package com.snappychat.friends;

/**
 * Created by Jelson on 12/2/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("POSITION", "Position is " + Integer.toString(position));
        switch (position) {
            case 0:
                CurrentFriendsFragment tab1 = new CurrentFriendsFragment();
                return tab1;
            case 1:
                PendingRequestsFragment tab2 = new PendingRequestsFragment();
                return tab2;
            case 2:
                InvitationSentFragment tab3 = new InvitationSentFragment();
                return tab3;
            case 3:
                SearchFriendsFragment tab4 = new SearchFriendsFragment();
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
