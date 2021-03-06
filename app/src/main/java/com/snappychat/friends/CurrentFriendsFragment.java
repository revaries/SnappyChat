package com.snappychat.friends;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class CurrentFriendsFragment extends Fragment {
    public static final String TAG = "CURRENT_FRIENDS";
    private static AsyncTask<String, Void, ArrayList<User>> friendsTask;
    private SearchUserFragment.OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter adapter;
    private ProgressBar mProgressBar;
    RecyclerView recyclerView;
    private User userLoggedIn;

    public static CurrentFriendsFragment newInstance(User user) {
        CurrentFriendsFragment fragment = new CurrentFriendsFragment();
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
        //View v = inflater.inflate(R.layout.current_friends_fragment, container, false);
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        //RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setHasFixedSize(true);
        //adapter = new RecyclerAdapter(currentFriendCards);
        adapter = new MyItemRecyclerViewAdapter(new ArrayList<User>(),mListener);
        recyclerView.setAdapter( adapter);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        //LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(llm);
        getFriendsList();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchUserFragment.OnListFragmentInteractionListener) {
            mListener = (SearchUserFragment.OnListFragmentInteractionListener) context;
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

    void setupAdapter(ArrayList<User> users) {
        if (getActivity() == null || recyclerView == null) return;
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(users);
    }



    public void getFriendsList(){
        friendsTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                return ServiceHandler.getFriends(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                friendsTask = null;
                //Log.d(TAG, "onPostExecute: result: " + result);
                //currentFriendCards = FriendsHandler.processJsonResponse(result, FriendsHandler.FRIENDS_URL);
                //adapter.updateData(result);
                setupAdapter(result);
            }

        };
        if(userLoggedIn != null)
            friendsTask.execute(userLoggedIn.getEmail());
    }

    public void deleteFriend(User userToDelete){
        AsyncTask<String, Void, ArrayList<User>> friendsTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",params[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String response = ServiceHandler.deleteFriend(params[1], jsonObject);
                if(response != null)
                    return ServiceHandler.getFriends(params[1]);
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<User> users) {
                if(users!=null){
                    Toast.makeText(getActivity(), "Friend deleted!", Toast.LENGTH_SHORT).show();
                }
                setupAdapter(users);
            }

        };
        friendsTask.execute(userToDelete.getEmail(),userLoggedIn.getEmail());
    }
}
