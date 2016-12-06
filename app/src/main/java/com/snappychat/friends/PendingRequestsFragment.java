package com.snappychat.friends;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

/**
 * Created by Jelson on 12/2/2016.
 */

public class PendingRequestsFragment extends Fragment {
    public static final String TAG = "PENDING_FRIENDS";
    private static AsyncTask<String, Void, ArrayList<FriendCard>> pendingFriendsTask;
    public static ArrayList<FriendCard> pendingFriendCards = new ArrayList<>();
    public static RecyclerAdapterPending adapter;
    private User userLoggedIn;
    OnListFragmentInteractionListener mListener;
    private ProgressBar mProgressBar;
    RecyclerView recyclerView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pending_requests_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_pending);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapterPending(pendingFriendCards,mListener);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        getPendingFriendsList();
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

    void setupAdapter(ArrayList<FriendCard> result) {
        if (getActivity() == null || recyclerView == null) return;
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(result);
    }

    public void getPendingFriendsList(){
        pendingFriendsTask = new AsyncTask<String, Void, ArrayList<FriendCard>>() {
            @Override
            protected ArrayList<FriendCard> doInBackground(String... params) {
                String response = FriendsHandler.getFriends(params[0],FriendsHandler.PENDING_URL);
                if(response != null)
                    return pendingFriendCards = FriendsHandler.processJsonResponse(response, FriendsHandler.PENDING_URL);
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<FriendCard> friends_pending) {
                setupAdapter(friends_pending);
            }

        };
        if(userLoggedIn != null)
            pendingFriendsTask.execute(userLoggedIn.getEmail());
    }

    public void modifyPendingRequest(FriendCard friendCard, boolean answer){
        AsyncTask<Object, Void, ArrayList<FriendCard>> invitationFriendsTask = new AsyncTask<Object, Void, ArrayList<FriendCard>>() {
            @Override
            protected ArrayList<FriendCard> doInBackground(Object... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",(String)params[0]);
                    jsonObject.put("accept",(Boolean) params[2]);
                    String result = ServiceHandler.updateFriendPending((String)params[1], jsonObject);
                    if(result != null)
                        return FriendsHandler.processJsonResponse(result, FriendsHandler.REQUESTED_URL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<FriendCard> result) {
                if(result != null){
                    Toast.makeText(getActivity(), "Friend pending modified!", Toast.LENGTH_SHORT).show();
                }
                adapter.updateData(result);
            }

        };
        invitationFriendsTask.execute(friendCard.getEmail(),userLoggedIn.getEmail(),answer);
    }
}
