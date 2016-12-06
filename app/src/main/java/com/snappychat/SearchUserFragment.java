package com.snappychat;


import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchUserFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private ProgressBar mProgressBar;
    private int mColumnCount = 1;
    private User userLoggedIn;
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchUserFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchUserFragment newInstance(User user, int columnCount) {
        SearchUserFragment fragment = new SearchUserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(MainActivity.USER_LOGGED_IN,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            userLoggedIn = (User) getArguments().get(MainActivity.USER_LOGGED_IN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new MyItemRecyclerViewAdapter(new ArrayList<User>(),mListener);
        recyclerView.setAdapter(adapter);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery("", true);
        searchView.clearFocus();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setupAdapter(null);
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!(query.equals(""))){
                    mProgressBar.setVisibility(View.VISIBLE);
                    processQuery(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(!(query.equals(""))){
                    mProgressBar.setVisibility(View.VISIBLE);
                    processQuery(query);
                }
                return false;
            }
        });
        clearFields();
    }

    public void clearFields(){
        if(searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            setupAdapter(new ArrayList<User>());
        }

    }

    void setupAdapter(ArrayList<User> users) {
        if (getActivity() == null || recyclerView == null) return;
        mProgressBar.setVisibility(View.GONE);
        adapter.updateData(users);
    }

    public void processQuery(String query){
        getFriends(query);
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
        void onChatRequested(User item);
        void onFriendAdded(User item);
        void onFriendRemoved(User item);
    }


    public void getFriends(String query){
        AsyncTask<Object, Void, ArrayList<User>> userTask = new AsyncTask<Object,Void,ArrayList<User>>(){
            @Override
            protected ArrayList<User> doInBackground(Object... params) {
                //Update user status to online
                return ServiceHandler.getUsers((User)params[0],(String) params[1]);
            }


            @Override
            protected void onPostExecute(ArrayList<User> users) {
                //SearchUserFragment.this.users = users;
                setupAdapter(users);
            }
        };
        userTask.execute(userLoggedIn,query);
    }

    public void addFriend(User userToAdd, String message){
        AsyncTask<String, Void, String> friendsTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",(String)params[0]);
                    jsonObject.put("message",(String)params[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return ServiceHandler.addFriend((String)params[1], jsonObject);
            }

            @Override
            protected void onPostExecute(String result) {
                if(result!=null){
                    Toast.makeText(getActivity(), "Friend request sent!", Toast.LENGTH_SHORT).show();
                }
            }

        };
        friendsTask.execute(userToAdd.getEmail(),userLoggedIn.getEmail(),message);
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
                    setupAdapter(users);
                }

            }

        };
        friendsTask.execute(userToDelete.getEmail(),userLoggedIn.getEmail());
    }
}
