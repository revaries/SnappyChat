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
import com.snappychat.friends.MyItemRecyclerViewAdapter;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/7/16.
 */

public class ChatConverstationsFragment extends Fragment{
    public static final String TAG = "PENDING_FRIENDS";
    private static AsyncTask<String, Void, ArrayList<User>> pendingFriendsTask;
    public static RecyclerConverstationsAdapter adapter;
    private User userLoggedIn;
    //PendingRequestsFragment.OnListFragmentInteractionListener mListener;
    private ProgressBar mProgressBar;
    RecyclerView recyclerView;

    public static PendingRequestsFragment newInstance(User user) {
        PendingRequestsFragment fragment = new PendingRequestsFragment();
        Bundle args = new Bundle();
        //args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        args.putSerializable(MainActivity.CURRENT_USER_LOGGED_IN_ID,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //userLoggedIn = (User) getArguments().get(MainActivity.USER_LOGGED_IN);
        userLoggedIn = (User) getArguments().get(MainActivity.CURRENT_USER_LOGGED_IN_ID);
        getPendingFriendsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_conversations, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.chat_conversations_recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerConverstationsAdapter(new ArrayList<User>());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        return v;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof PendingRequestsFragment.OnListFragmentInteractionListener) {
//            mListener = (PendingRequestsFragment.OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onPendingChanged(User item, boolean answer);
//    }

    void setupAdapter(ArrayList<User> result) {
        if (getActivity() == null || recyclerView == null) return;
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(result);
    }

    public void getPendingFriendsList(){
        pendingFriendsTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                return ServiceHandler.getFriendsPending(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                setupAdapter(result);
            }

        };
        if(userLoggedIn != null)
            pendingFriendsTask.execute(userLoggedIn.getEmail());
    }


    public void modifyPendingRequest(User friendCard, boolean answer){
        AsyncTask<Object, Void, ArrayList<User>> invitationFriendsTask = new AsyncTask<Object, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(Object... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",(String)params[0]);
                    jsonObject.put("accept",(Boolean) params[2]);
                    String result = ServiceHandler.updateFriendPending((String)params[1], jsonObject);
                    if(result != null)
                        return ServiceHandler.getFriendsPending((String)params[1]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                if(result != null){
                    Toast.makeText(getActivity(), "Friend pending modified!", Toast.LENGTH_SHORT).show();
                    setupAdapter(result);
                }
            }

        };
        invitationFriendsTask.execute(friendCard.getEmail(),userLoggedIn.getEmail(),answer);
    }
}

