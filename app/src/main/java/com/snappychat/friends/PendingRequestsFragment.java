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

public class PendingRequestsFragment extends Fragment {
    public static final String LAYOUT = "PENDING";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pending_requests_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_view_pending);
        rv.setHasFixedSize(true);
        RecyclerAdapterPending adapter = new RecyclerAdapterPending(new String[]{"test pending one", "test pending two", "test three", "test four", "test five" , "test six" , "test seven","test eight", "test nine", "test 10"});
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }
}
