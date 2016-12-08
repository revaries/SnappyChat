package com.snappychat.profile;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappychat.LoginActivity;
import com.snappychat.MainActivity;
import com.snappychat.R;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ProfileViewActivity extends AppCompatActivity {

    public static final String USER_TYPE = "user type";
    private ImageView profilepicture;
    private Uri imagefileuri;
    final int REQUEST_IMAGE_CAPTURE = 1;
    private User userLoggedIn;
    private TextView Name;
    private TextView Interests;
    private TextView Location;
    private TextView Profession;
    private TextView AboutMe;
    private TextView NickName;
    private TextView Visibility;
    private TextView Notifications;
    private TextView Email;
    private User profileUser;

    private MainActivity mainActivity;

    //private TextView Birthday;
    private String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileview);

        Name = (TextView) findViewById(R.id.name);
        Interests = (TextView) findViewById(R.id.interests);
        Location = (TextView) findViewById(R.id.location);
        Profession = (TextView) findViewById(R.id.profession);
        AboutMe = (TextView) findViewById(R.id.about_me);
        //Birthday = (TextView) findViewById(R.id.birthday);
        profilepicture = (ImageView) findViewById(R.id.profilepicture);
        NickName = (TextView) findViewById(R.id.nick_name);
        Visibility = (TextView)findViewById(R.id.visibility);
        Notifications = (TextView)findViewById(R.id.notifications);
        Email = (TextView)findViewById(R.id.email);

        mainActivity = new MainActivity();

        Intent intent = getIntent();
        profileUser = (User) intent.getSerializableExtra(MainActivity.USER_LOGGED_IN);

        userLoggedIn =  mainActivity.getUserforShare();


        Name.setText(profileUser.getFirstName()+" "+profileUser.getLastName());
        Interests.setText(profileUser.getInterests());
        Location.setText(profileUser.getLocation());
        Profession.setText(profileUser.getProfession());
        AboutMe.setText(profileUser.getAboutMe());
        Visibility.setText(profileUser.getVisibility());
        Notifications.setText(profileUser.getNotification().toString());
        NickName.setText(profileUser.getNickName());
        Email.setText(profileUser.getEmail());


        Name.setOnClickListener(textClickListeners);
        Interests.setOnClickListener(textClickListeners);
        Location.setOnClickListener(textClickListeners);
        Profession.setOnClickListener(textClickListeners);
        AboutMe.setOnClickListener(textClickListeners);
        Visibility.setOnClickListener(textClickListeners);
        Notifications.setOnClickListener(textClickListeners);
        NickName.setOnClickListener(textClickListeners);


        if (profileUser.getImage()!=null)
        {

            byte[] imagearray = Base64.decode(profileUser.getImage(), Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
            profilepicture.setImageBitmap(image);
        }



        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLoggedIn.getEmail().equals(profileUser.getEmail())) {
                    Log.v("Clicked On Picture", " Camera APp should Open ");
                    ProfilePictureSelector();
                }
            }
        });
    }



    /*

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

     */



    private View.OnClickListener textClickListeners = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (profileUser.getEmail().equals(userLoggedIn.getEmail()))
            {
                Intent editIntent = new Intent(getBaseContext(),ProfileDataCollectorActivity.class);
                editIntent.putExtra(LoginActivity.OPERATION,"EDIT");
                editIntent.putExtra(MainActivity.USER_LOGGED_IN,userLoggedIn);
                if (view == Name)
                {
                    editIntent.putExtra("FragmentToEdit","Name");
                }
                else if (view==Interests)
                {
                    editIntent.putExtra("FragmentToEdit","Interests");
                }
                else if (view == Location)
                {
                    editIntent.putExtra("FragmentToEdit","Profession");
                }
                else if (view == Profession)
                {
                    editIntent.putExtra("FragmentToEdit","Profession");
                }
                else if (view == AboutMe)
                {
                    editIntent.putExtra("FragmentToEdit","Interests");
                }
                else if (view == Visibility)
                {
                    editIntent.putExtra("FragmentToEdit","settings");
                }
                else if (view == NickName)
                {
                    editIntent.putExtra("FragmentToEdit","Name");
                }
                else if (view==Notifications)
                {
                    editIntent.putExtra("FragmentToEdit","settings");
                }
                startActivity(editIntent);
            }

        }
    };

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
            imagebitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imagebyte = byteArrayOutputStream.toByteArray();
        }
    }

}
