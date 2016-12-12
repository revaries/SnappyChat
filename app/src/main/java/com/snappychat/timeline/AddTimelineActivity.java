package com.snappychat.timeline;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.Image;
import com.snappychat.model.ImageUtils;
import com.snappychat.model.Timeline;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddTimelineActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageAdapter adapter;
    private EditText timelineComment;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private User userLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timeline);
        userLoggedIn = (User) getIntent().getSerializableExtra(MainActivity.USER_LOGGED_IN);
        gridView = (GridView) findViewById(R.id.mygrid);
        ImageButton addImage = (ImageButton) findViewById(R.id.addImageButton);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePictureSelector();
            }
        });
        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeTimeline();
            }
        });
        timelineComment = (EditText)findViewById(R.id.timelineComment);
        adapter = new ImageAdapter(this,new ArrayList<Bitmap>());
        gridView.setAdapter(adapter);

    }

    private void profilePictureSelector() {
        File camimageholders = new File(Environment.getExternalStorageDirectory() + File.separator + "ProfilePictures" + File.separator);
        camimageholders.mkdirs();
        String capturedimagename = "snappychat profile picture";
        //File imageinstorage = new File(camimageholders,capturedimagename);

        List<Intent> camIntents = new ArrayList<Intent>();
        Intent camerapictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo>  syscamlists = packageManager.queryIntentActivities(camerapictureIntent, 0);
        for(ResolveInfo each: syscamlists){
            String packagename = each.activityInfo.packageName;
            Intent eachintent = new Intent(camerapictureIntent);
            eachintent.setComponent(new ComponentName(each.activityInfo.packageName,each.activityInfo.name));
            eachintent.setPackage(packagename);
            camIntents.add(eachintent);
        }

        Intent galleryintent = new Intent();
        galleryintent.setType("image/*");
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);


        Intent chooserintent = Intent.createChooser(galleryintent,"");
        chooserintent.putExtra(Intent.EXTRA_INITIAL_INTENTS,camIntents.toArray(new Parcelable[camIntents.size()]));
        startActivityForResult(chooserintent,REQUEST_IMAGE_CAPTURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imagebitmap  = null;
            Bundle extras = data.getExtras();
            if (extras!=null)
            {
                imagebitmap = (Bitmap) extras.get("data");
            }
            else
            {
                Uri imguri = data.getData();
                try{
                    imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imguri);
                }
                catch (IOException e)
                {
                    Log.e("IOEXception", e.toString());
                }


            }
            compressImage(imagebitmap);
            //adapter.addPicture(imagebitmap);
        }
    }

    private void callBackActivity(){
        finish();
    }

    private void storeTimeline(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Timeline timeline = new Timeline();
                timeline.setUserId(userLoggedIn.getId());
                timeline.setComment(timelineComment.getText().toString());
                timeline.setImages(new ArrayList<String>());
                for(Bitmap image : adapter.getImagesList()){
                    //Image imageToAdd = new Image();
                    //imageToAdd.setData(ImageUtils.encodeImageBase64(image));
                    timeline.getImages().add(ImageUtils.encodeImageBase64(image));
                }
                String timelineToString = new Gson().toJson(timeline,Timeline.class);
                final String result = ServiceHandler.createTimeline(userLoggedIn.getEmail(),timelineToString);
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(result != null)
                            callBackActivity();
                        else
                            Toast.makeText(AddTimelineActivity.this, "Saving timeline failed.",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

        /*Timeline timeline = new Timeline();
        timeline.setUserId(userLoggedIn.getId());
        timeline.setComment(timelineComment.getText().toString());
        timeline.setImages(adapter.getImagesEncodedList());
        String timelineToString = new Gson().toJson(timeline,Timeline.class);
        AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return createTimeline(params[0],params[1]);
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null)
                    callBackActivity();
                else
                    Toast.makeText(AddTimelineActivity.this, "Saving timeline failed.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        task.execute(userLoggedIn.getEmail(),timelineToString);*/
    }


    private void compressImage(final Bitmap image){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                final Bitmap scaledBitmap = ImageUtils.scaleImageAspectRatio(image);
                //To run on UI Thread
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addPicture(scaledBitmap);
                    }
                });
            }
        }).start();
    }
}
