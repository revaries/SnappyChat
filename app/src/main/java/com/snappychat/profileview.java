package com.snappychat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.util.DebugUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class profileview extends AppCompatActivity {

    private ImageView profilepicture;
    private Uri imagefileuri;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextView Name;
    private TextView Interests;
    private TextView Location;
    private TextView Profession;
    private TextView AboutMe;
    private TextView Birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileview);

        Name = (TextView) findViewById(R.id.name);
        Interests = (TextView) findViewById(R.id.interests);
        Location = (TextView) findViewById(R.id.location);
        Profession = (TextView) findViewById(R.id.profession);
        AboutMe = (TextView) findViewById(R.id.about_me);
        Birthday = (TextView) findViewById(R.id.birthday);




        profilepicture = (ImageView) findViewById(R.id.profilepicture);
        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Clicked On Picture"," Camera APp should Open ");
                ProfilePictureSelector();

            }
        });
    }



    private void callingpictureTaker()
    {

        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void ProfilePictureSelector()
    {
        File camimageholders = new File(Environment.getExternalStorageDirectory() + File.separator + "ProfilePictures" + File.separator);
        camimageholders.mkdirs();
        String capturedimagename = "snappychat profile picture";
        File imageinstorage = new File(camimageholders,capturedimagename);

        imagefileuri = Uri.fromFile(imageinstorage);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          /*
            boolean fromcamera;

            if (data == null) {
                fromcamera = true;
            } else {
                final String action = data.getAction();
                if (action == null) {
                    fromcamera = false;
                } else {
                    fromcamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }
            if (fromcamera)
            {
                Bundle extras = data.getExtras();
                imagebitmap = (Bitmap) extras.get("data");
            }
            else
            {
               // if (data == null)
                //{
                    imagebitmap = null;
                //}
                //else
                //{
                    Uri imguri = data.getData();
                    try{
                        imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imguri);
                    }
                    catch (IOException e)
                    {
                        Log.e("IOEXception", e.toString());
                    }

                //}

            }
            */
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
            profilepicture.setImageBitmap(imagebitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 200, byteArrayOutputStream);
            byte[] imagebyte = byteArrayOutputStream.toByteArray();
        }
    }

}