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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

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
    private int mColumnCount = 1;
    private User user;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    ArrayList<User> users;
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
        args.putSerializable(MainActivity.USER,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            user = (User) getArguments().get(MainActivity.USER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            users = new ArrayList<User>();

            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(users, mListener));
        //}

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                users = null;
                setupAdapter();
                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //clearBuildingMarkers();
                if(!(query.equals(""))){
                    //clearBuildingMarkers();
                    processQuery(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(!(query.equals(""))){
                    //clearBuildingMarkers();
                    processQuery(query);
                }
                return false;
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                users = null;
                setupAdapter();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //clearBuildingMarkers();
                if(!(query.equals(""))){
                    //clearBuildingMarkers();
                    processQuery(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(!(query.equals(""))){
                    //clearBuildingMarkers();
                    processQuery(query);
                }
                return false;
            }
        });
    }

    void setupAdapter() {
        if (getActivity() == null || recyclerView == null) return;
        if (users != null) {
//            mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(),
//                    android.R.layout.simple_gallery_item, mItems));
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(users,mListener));
        } else {
            recyclerView.setAdapter(null);
        }
    }

    public ArrayList<String> processQuery(String query){
        ArrayList<String> matches = new ArrayList<>();
        String[] words = query.toLowerCase().split("\\s+");
        new FilterUsersTask().execute(user,query);
        return matches;
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
        void onListFragmentInteraction(User item);
    }

    private class FilterUsersTask extends AsyncTask<Object,Void,ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(Object... params) {
            //Update user status to online
            return ServiceHandler.getUsers((User)params[0],(String) params[1]);
        }


        @Override
        protected void onPostExecute(ArrayList<User> users) {
            SearchUserFragment.this.users = users;
            setupAdapter();
        }
    }
}
