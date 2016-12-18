package com.snappychat.profile;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.snappychat.LoginActivity;
import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.ImageUtils;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.snappychat.MainActivity.USER_LOGGED_IN;

public class ProfileDataCollectorActivity extends AppCompatActivity implements ProfileDataNameQuestionsFragment.OnFragmentInteractionListener, ProfileDataSettingsQuestionsFragment.OnFragmentInteractionListener, ProfileDataUserInterestsQuestionsFragment.OnFragmentInteractionListener, ProfileDataProfQuestionsFragment.OnFragmentInteractionListener, ProfileDataProfilePictureFragment.OnFragmentInteractionListener {

    private Uri imagefileuri;
    final int REQUEST_IMAGE_CAPTURE = 1;
    public final static String EDIT = "EDIT";
    public final static String NEW = "NEW";
    private User snappyuser;
    private Fragment ProfileNameQuestionFragment;
    private Fragment ProfileSettingsQuestionFragment;
    private Fragment ProfileInterestQuestionFragment;
    private Fragment ProfileProfessionQuestionFragment;
    private Fragment ProfilePictureFragment;
    private FragmentManager snappyfragmanager;
    private String operation;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        if (operation.equals("NEW")) {
            snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileNameQuestionFragment).commit();
        } else if (operation.equals("EDIT")) {
            String page = incoming.getStringExtra("FragmentToEdit");

            switch (page) {
                case "ProfilePicture":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder,ProfilePictureFragment).commit();
                    break;
                case "Name":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileNameQuestionFragment).commit();
                    break;
                case "Interests":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileInterestQuestionFragment).commit();
                    break;
                case "Profession":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileProfessionQuestionFragment).commit();
                    break;
                case "settings":
                    snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileSettingsQuestionFragment).commit();
            }
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public User getUserObject() {
        return snappyuser;
    }

    public String getOperation() {
        return operation;
    }

    public void nextpagehandler(String pageid) {
        switch (pageid) {
            case "name":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileInterestQuestionFragment).commit();
                break;
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileProfessionQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfilePictureFragment).commit();
                break;
            case "profilepicture":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileSettingsQuestionFragment).commit();
                break;
            case "settings":
                callTimeLine();
                break;

        }

    }

    public void prevpagehandler(String pageid) {
        switch (pageid) {
            case "interests":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileNameQuestionFragment).commit();
                break;
            case "profession":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileInterestQuestionFragment).commit();
                break;
            case "profilepicture":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfileProfessionQuestionFragment).commit();
                break;
            case "settings":
                snappyfragmanager.beginTransaction().replace(R.id.profiledata_fragmentholder, ProfilePictureFragment).commit();
                break;

        }
    }

    public void savePageHandeler() {
        saveUpdatedValues();

    }

    public void profileEditHandler(String editRequestItem) {
        switch (editRequestItem) {
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


    private void callTimeLine() {

        try {
            new NewUserCreator().execute();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }


    }

    private void saveUpdatedValues() {
        try {
            new UpdateUser().execute();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ProfileDataCollector Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    */
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private class NewUserCreator extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            Gson userGson = new Gson();
            JSONObject jsonObject = null;
            String userString = userGson.toJson(snappyuser);
            //jsonObject = new JSONObject(userGson.toJson(snappyuser));
            //jsonObject.put("image", new JSONObject().put("data",snappyuser.getImage()));
            return ServiceHandler.createUser(userString);

        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                if (response.toLowerCase().contains("user created")) {
                    try {
                        Intent timelineIntent = new Intent(getBaseContext(), MainActivity.class);
                        timelineIntent.putExtra(USER_LOGGED_IN, snappyuser);
                        startActivity(timelineIntent);
                    } catch (Exception e) {
                        Log.v("Main Activity", e.toString());
                    }

                }
            } else {
                Log.e("", "");
            }
        }
    }

    private class UpdateUser extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            Gson userGson = new Gson();
            String userString = userGson.toJson(snappyuser);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(userString);
                jsonObject.remove("email");
                Log.v("My JSON Object", userString.toString());
            } catch (Exception e) {
                Log.e("JSON Conversion", e.toString());
            }
            return ServiceHandler.updateUserWithString(snappyuser.getEmail(), jsonObject.toString());
        }

        @Override
        protected void onPostExecute(String response) {
            Log.v("Response", response);
            if (response != null) {
                Toast.makeText(ProfileDataCollectorActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                Intent profileViewIntent = new Intent(getBaseContext(), ProfileViewActivity.class);
                //Intent timelineIntent = new Intent(getBaseContext(), ProfileView.class);

                profileViewIntent.putExtra(USER_LOGGED_IN, snappyuser);
                startActivity(profileViewIntent);

            }else{
                Toast.makeText(ProfileDataCollectorActivity.this, "User creation failed.",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void profilePictureSelector() {
        File camimageholders = new File(Environment.getExternalStorageDirectory() + File.separator + "ProfilePictures" + File.separator);
        camimageholders.mkdirs();
        String capturedimagename = "snappychat profile picture";
        File imageinstorage = new File(camimageholders, capturedimagename);

        imagefileuri = Uri.fromFile(imageinstorage);

        List<Intent> camIntents = new ArrayList<Intent>();
        Intent camerapictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> syscamlists = packageManager.queryIntentActivities(camerapictureIntent, 0);
        for (ResolveInfo each : syscamlists) {
            String packagename = each.activityInfo.packageName;
            Intent eachintent = new Intent(camerapictureIntent);
            eachintent.setComponent(new ComponentName(each.activityInfo.packageName, each.activityInfo.name));
            eachintent.setPackage(packagename);
            camIntents.add(eachintent);
        }

        Intent galleryintent = new Intent();
        galleryintent.setType("image/*");
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);


        Intent chooserintent = Intent.createChooser(galleryintent, "");
        chooserintent.putExtra(Intent.EXTRA_INITIAL_INTENTS, camIntents.toArray(new Parcelable[camIntents.size()]));
        startActivityForResult(chooserintent, REQUEST_IMAGE_CAPTURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imagebitmap = null;
            Bundle extras = data.getExtras();
            if (extras != null) {
                imagebitmap = (Bitmap) extras.get("data");
            } else {
                Uri imguri = data.getData();
                try {
                    imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imguri);
                    imagebitmap = scaleDownBitmap(imagebitmap,70,this);
                    //imagebitmap = ImageUtils.scaleImageAspectRatio(imagebitmap);
                } catch (IOException e) {
                    Log.e("IOEXception", e.toString());
                }

            }
            if (imagebitmap != null) {
                //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //ImageUtils.compressImage(imagebitmap);//Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                //byte[] imagebyte = byteArrayOutputStream.toByteArray();
                //String imagestring = Base64.encodeToString(imagebyte, Base64.DEFAULT);
                String imagestring = ImageUtils.encodeImageBase64(imagebitmap);
                snappyuser.setImage(imagestring);
                ProfileDataProfilePictureFragment profpictFrag = (ProfileDataProfilePictureFragment) ProfilePictureFragment;
                profpictFrag.setProfilePicture(imagestring);

            }

        }
    }


    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }


}
