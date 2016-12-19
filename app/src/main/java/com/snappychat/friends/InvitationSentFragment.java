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
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class InvitationSentFragment extends Fragment {
    public static final String TAG = "INVITATION_FRIENDS";
    private static AsyncTask<String, Void, ArrayList<User>> invitationFriendsTask;
    public static RecyclerAdapterInvitation adapter;
    private OnListFragmentInteractionListener mListener;
    private User userLoggedIn;
    private ProgressBar mProgressBar;
    RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_invitation);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapterInvitation(new ArrayList<User>(),mListener);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        //
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCancelRequest(User item);
    }

    void setupAdapter(ArrayList<User> result) {
        if (getActivity() == null || recyclerView == null) return;
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(result);
    }

    public void getInvitationFriendsList(){
        invitationFriendsTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                return ServiceHandler.getFriendsRequest(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                setupAdapter(result);
            }

        };
        if(userLoggedIn != null)
            invitationFriendsTask.execute(userLoggedIn.getEmail());
    }

    public void cancelRequest(User friendRequestToCancel){
        AsyncTask<String, Void, ArrayList<User>> invitationFriendsTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id",(String)params[0]);
                    ServiceHandler.deleteFriendRequest(params[1], jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return ServiceHandler.getFriendsRequest(params[1]);
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                if(result != null){
                    Toast.makeText(getActivity(), "Friend request canceled!", Toast.LENGTH_SHORT).show();
                }
                setupAdapter(result);
            }

        };
        invitationFriendsTask.execute(friendRequestToCancel.getEmail(),userLoggedIn.getEmail());
    }
}
