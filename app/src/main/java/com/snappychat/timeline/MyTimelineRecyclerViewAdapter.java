package com.snappychat.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.ImageUtils;
import com.snappychat.model.Timeline;

import java.text.SimpleDateFormat;
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
                .inflate(R.layout.fragment_timeline,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getId());
        //holder.mContentView.setText(mValues.get(position).getComment());
        holder.date.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(holder.mItem.getCreationDate()));
        holder.postText.setText(mValues.get(position).getComment());

        List<String> cardImages = mValues.get(position).getImages();

        if (cardImages!=null) {
            int total = cardImages.size();
            int column = 5;
            int row = total / column;
            holder.postGrid.setColumnCount(column);
            holder.postGrid.setRowCount(row + 1);
            for(int i =0, c = 0, r = 0; i < total; i++, c++){
                if(c == column)
                {
                    c = 0;
                    r++;
                }
                String stringImage = cardImages.get(i);
                ImageView imageView = new ImageView(activityConext);
                //imageView.setLayoutParams(new GridLayout.LayoutParams(240,320));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5,5,5,5);
                imageView.setVisibility(View.VISIBLE);
                Bitmap bitmap = ImageUtils.decodeImageBase64(stringImage);
                imageView.setImageBitmap(ImageUtils.decodeImageBase64(stringImage));
                imageView.setBackground(activityConext.getResources().getDrawable(R.drawable.image_border));
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                if(bitmap.getHeight() > bitmap.getWidth()){
                    param.height = 320;//GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = 240; //GridLayout.LayoutParams.WRAP_CONTENT;
                }else{
                    param.height = 240;//GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = 320; //GridLayout.LayoutParams.WRAP_CONTENT;
                }
                param.rightMargin = 5;
                param.topMargin = 5;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                imageView.setLayoutParams (param);
                holder.postGrid.addView(imageView);

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
        public TextView date;
        public TextView postText;
        public GridLayout postGrid;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            /*mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);*/
            date = (TextView) view.findViewById(R.id.date);
            postText = (TextView) view.findViewById(R.id.comment);
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
