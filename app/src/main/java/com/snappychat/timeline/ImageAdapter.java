package com.snappychat.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.snappychat.R;

import java.util.ArrayList;

/**
 * Created by Fabrizio on 10/12/2016.
 */

public class ImageAdapter extends BaseAdapter {

    private ArrayList<Bitmap> imagesList;
    private Context mContext;
    private OnImageGridViewSelectedListener mListener;

    public ImageAdapter(Context context, ArrayList<Bitmap> imagesList) {
        super();
        this.imagesList = imagesList;
        mContext = context;
        if (context instanceof OnImageGridViewSelectedListener) {
            mListener = (OnImageGridViewSelectedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
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

    public interface OnImageGridViewSelectedListener {
        void onImageRemoved(Bitmap item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = (LinearLayout) convertView;
        if (convertView == null) { // if it's not recycled, initialize some


            linearLayout = new LinearLayout(mContext);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            ImageButton imageButton = new ImageButton(mContext);

            imageButton.setImageResource(R.drawable.ic_delete_black_24dp);
            // attributes
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(240,320));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(5,5,5,5);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackground(mContext.getResources().getDrawable(R.drawable.image_border));
            linearLayout.addView(imageView);
            linearLayout.addView(imageButton);

        } else {

            ImageView imageView = (ImageView) linearLayout.getChildAt(0);
            ImageButton imageButton = (ImageButton) linearLayout.getChildAt(1);
            linearLayout.removeView(imageView);
            linearLayout.removeView(imageButton);
            final Bitmap bitmap = getImagesList().get(position);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onImageRemoved(bitmap);
                }
            });
            if(bitmap.getHeight() > bitmap.getWidth()){
                imageView.setLayoutParams(new GridView.LayoutParams(240,320));
            }else{
                imageView.setLayoutParams(new GridView.LayoutParams(320,240));
            }
            imageView.setImageBitmap(getImagesList().get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(getImagesList().get(position));
                }
            });
            linearLayout.addView(imageView);
            linearLayout.addView(imageButton);
        }




        return linearLayout;
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

    public void showDialog(final Bitmap imageBitMap) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setCancelable(true);
        LayoutInflater factory = LayoutInflater.from(mContext);
        View view = factory.inflate(R.layout.send_image_dialog, null);
        ImageView imgView = (ImageView) view.findViewById(R.id.dialog_imageview);
        imgView.setPadding(5,30,5,30);
        imgView.setImageBitmap(imageBitMap);
        //imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //ImageUtils.scaleImageAspectRatio(imageBitMap);
        builder1.setView(view);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
