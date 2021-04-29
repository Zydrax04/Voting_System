package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



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
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        loginImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    loginImg.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    loginImg.startAnimation(scaleDown);
                }
                return false;
            }
        });

        registerImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    registerImg.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    registerImg.startAnimation(scaleDown);
                }
                return false;
            }
        });

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