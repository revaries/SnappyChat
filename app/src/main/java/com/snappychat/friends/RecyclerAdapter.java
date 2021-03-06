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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FriendViewHolder> {
    private static String TAG = "RECYCLER_FRIENDS";
    private ArrayList<FriendCard> mDataset;

    public RecyclerAdapter(ArrayList<FriendCard> myDataset) {
        mDataset = myDataset;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public Button mButton;
        public FriendViewHolder(View v) {
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
    public RecyclerAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Integer layoutId = R.layout.card_view;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getName() + " " +mDataset.get(position).getLast());
        holder.mTextViewCardName.setText(mDataset.get(position).getDescription());
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
