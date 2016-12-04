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

/**
 * Created by Jelson on 12/2/2016.
 */

public class RecyclerAdapterInvitation extends RecyclerView.Adapter<RecyclerAdapterInvitation.FriendViewHolder> {
    private static String TAG = "RECYCLER_INVITATION";
    private String[] mDataset;

    public RecyclerAdapterInvitation(String[] myDataset) {
        mDataset = myDataset;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public Button mButton;
        public FriendViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_invitation);
            mTextView = (TextView) v.findViewById(R.id.card_text);
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
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

