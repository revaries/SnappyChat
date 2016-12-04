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

public class PendingRequestsFragment extends Fragment {
    public static final String TAG = "PENDING_FRIENDS";
    private static AsyncTask<Void, Void, String> pendingFriendsTask;
    public static ArrayList<FriendCard> pendingFriendCards = new ArrayList<>();
    public static RecyclerAdapterPending adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getPendingFriendsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pending_requests_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view_pending);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapterPending(pendingFriendCards);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }

    public void getPendingFriendsList(){
        pendingFriendsTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String response = FriendsHandler.getFriends(FriendsHandler.PENDING_URL);
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                pendingFriendsTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
                pendingFriendCards = FriendsHandler.processJsonResponse(result, FriendsHandler.PENDING_URL);
                adapter.updateData(pendingFriendCards);
            }

        };
        pendingFriendsTask.execute(null, null, null);
    }
}
