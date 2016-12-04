package com.snappychat.friends;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snappychat.R;
import com.snappychat.networking.FriendsHandler;
import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class CurrentFriendsFragment extends Fragment {
    public static final String TAG = "CURRENT_FRIENDS";
    private static AsyncTask<Void, Void, String> friendsTask;
    public static ArrayList<FriendCard> currentFriendCards = new ArrayList<>();
    public static RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFriendsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.current_friends_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapter(currentFriendCards);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }

    public void getFriendsList(){
        friendsTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String response = FriendsHandler.getFriends(FriendsHandler.FRIENDS_URL);
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                friendsTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
                currentFriendCards = FriendsHandler.processJsonResponse(result, FriendsHandler.FRIENDS_URL);
                adapter.updateData(currentFriendCards);
            }

        };
        friendsTask.execute(null, null, null);
    }
}
