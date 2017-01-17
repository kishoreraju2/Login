package com.example.kishore.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class UserAreaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUserName = (EditText) findViewById(R.id.editText);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final TextView welcomeMessage = (TextView) findViewById(R.id.textView);


        boolean yes = MainActivity.isLoggedFacebook();
        if(yes == false) {
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            int age = intent.getIntExtra("age", -1);

            String message = name + " Welcome to the user area";
            welcomeMessage.setText(message);
            etUserName.setText(username);
            etAge.setText(age + "");
        }
        else{

            Bundle inBundle = getIntent().getExtras();
            String name = inBundle.get("name").toString();
            String surname = inBundle.get("surname").toString();
            String imageUrl = inBundle.get("imageUrl").toString();

            new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);
            String message = name + " Welcome to the user area";
            welcomeMessage.setText(message);
            etUserName.setText(name+" "+surname);

        }



    }


}
