package com.snappychat.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.snappychat.R;

import java.util.ArrayList;

/**
 * Created by Fabrizio on 10/12/2016.
 */

public class ImageAdapter extends BaseAdapter {

    private ArrayList<Bitmap> imagesList;
    private ImageView imageView;
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<Bitmap> imagesList) {
        super();
        this.imagesList = imagesList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return getImagesList().size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(240,320));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(5,5,5,5);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackground(mContext.getResources().getDrawable(R.drawable.image_border));

        } else {

            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(getImagesList().get(position));
        return imageView;
    }

    public void addPicture(Bitmap image) {
        Log.d("ImageAdapter","Image size: "+image.getByteCount());
        //getImagesEncodedList().add(encodedImage.trim());
        getImagesList().add(image);
        notifyDataSetChanged();
    }


    public ArrayList<Bitmap> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<Bitmap> imagesList) {
        this.imagesList = imagesList;
    }
}
