package com.snappychat;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class ProfileDataCollector extends AppCompatActivity implements ProfileDataNameQuestions.OnFragmentInteractionListener,ProfileDataSettingsQuestions.OnFragmentInteractionListener,ProfileDataUserInterestsQuestions.OnFragmentInteractionListener,ProileDataProfQuestions.OnFragmentInteractionListener,ProfileDataProfilePicture.OnFragmentInteractionListener{

    private Uri imagefileuri;
    final int REQUEST_IMAGE_CAPTURE = 1;
    private User snappyuser;
    private Fragment ProfileNameQuestionFragment;
    private Fragment ProfileSettingsQuestionFragment;
    private Fragment ProfileInterestQuestionFragment;
    private Fragment ProfileProfessionQuestionFragment;
    private Fragment ProfilePictureFragment;
    private FragmentManager snappyfragmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledatacollector);

        Intent incoming = getIntent();
        snappyuser = (User) incoming.getSerializableExtra(USER_LOGGED_IN);


        ProfileNameQuestionFragment = new ProfileDataNameQuestions();
        ProfileSettingsQuestionFragment = new ProfileDataSettingsQuestions();
        ProfileInterestQuestionFragment = new ProfileDataUserInterestsQuestions();
        ProfileProfessionQuestionFragment = new ProileDataProfQuestions();
        ProfilePictureFragment = new ProfileDataProfilePicture();



        snappyfragmanager = getSupportFragmentManager();
        snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();

    }


    public User getUserObject()
    {
        return snappyuser;
    }


    public void nextpagehandler(String pageid)
    {
        switch (pageid){
            case "name":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileInterestQuestionFragment).commit();
                break;
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileProfessionQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfilePictureFragment).commit();
                break;
            case "profilepicture":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileSettingsQuestionFragment).commit();
                break;
            case "settings":
                callTimeLine();
                break;

        }

    }

    public void  prevpagehandler(String pageid)
    {
        switch (pageid){
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileInterestQuestionFragment).commit();
                break;
            case "profilepicture":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileProfessionQuestionFragment).commit();
                break;
            case "settings":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfilePictureFragment).commit();
                break;

        }
    }

    private void callTimeLine()
    {

        try{
            new NewUserCreator().execute();
        }
        catch (Exception e)
        {
            Log.e("Error",e.toString());
        }


        Intent timelineIntent = new Intent(getBaseContext(),MainActivity.class);
        timelineIntent.putExtra(USER_LOGGED_IN,snappyuser);
        startActivity(timelineIntent);

    }


    private class NewUserCreator extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            Gson userGson = new Gson();
            String userString = userGson.toJson(snappyuser);
            Log.v("My JSON Object",userString.toString());
            ServiceHandler.createUser(userString);
            return null;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void profilePictureSelector()
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
            if (imagebitmap!=null)
            {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imagebyte = byteArrayOutputStream.toByteArray();
                String imagestring = Base64.encodeToString(imagebyte, Base64.DEFAULT);
                snappyuser.setImage(imagestring);
            }

        }
    }


}
