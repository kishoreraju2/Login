package com.example.kishore.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText etUserName = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button login = (Button) findViewById(R.id.etLogin);
        final Button register = (Button) findViewById(R.id.etRegisterHere);
       final ProgressBar progress = (ProgressBar) findViewById(R.id.loadingProgress);

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
}
