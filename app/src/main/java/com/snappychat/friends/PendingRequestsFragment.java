package com.snappychat.friends;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.FriendCard;
import com.snappychat.model.User;
import com.snappychat.networking.FriendsHandler;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.snappychat.friends.InvitationSentFragment.invitationFriendCards;

/**
 * Created by Jelson on 12/2/2016.
 */

public class PendingRequestsFragment extends Fragment {
    public static final String TAG = "PENDING_FRIENDS";
    private static AsyncTask<String, Void, String> pendingFriendsTask;
    public static ArrayList<FriendCard> pendingFriendCards = new ArrayList<>();
    public static RecyclerAdapterPending adapter;
    private User userLoggedIn;
    OnListFragmentInteractionListener mListener;

    public static PendingRequestsFragment newInstance(User user) {
        PendingRequestsFragment fragment = new PendingRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userLoggedIn = (User) getArguments().get(MainActivity.USER_LOGGED_IN);
        getPendingFriendsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pending_requests_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view_pending);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapterPending(pendingFriendCards,mListener);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPendingChanged(FriendCard item, boolean answer);
    }

    public void getPendingFriendsList(){
        pendingFriendsTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String response = FriendsHandler.getFriends(params[0],FriendsHandler.PENDING_URL);
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
        pendingFriendsTask.execute(userLoggedIn.getEmail());
    }

    public void modifyPendingRequest(FriendCard friendCard, boolean answer){
        AsyncTask<Object, Void, String> invitationFriendsTask = new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",(String)params[0]);
                    jsonObject.put("accept",(Boolean) params[2]);
                    String result = ServiceHandler.updateFriendPending((String)params[1], jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return FriendsHandler.getFriends((String)params[1],FriendsHandler.PENDING_URL);
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(TAG, "onPostExecute: result: " + result);
                invitationFriendCards = FriendsHandler.processJsonResponse(result, FriendsHandler.REQUESTED_URL);
                if(result != null){
                    Toast.makeText(getActivity(), "Friend request canceled!", Toast.LENGTH_SHORT).show();
                }
                adapter.updateData(invitationFriendCards);
            }

        };
        invitationFriendsTask.execute(friendCard.getEmail(),userLoggedIn.getEmail(),answer);
    }
}
