package com.snappychat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappychat.SearchUserFragment.OnListFragmentInteractionListener;
import com.snappychat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(ArrayList<User> items, SearchUserFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getNickName());
        holder.infoTextView.setText(mValues.get(position).getFirstName()+" "+mValues.get(position).getLastName());
        if(!holder.mItem.getStatus()){
            holder.statusImageView.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
            holder.statusTextView.setText("Off");
        }else
            holder.statusTextView.setText("On");
        if(holder.mItem.getImage() != null && !holder.mItem.getImage().isEmpty())
            holder.infoImageView.setImageBitmap(holder.mItem.getImageIntoBitmap());

        if(!holder.mItem.isFriend()){
            holder.friendImageView.setVisibility(View.INVISIBLE);
            holder.friendTextView.setVisibility(View.INVISIBLE);
            holder.chatButton.setVisibility(View.INVISIBLE);
        }else
            holder.friendTextView.setText("Friend");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView infoImageView;
        public final TextView infoTextView;
        public final ImageView statusImageView;
        public final TextView statusTextView;
        public final ImageView friendImageView;
        public final TextView friendTextView;
        public final ImageButton chatButton;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            infoImageView = (ImageView) view.findViewById(R.id.info_image);
            infoTextView = (TextView) view.findViewById(R.id.info_text);
            statusImageView = (ImageView) view.findViewById(R.id.status_image);
            statusTextView = (TextView) view.findViewById(R.id.status_text);
            friendTextView = (TextView) view.findViewById(R.id.friend_text);
            friendImageView = (ImageView) view.findViewById(R.id.friend_image);
            chatButton = (ImageButton) view.findViewById(R.id.chat_button);
            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Call to Chat Activity
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + infoTextView.getText() + "'";
        }
    }
}
