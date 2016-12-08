package com.snappychat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.snappychat.friends.MyItemRecyclerViewAdapter;
import com.snappychat.model.User;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/7/16.
 */

public class ChatConverstationsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_conversations, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.chat_conversations_recycler_view);
//        recyclerView = (RecyclerView) v.findViewById(R.id.list);
//        recyclerView.removeAllViewsInLayout();
//        recyclerView.setHasFixedSize(true);
//        //adapter = new RecyclerAdapter(currentFriendCards);
//        adapter = new MyItemRecyclerViewAdapter(new ArrayList<User>(),mListener);
//        recyclerView.setAdapter( adapter);
//        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        //LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        //recyclerView.setLayoutManager(llm);
//        getFriendsList();
        return v;
    }


}
