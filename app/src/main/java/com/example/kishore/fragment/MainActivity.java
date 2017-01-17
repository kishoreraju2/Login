package com.example.kishore.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    public static boolean loggingInFacebook = false;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
       callbackManager = CallbackManager.Factory.create();
       accessTokenTracker = new AccessTokenTracker() {
           @Override
           protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
           }
       };

       profileTracker = new ProfileTracker() {
           @Override
           protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
               nextActivity(newProfile);
           }
       };
       accessTokenTracker.startTracking();
       profileTracker.startTracking();
        setContentView(R.layout.activity_main);
        final EditText etUserName = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button login = (Button) findViewById(R.id.etLogin);
        final Button register = (Button) findViewById(R.id.etRegisterHere);
       final ProgressBar progress = (ProgressBar) findViewById(R.id.loadingProgress);
       LoginButton loginButton = (LoginButton)findViewById(R.id.fbLogin);
       callback = new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               loggingInFacebook = true;
               AccessToken accessToken = loginResult.getAccessToken();
               Profile profile = Profile.getCurrentProfile();
               nextActivity(profile);
               Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();    }

           @Override
           public void onCancel() {
           }

           @Override
           public void onError(FacebookException e) {
           }
       };
       loginButton.setReadPermissions("user_friends");
       loginButton.registerCallback(callbackManager, callback);

       login.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               progress.setVisibility(View.VISIBLE);

               final String username = etUserName.getText().toString();
               final String password = etPassword.getText().toString();

               Response.Listener<String> responseListener = new Response.Listener<String>(){


                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonResponse = new JSONObject(response);
                           boolean success = jsonResponse.getBoolean("success");

                           if(success){
                               String name = jsonResponse.getString("name");
                               int age = jsonResponse.getInt("age");
                               Intent intent = new Intent(MainActivity.this,UserAreaActivity.class);
                               intent.putExtra("name",name);
                               intent.putExtra("age",age);
                               intent.putExtra("username",username);
                               MainActivity.this.startActivity(intent);

                           }else{
                               progress.setVisibility(View.GONE);
                               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                               builder.setMessage("Register failed")
                                       .setNegativeButton("try again",null)
                                       .create()
                                       .show();
                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               };

               LoginRequest loginRequest = new LoginRequest(username,password,responseListener);
               RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
               queue.add(loginRequest);
           }
       });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }
    private void nextActivity(Profile profile){
        if(profile != null){
            Intent main = new Intent(MainActivity.this, UserAreaActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());
            startActivity(main);
        }
    }

    public static boolean isLoggedFacebook(){
        return loggingInFacebook;
    }
}
