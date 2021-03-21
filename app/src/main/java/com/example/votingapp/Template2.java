package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Template2 extends AppCompatActivity {
    private boolean exists = false;
    //FireBase Reference
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("Users");
    final String userId = myRef.push().getKey();
    Button voteBtn;
    TextView question;
    TextView option1;
    TextView option2;
    RadioButton radioButtonOpt1;
    RadioButton radioButtonOpt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template2);

        //Get user name
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("USER_NAME");
        final String questiontemp2 = intent.getStringExtra("questiontemp2");
        final String option1temp2 = intent.getStringExtra("option1");
        final String option2temp2 = intent.getStringExtra("option2");

        //Page Buttons
        voteBtn = findViewById(R.id.votebtn2);
        question = findViewById(R.id.questiontemp2);
        option1 = findViewById(R.id.option1temp2);
        option2 = findViewById(R.id.option2temp2);
        radioButtonOpt1 = findViewById(R.id.radioButtonOpt1);
        radioButtonOpt2 = findViewById(R.id.radioButtonOpt2);

        if(username != null && !username.equals("Admin")) {
            question.setText(questiontemp2);
            question.setEnabled(false);
            option1.setText(option1temp2);
            option1.setEnabled(false);
            option2.setText(option2temp2);
            option2.setEnabled(false);
        }

        //OnClickListener for accountBtn -> GetAccount
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.equals("Admin")){
                    userAction(username);
                    return;
                }
                else{
                    adminAction();
                }
            }
        });
    }

    public void adminAction(){
        //take question
        String questiontemp2 = question.getText().toString();
        String option1temp2 = option1.getText().toString();
        String option2temp2 = option2.getText().toString();

        //search if template already exists
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);

                        if (username.equals("Template2")) {
                            ds.child("questiontemp2").getRef().setValue(questiontemp2);
                            ds.child("option1").getRef().setValue(option1temp2);
                            ds.child("option2").getRef().setValue(option2temp2);
                            ds.child("option1votes").getRef().setValue(0);
                            ds.child("option2votes").getRef().setValue(0);
                            exists = true;
                            Toast.makeText(Template2.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //else create Template
                    if(!exists) {
                        myRef.child(userId).child("username").setValue("Template2");
                        myRef.child(userId).child("questiontemp2").setValue(questiontemp2);
                        myRef.child(userId).child("option1").setValue(option1temp2);
                        myRef.child(userId).child("option2").setValue(option2temp2);
                        myRef.child(userId).child("option1votes").setValue(0);
                        myRef.child(userId).child("option2votes").setValue(0);
                        Toast.makeText(Template2.this, "Template created successfully", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void userAction(String crtUsername){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String username = ds.child("username").getValue(String.class);
                        //update Template if exists
                        if(username.equals("Template2")){
                            //to do
                            if(radioButtonOpt1.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("option1votes").getValue().toString());
                                ds.child("option1votes").getRef().setValue(crtValue+1);
                            }
                            else if(radioButtonOpt2.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("option2votes").getValue().toString());
                                ds.child("option2votes").getRef().setValue(crtValue+1);
                            }
                            else{
                                Toast.makeText(Template2.this, "Please choose an option", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        if (username.equals(crtUsername)) {
                            ds.child("voted").getRef().setValue(1);
                            //Toast.makeText(Template1.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    finish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}