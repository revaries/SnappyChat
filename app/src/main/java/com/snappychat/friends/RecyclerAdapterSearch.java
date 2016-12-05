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

import java.util.ArrayList;

/**
 * Created by Jelson on 12/2/2016.
 */

public class RecyclerAdapterSearch extends RecyclerView.Adapter<RecyclerAdapterSearch.FriendViewHolder> {
    private static String TAG = "RECYCLER_SEARCH";
    private ArrayList<FriendCard> mDataset;

    public RecyclerAdapterSearch(ArrayList<FriendCard> myDataset) {
        mDataset = myDataset;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public Button mButton;
        public FriendViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view_search);
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
    public RecyclerAdapterSearch.FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        Integer layoutId = R.layout.card_view_invitations;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getName() + " " +mDataset.get(position).getLast());
        holder.mTextViewCardName.setText(mDataset.get(position).getDescription());
        if(mDataset.get(position).getFriends()){
            holder.mButton.setText("Friends");
        }else if(mDataset.get(position).getPending()){
            holder.mButton.setText("Pending");
        }else if(mDataset.get(position).getRequested()){
            holder.mButton.setText("Request Sent");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateData(ArrayList<FriendCard> data){
        mDataset = data;
        notifyDataSetChanged();
    }
}

