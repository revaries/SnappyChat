package com.snappychat.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.Timeline;

import java.util.List;


public class MyTimelineRecyclerViewAdapter extends RecyclerView.Adapter<MyTimelineRecyclerViewAdapter.ViewHolder> {

    private List<Timeline> mValues;


    private final TimelineFragment.OnListFragmentInteractionListener mListener;
    private Context activityConext;

    public MyTimelineRecyclerViewAdapter(List<Timeline> items, TimelineFragment.OnListFragmentInteractionListener listener, Context myContext) {
        mValues = items;
        mListener = listener;
        activityConext = myContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_timeline_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getId());
        //holder.mContentView.setText(mValues.get(position).getComment());
        holder.postText.setText(mValues.get(position).getComment());

        List<String> cardImages = mValues.get(position).getImages();

        if (cardImages!=null) {
            for (String eachimage : cardImages) {
                ImageView tempImage = new ImageView(activityConext);
                tempImage.setScaleType(ImageView.ScaleType.FIT_XY);
                tempImage.setImageBitmap(ImageFromBase64(eachimage));
                holder.postGrid.addView(tempImage);

            }
        }


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
        //public final TextView mIdView;
        //public final TextView mContentView;
        public Timeline mItem;

        public TextView postText;
        public GridLayout postGrid;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            /*mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);*/

            postText = (TextView) view.findViewById(R.id.timeline_post_card_text);
            postGrid = (GridLayout) view.findViewById(R.id.timeline_post_card_grid);
        }

        @Override
        public String toString() {
            //return super.toString() + " '" + mContentView.getText() + "'";
            return super.toString();
        }
    }

    public Bitmap ImageFromBase64 (String imagestring)
    {
        byte[] imagearray = Base64.decode(imagestring, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
        return image;
    }
}
