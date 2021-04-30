package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class AdminPanelActivity extends AppCompatActivity {

    public void openChooseTemplateActivity(){
        Intent intent = new Intent(this, ChooseTemplateActivity.class);
        startActivity(intent);
    }
    public void openChangePasswordActivity(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Button makePollBtn = findViewById(R.id.makePollBtn);
        Button passwdBtn = findViewById(R.id.passwdBtn);
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        makePollBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    makePollBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    makePollBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });

        passwdBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    passwdBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    passwdBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });


        //Change Password Click Listener
        passwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordActivity();
            }
        });
        //makePollBtn Click Listener
        makePollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseTemplateActivity();
            }
        });

    }
}