package com.snappychat.friends;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.friends.SearchUserFragment.OnListFragmentInteractionListener;
import com.snappychat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private final OnListFragmentInteractionListener mListener;


    public MyItemRecyclerViewAdapter(ArrayList<User> values, SearchUserFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
        mValues = values;
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
        holder.nickTextView.setText("@"+holder.mItem.getNickName());
        holder.infoTextView.setText(holder.mItem.getFirstName()+" "+holder.mItem.getLastName());
        if(!holder.mItem.getStatus()){
            holder.statusImageView.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
            holder.statusTextView.setText("Off");
        }else
            holder.statusTextView.setText("On");
        if(holder.mItem.getImage() != null && !holder.mItem.getImage().isEmpty()) {
            holder.imageProfileImageView.setImageBitmap(holder.mItem.getImageIntoBitmap());
            holder.imageProfileImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.imageProfileImageView.setImageTintList(ColorStateList.valueOf(holder.imageProfileImageView.getContext().getResources().getColor(R.color.com_facebook_blue)));
            }

        }
        
        if(!holder.mItem.isFriend()){
            holder.friendImageView.setVisibility(View.GONE);
            holder.friendTextView.setVisibility(View.GONE);
            holder.chatButton.setVisibility(View.GONE);
            holder.addFriendButton.setVisibility(View.VISIBLE);
        }else {
            holder.addFriendButton.setVisibility(View.GONE);
            holder.friendImageView.setVisibility(View.VISIBLE);
            holder.friendTextView.setVisibility(View.VISIBLE);
            holder.friendTextView.setText("Friend");
        }
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onChatRequested(holder.mItem);
                }
            }
        });

        holder.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFriendAdded(holder.mItem);
                }
            }
        });

        holder.removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFriendRemoved(holder.mItem);
                }
            }
        });
        holder.profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onProfileRequested(holder.mItem);
                }
            }
        });
    }

    public void updateData(ArrayList<User> data){
        mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nickTextView;
        public final ImageView imageProfileImageView;
        public final TextView infoTextView;
        public final ImageView statusImageView;
        public final TextView statusTextView;
        public final ImageView friendImageView;
        public final TextView friendTextView;
        public final ImageButton profileButton;
        public final ImageButton chatButton;
        public final ImageButton addFriendButton;
        public final ImageButton removeFriendButton;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nickTextView = (TextView) view.findViewById(R.id.nick_text);
            imageProfileImageView = (ImageView) view.findViewById(R.id.image_profile);
            infoTextView = (TextView) view.findViewById(R.id.info_text);
            statusImageView = (ImageView) view.findViewById(R.id.status_image);
            statusTextView = (TextView) view.findViewById(R.id.status_text);
            friendTextView = (TextView) view.findViewById(R.id.friend_text);
            friendImageView = (ImageView) view.findViewById(R.id.friend_image);
            profileButton= (ImageButton) view.findViewById(R.id.profile_button);
            chatButton = (ImageButton) view.findViewById(R.id.chat_button);
            addFriendButton = (ImageButton) view.findViewById(R.id.add_friend_button);
            removeFriendButton = (ImageButton) view.findViewById(R.id.remove_friend_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + infoTextView.getText() + "'";
        }
    }
}
