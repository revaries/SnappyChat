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
import com.snappychat.model.FriendCard;
import com.snappychat.model.User;

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class RecyclerAdapterPending extends RecyclerView.Adapter<RecyclerAdapterPending.FriendViewHolder> {
    private static String TAG = "RECYCLER_PENDING";
    private ArrayList<User> mDataset;
    private final PendingRequestsFragment.OnListFragmentInteractionListener mListener;

    public RecyclerAdapterPending(ArrayList<User> myDataset, PendingRequestsFragment.OnListFragmentInteractionListener listener) {

        mDataset = myDataset;
        mListener = listener;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public Button mButton;
        public Button mReject;
        public User mItem;
        public FriendViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.card_text);
            mTextViewCardName = (TextView) v.findViewById(R.id.card_name);
            mButton = (Button) v.findViewById(R.id.card_button_accept);
            mReject = (Button) v.findViewById(R.id.card_button_reject);

            setButtonTextAndListener(mButton, "Accept");
            setButtonTextAndListener(mReject, "Reject");
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
    public RecyclerAdapterPending.FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        Integer layoutId = R.layout.card_view_pending;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        holder.mTextView.setText(mDataset.get(position).getFirstName() + " " +mDataset.get(position).getLastName());
        holder.mTextViewCardName.setText(mDataset.get(position).getMessage());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPendingChanged(holder.mItem,true);
                }
            }
        });
        holder.mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPendingChanged(holder.mItem,false);
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

