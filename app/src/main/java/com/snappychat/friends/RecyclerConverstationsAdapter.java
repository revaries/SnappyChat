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
 * Created by Jelson on 12/8/16.
 */

public class RecyclerConverstationsAdapter extends RecyclerView.Adapter<RecyclerConverstationsAdapter.ChatConversationViewHolder> {
    private static String TAG = "RECYCLER_FRIENDS";
    private ArrayList<User> mDataset;

    public RecyclerConverstationsAdapter(ArrayList<User> myDataset) {
        mDataset = myDataset;
    }

    public static class ChatConversationViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public Button mButton;
        public ChatConversationViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.card_text);
            mTextViewCardName = (TextView) v.findViewById(R.id.card_name);
            mButton = (Button) v.findViewById(R.id.card_view_button);
            setButtonTextAndListener(mButton, "View Profile");

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
    public RecyclerConverstationsAdapter.ChatConversationViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                       int viewType) {
        Integer layoutId = R.layout.card_view;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        RecyclerConverstationsAdapter.ChatConversationViewHolder vh =
                new RecyclerConverstationsAdapter.ChatConversationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerConverstationsAdapter.ChatConversationViewHolder holder, int position) {
//        holder.mTextView.setText(mDataset.get(position).getName() + " " +mDataset.get(position).getLast());
//        holder.mTextViewCardName.setText(mDataset.get(position).getDescription());
        holder.mTextView.setText(mDataset.get(position).getFirstName() + " " +mDataset.get(position).getLastName());
        holder.mTextViewCardName.setText(mDataset.get(position).getMessage());
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

