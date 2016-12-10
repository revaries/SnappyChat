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

import static com.snappychat.networking.ServiceHandler.getChatConversations;

/**
 * Created by Jelson on 12/7/16.
 */

public class ChatConverstationsFragment extends Fragment{
    public static final String TAG = "CHAT_CONVERSATION";
    private static AsyncTask<String, Void, ArrayList<User>> chatConversationTask;
    public static RecyclerConverstationsAdapter adapter;
    public static User userLoggedIn;
    private ProgressBar mProgressBar;
    RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;

    public static ChatConverstationsFragment newInstance(User user) {
        ChatConverstationsFragment fragment = new ChatConverstationsFragment();
        Bundle args = new Bundle();
        //args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userLoggedIn = (User) getActivity().getIntent().getSerializableExtra(MainActivity.USER_LOGGED_IN);
        getChatsList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_conversations, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.chat_conversations_recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerConverstationsAdapter(new ArrayList<User>(), mListener);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
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


    public interface OnListFragmentInteractionListener {
        void onDeleteChatConversation(String creator, String receiver);
    }

    public void setupAdapter(ArrayList<User> result) {
        if (getActivity() == null || recyclerView == null) return;
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(result);
    }

    public void getChatsList(){
        chatConversationTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                return ServiceHandler.getChatConversations(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                setupAdapter(result);
            }
        };
        if(userLoggedIn != null)
            chatConversationTask.execute(userLoggedIn.getEmail());

    }

    public void deleteChatConversation(String user_creator, String user_receiver){
        AsyncTask<String, Void, ArrayList<User>> deleteConversationTask = new AsyncTask<String, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(String... params) {
                JSONObject put_request = new JSONObject();
                try {
                    put_request.put("user_creator_id", params[0]);
                    put_request.put("user_receiver_id", params[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String response = ServiceHandler.deleteChatConversation(put_request);
                if(response != null)
                    return getChatConversations(userLoggedIn.getEmail());
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<User> result) {
                if(result != null){
                    Toast.makeText(getActivity(), "Chat conversation deleted!", Toast.LENGTH_SHORT).show();
                    setupAdapter(result);
                }
            }
        };
        deleteConversationTask.execute(user_creator,user_receiver);
    }
}

