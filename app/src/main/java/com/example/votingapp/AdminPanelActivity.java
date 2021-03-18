package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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