package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StartPageActivity extends AppCompatActivity {


    public void openGetAccountActivity(){
        Intent intent = new Intent(this, GetAccountActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        //Page Buttons
        ImageView loginImg = findViewById(R.id.loginImg);
        ImageView registerImg = findViewById(R.id.registerImg);

        //OnClickListener for accountBtn -> GetAccount
        loginImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGetAccountActivity();
            }
        });

        //OnClickListener for accountBtn -> GetAccount
        registerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });


    }
}