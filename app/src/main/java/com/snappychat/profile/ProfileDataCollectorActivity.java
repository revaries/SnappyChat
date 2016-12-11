package com.snappychat.profile;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.snappychat.LoginActivity;
import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class ProfileDataCollectorActivity extends AppCompatActivity implements ProfileDataNameQuestionsFragment.OnFragmentInteractionListener,ProfileDataSettingsQuestionsFragment.OnFragmentInteractionListener,ProfileDataUserInterestsQuestionsFragment.OnFragmentInteractionListener,ProfileDataProfQuestionsFragment.OnFragmentInteractionListener,ProfileDataProfilePictureFragment.OnFragmentInteractionListener{

    private Uri imagefileuri;
    final int REQUEST_IMAGE_CAPTURE = 1;
    public final static String EDIT = "EDIT";
    public final static String NEW = "NEW";
    private static User snappyuser;
    private Fragment ProfileNameQuestionFragment;
    private Fragment ProfileSettingsQuestionFragment;
    private Fragment ProfileInterestQuestionFragment;
    private Fragment ProfileProfessionQuestionFragment;
    private Fragment ProfilePictureFragment;
    private FragmentManager snappyfragmanager;
    private String operation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledatacollector);

        Intent incoming = getIntent();
        snappyuser = (User) incoming.getSerializableExtra(USER_LOGGED_IN);
        operation = incoming.getStringExtra(LoginActivity.OPERATION);

        ProfileNameQuestionFragment = new ProfileDataNameQuestionsFragment();
        ProfileSettingsQuestionFragment = new ProfileDataSettingsQuestionsFragment();
        ProfileInterestQuestionFragment = new ProfileDataUserInterestsQuestionsFragment();
        ProfileProfessionQuestionFragment = new ProfileDataProfQuestionsFragment();
        ProfilePictureFragment = new ProfileDataProfilePictureFragment();

        snappyfragmanager = getSupportFragmentManager();

        if (operation.equals("NEW"))
        {
            snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();
        }
        else if (operation.equals("EDIT"))
        {
            String page = incoming.getStringExtra("FragmentToEdit");

            switch(page){
                case "Name":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileNameQuestionFragment).commit();
                    break;
                case "Interests":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileInterestQuestionFragment).commit();
                    break;
                case "Profession":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileProfessionQuestionFragment).commit();
                    break;
                case "settings":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfileSettingsQuestionFragment).commit();
            }
        }




    }


    public User getUserObject()
    {
        return snappyuser;
    }

    public String getOperation()
    {
        return operation;
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

    public void savePageHandeler()
    {
        saveUpdatedValues();

    }

    public void profileEditHandler(String editRequestItem)
    {
        switch (editRequestItem){
            case "one":
                break;
            case "two":
                break;
            case "three":
                break;
            case "four":
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
        //Intent timelineIntent = new Intent(getBaseContext(), ProfileView.class);
        timelineIntent.putExtra(USER_LOGGED_IN,snappyuser);
        startActivity(timelineIntent);

    }

    private void saveUpdatedValues()
    {
        try
        {
            new UpdateUser().execute();
        }
        catch (Exception e )
        {
            Log.e("Error",e.toString());
        }
        Intent timelineIntent = new Intent(getBaseContext(),ProfileViewActivity.class);
        //Intent timelineIntent = new Intent(getBaseContext(), ProfileView.class);
        timelineIntent.putExtra(USER_LOGGED_IN,snappyuser);
        startActivity(timelineIntent);

    }

    private class NewUserCreator extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            Gson userGson = new Gson();
            String userString = userGson.toJson(snappyuser);
            Log.v("Image Detail",snappyuser.getImage());
            Log.v("My JSON Object",userString.toString());
            ServiceHandler.createUser(userString);
            return null;
        }

    }

    private class UpdateUser extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            Gson userGson = new Gson();
            String userString = userGson.toJson(snappyuser);
            JSONObject jsonObject;
            try{
                jsonObject  = new JSONObject(userString);
                jsonObject.remove("email");
                Log.v("My JSON Object",userString.toString());
                ServiceHandler.updateUserWithString(snappyuser.getEmail(),jsonObject.toString());

            }
            catch (Exception e)
            {
                Log.e("JSON Conversion",e.toString());
            }



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
                    imagebitmap = scaleDownBitmap(imagebitmap,50,this);
                }
                catch (IOException e)
                {
                    Log.e("IOEXception", e.toString());
                }

            }
            if (imagebitmap!=null)
            {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] imagebyte = byteArrayOutputStream.toByteArray();
                String imagestring = Base64.encodeToString(imagebyte, Base64.DEFAULT);
                snappyuser.setImage(imagestring);
                ProfileDataProfilePictureFragment profpictFrag = (ProfileDataProfilePictureFragment) ProfilePictureFragment;
                profpictFrag.setProfilePicture(imagestring);

            }

        }
    }


    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }


}
