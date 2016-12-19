package com.snappychat.friends;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.User;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class RecyclerAdapterInvitation extends RecyclerView.Adapter<RecyclerAdapterInvitation.FriendViewHolder> {
    private static String TAG = "RECYCLER_INVITATION";
    private ArrayList<User> mDataset;
    private final InvitationSentFragment.OnListFragmentInteractionListener mListener;

    public RecyclerAdapterInvitation(ArrayList<User> myDataset, InvitationSentFragment.OnListFragmentInteractionListener mListener) {
        mDataset = myDataset;
        this.mListener = mListener;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public Button mButton;
        public User mItem;
        public FriendViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_invitation);
            mTextView = (TextView) v.findViewById(R.id.card_text);
            mTextViewCardName = (TextView) v.findViewById(R.id.card_name);
            mButton = (Button) v.findViewById(R.id.card_invitation_button);

            setButtonTextAndListener(mButton, "Cancel");
        }

        public void setButtonTextAndListener(Button button, final String text){
            button.setText(text);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, text + " was clicked!");
                }
            });
        }
    }

    @Override
    public RecyclerAdapterInvitation.FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        Integer layoutId = R.layout.card_view_invitations;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        //holder.mTextView.setText(mDataset.get(position).getFirstName() + " " +mDataset.get(position).getLastName());
        holder.mTextView.setText(mDataset.get(position).getEmail());
        holder.mTextViewCardName.setText(mDataset.get(position).getMessage());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCancelRequest(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateData(ArrayList<User> data){
        mDataset = data;
        notifyDataSetChanged();
    }
}

