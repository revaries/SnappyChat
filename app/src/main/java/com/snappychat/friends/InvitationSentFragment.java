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

/**
 * Created by Jelson on 12/2/2016.
 */

public class InvitationSentFragment extends Fragment {
    public static final String TAG = "INVITATION_FRIENDS";
    private static AsyncTask<String, Void, String> invitationFriendsTask;
    public static ArrayList<FriendCard> invitationFriendCards = new ArrayList<>();
    public static RecyclerAdapterInvitation adapter;
    private OnListFragmentInteractionListener mListener;
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
        adapter = new RecyclerAdapterInvitation(invitationFriendCards,mListener);
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
        void onCancelRequest(FriendCard item);
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

    public void cancelRequest(FriendCard friendCard){
        AsyncTask<String, Void, ArrayList<FriendCard>> invitationFriendsTask = new AsyncTask<String, Void, ArrayList<FriendCard>>() {
            @Override
            protected ArrayList<FriendCard> doInBackground(String... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",(String)params[0]);
                    ServiceHandler.deleteFriendRequest(params[1], jsonObject);
                    String result = FriendsHandler.getFriends(params[1],FriendsHandler.REQUESTED_URL);
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
                    Toast.makeText(getActivity(), "Friend request canceled!", Toast.LENGTH_SHORT).show();
                }
                adapter.updateData(result);
            }

        };
        invitationFriendsTask.execute(friendCard.getEmail(),userLoggedIn.getEmail());
    }
}
