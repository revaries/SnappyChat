package com.snappychat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.snappychat.model.User;
import com.snappychat.networking.ServiceHandler;
import com.snappychat.profile.ProfileDataCollectorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "Firebase Login";
    public static final String STATUS = "status";

    //Creating Objects
    private FirebaseAuth snappyauth;
    private FirebaseAuth.AuthStateListener snappyauthListener;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private SignInButton googlesignIn;
    private LoginButton fbloginbutton;
    private static final int RC_SIGN_IN = 1001;
    public static final String OPERATION = "OPERATION";
    private User user;
    private static User loginuser;
    String email;
    private String token;

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
                    Log.v("User Logged In EMAIL", user.getEmail());
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    email = user.getEmail();
                    token = FirebaseInstanceId.getInstance().getToken();
                    new GetUserTask().execute(email, token);
                } else {
                    googlesignIn.setVisibility(View.VISIBLE);
                    fbloginbutton.setVisibility(View.VISIBLE);
                    snappyauth.signOut();
                    LoginManager.getInstance().logOut();
                    signOut();
                    //Log.v("User SIgned out", "No User");
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
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        googlesignIn = (SignInButton) findViewById(R.id.google_login_button);
        googlesignIn.setSize(SignInButton.SIZE_STANDARD);
        googlesignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_login_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });


        //Google SIgn in Complete

        //Facebook Login Button Intialization
        callbackManager = CallbackManager.Factory.create();
        fbloginbutton = (LoginButton) findViewById(R.id.facebook_login_button);
        fbloginbutton.setReadPermissions("email", "public_profile");
        fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "facebook:onSuccess:" + loginResult);
                Log.d("Facebook Login", loginResult.getAccessToken().getToken().toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
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

    }

    private void signOut() {
        if(googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.d(TAG,"Google user log out");
                            //if (snappyauthListener != null) {
                            //    snappyauth.removeAuthStateListener(snappyauthListener);
                            //}
                        }
                    });
        }
    }

    private void selectActivity() {
        if(user!=null) { //If user is found, redirect to timeline
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.putExtra(MainActivity.USER_LOGGED_IN,user);
            i.putExtra(MainActivity.FROM_LOGIN,true);
            loginuser = user;
            startActivity(i);
        }else { //if user doesn't exist redirects to user profile activity
            user = new User();
            loginuser = user;
            user.setEmail(email);
            user.setToken(token);
            //Intent i = new Intent(getBaseContext(), UserProfileActitivy.class);
            Intent i = new Intent(getBaseContext(),ProfileDataCollectorActivity.class);
            i.putExtra(MainActivity.USER_LOGGED_IN,user);
            i.putExtra(OPERATION,"NEW");
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        snappyauth.addAuthStateListener(snappyauthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (snappyauthListener != null) {
            snappyauth.removeAuthStateListener(snappyauthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Trying LoginCallBacks
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestcode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }else{
            //Call Back Facebook SDK
            callbackManager.onActivityResult(requestcode, resultcode, data);
        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Google SIgn In", connectionResult.toString());
    }

    public void setUser(User user){
        this.user = user;
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        snappyauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                            // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        snappyauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    private class GetUserTask extends AsyncTask<String,Void,User> {
        @Override
        protected User doInBackground(String... params) {
            //Update user status to online
            JSONObject user = new JSONObject();
            try {
                //user.put("status",true);
                user.put("token",params[1]);
                ServiceHandler.updateUser(params[0],user);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException",e);
            }
            return ServiceHandler.getUser(params[0]);
        }


        @Override
        protected void onPostExecute(User user) {
            setUser(user);
            selectActivity();
        }
    }

    public User getLoginuser()
    {
        return loginuser;
    }
}
