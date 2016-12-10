package com.snappychat.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import java.util.ArrayList;

/**
 * Created by Jelson on 12/8/16.
 */

public class RecyclerConverstationsAdapter extends RecyclerView.Adapter<RecyclerConverstationsAdapter.ChatConversationViewHolder> {
    private static String TAG = "CONVERSATION_CHAT";
    private ArrayList<User> mDataset;
    private static Context context = null;
    private final ChatConverstationsFragment.OnListFragmentInteractionListener mListener;

    public RecyclerConverstationsAdapter(ArrayList<User> myDataset, ChatConverstationsFragment.OnListFragmentInteractionListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    public static class ChatConversationViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextViewCardName;
        public ImageButton mButton;
        public ImageView mImage;
        public ChatConversationViewHolder(View v) {
            super(v);
            context = v.getContext();
            mCardView = (CardView) v.findViewById(R.id.card_view_conversations);
            mTextView = (TextView) v.findViewById(R.id.card_text_chat);
            mTextViewCardName = (TextView) v.findViewById(R.id.card_name_chat);
            mButton = (ImageButton) v.findViewById(R.id.remove_chat);
            mImage = (ImageView) v.findViewById(R.id.card_image_chat);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Card view was clicked!");
                }
            });

            mButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Delete chat was clicked!");
                    }
            });

        }
    }

    @Override
    public RecyclerConverstationsAdapter.ChatConversationViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                       int viewType) {
        Integer layoutId = R.layout.card_view_converstations;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        RecyclerConverstationsAdapter.ChatConversationViewHolder vh =
                new RecyclerConverstationsAdapter.ChatConversationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerConverstationsAdapter.ChatConversationViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset.get(position).getFirstName() + " " +mDataset.get(position).getLastName());
        holder.mTextViewCardName.setText(mDataset.get(position).getMessage());
        if(mDataset.get(position).getPending()){
            holder.mImage.setImageResource(R.drawable.circle_unread);
        }else {
            holder.mImage.setImageResource(R.drawable.circle_gray);
        }
        if(!mDataset.get(position).getChatOwner()){
            holder.mButton.setVisibility(View.GONE);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(MainActivity.USER_LOGGED_IN,ChatConverstationsFragment.userLoggedIn);
                intent.putExtra(ChatActivity.USER_RECEIVER,mDataset.get(position));
                context.startActivity(intent);
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mListener.onDeleteChatConversation(ChatConverstationsFragment.userLoggedIn.getEmail(), mDataset.get(position).getEmail());

                Log.d(TAG, "Chat conversation was deleted");
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

