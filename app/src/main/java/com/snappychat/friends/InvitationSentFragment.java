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

import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.networking.FriendsHandler;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class InvitationSentFragment extends Fragment {
    public static final String TAG = "INVITATION_FRIENDS";
    private static AsyncTask<String, Void, String> invitationFriendsTask;
    public static ArrayList<FriendCard> invitationFriendCards = new ArrayList<>();
    public static RecyclerAdapterInvitation adapter;
    private User userLoggedIn;

    public static InvitationSentFragment newInstance(User user) {
        InvitationSentFragment fragment = new InvitationSentFragment();
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userLoggedIn = (User) getArguments().get(MainActivity.USER_LOGGED_IN);
        getInvitationFriendsList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitation_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view_invitation);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapterInvitation(invitationFriendCards);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }

    public void getInvitationFriendsList(){
        invitationFriendsTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String response = FriendsHandler.getFriends(params[0],FriendsHandler.REQUESTED_URL);
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                invitationFriendsTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
                invitationFriendCards = FriendsHandler.processJsonResponse(result, FriendsHandler.REQUESTED_URL);
                adapter.updateData(invitationFriendCards);
            }

        };
        invitationFriendsTask.execute(userLoggedIn.getEmail());
    }
}
