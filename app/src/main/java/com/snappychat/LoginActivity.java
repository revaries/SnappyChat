package com.snappychat;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    //Creating Objects
    private FirebaseAuth snappyauth;
    private FirebaseAuth.AuthStateListener snappyauthListener;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private SignInButton googlesignIn;
    private static final int currentcode = 1001;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Firebase Authentication Intiatlization
        snappyauth = FirebaseAuth.getInstance();

        //Firebase Authentication Listener Intialization
        snappyauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.v("User Logged In", user.getUid());
                } else {
                    Log.v("User SIgned out", "No User");
                }
            }
        };
        //Firebase Authentication Listener Closed

        //Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        googlesignIn = (SignInButton) findViewById(R.id.google_login_button);
        googlesignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignInOperation For Google
                try{
                    Intent googlesigninIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(googlesigninIntent,currentcode);
                }
                catch (Error e)
                {
                    Log.v("Error",e.toString());
                }
            }
        });




        //Google SIgn in Complete

        //Facebook Login Button Intialization
        callbackManager = CallbackManager.Factory.create();
        LoginButton fbloginbutton = (LoginButton) findViewById(R.id.facebook_login_button);
        fbloginbutton.setReadPermissions("email", "public_profile");
        fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "facebook:onSuccess:" + loginResult);
                Log.d("Facebook Login", loginResult.getAccessToken().getToken().toString());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook Login", "Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook Login", error.toString());
            }
        });
        //Facebook Login Button Closure

    }

    @Override
    protected void onStart() {
        super.onStart();
        snappyauth.addAuthStateListener(snappyauthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (snappyauthListener!=null)
        {
            snappyauth.removeAuthStateListener(snappyauthListener);
        }
    }

    private void GoogleSignIn()
    {


    }

    //Trying LoginCallBacks
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);
        //Call Back Facebook SDK
        callbackManager.onActivityResult(requestcode,resultcode,data);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Google SIgn In",connectionResult.toString());
    }
}
