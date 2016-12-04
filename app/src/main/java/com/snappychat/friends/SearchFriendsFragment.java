package com.snappychat.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snappychat.R;

/**
 * Created by Jelson on 12/2/2016.
 */

public class SearchFriendsFragment extends Fragment {
    public static final String LAYOUT = "SEARCH";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_friends_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view_search);
        rv.setHasFixedSize(true);
        RecyclerAdapter adapter = new RecyclerAdapter(new String[]{"test search one", "test search two", "test three", "test four", "test five" , "test six" , "test seven","test eight", "test nine", "test 10"});
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }
}
