package com.snappychat.friends;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.ChatMessage;
import com.snappychat.model.ImageUtils;

import java.util.ArrayList;

/**
 * Created by Jelson on 11/27/16.
 */

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<ChatMessage> chatMessageList;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return getChatMessageList().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = (ChatMessage) getChatMessageList().get(position);
        View vi = convertView;
        if(message.getType().equals("text")){
            vi = inflater.inflate(R.layout.adapter_chat, null);
            vi = setTextMessagesView(vi, message);
        }else if(message.getType().equals("image")){
            vi = inflater.inflate(R.layout.adapter_chat_image, null);
            vi = setImageMessagesView(vi, message);
        }

        return vi;
    }

    public View setImageMessagesView(View vi, ChatMessage message){
        ImageView imgView = (ImageView) vi.findViewById(R.id.imageview_message);
        //byte[] imagearray = Base64.decode(message.body, Base64.DEFAULT);
        //Bitmap image = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
        Bitmap image = ImageUtils.decodeImageBase64(message.body);
        imgView.setImageBitmap(image);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.imageview_layout_parent);
        if(message.isMine){
            parent_layout.setGravity(Gravity.RIGHT);
        }else{
            parent_layout.setGravity(Gravity.LEFT);
        }

        return vi;
    }

    public View setTextMessagesView(View vi, ChatMessage message){
        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        msg.setText(message.body);
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);
        // if message is mine then align to right
        if (message.isMine) {
            layout.setBackgroundResource(R.drawable.bubble2);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layout.setBackgroundTintList(ColorStateList.valueOf(vi.getContext().getResources().getColor(R.color.com_facebook_button_login_silver_background_color)));
            }
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layout.setBackgroundTintList(ColorStateList.valueOf(vi.getContext().getResources().getColor(R.color.com_facebook_button_background_color_selected)));
            }
            parent_layout.setGravity(Gravity.LEFT);


        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(ChatMessage object) {
        getChatMessageList().add(object);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<ChatMessage> list){
        this.chatMessageList = list;
        notifyDataSetChanged();
    }

    public ArrayList<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }
}
