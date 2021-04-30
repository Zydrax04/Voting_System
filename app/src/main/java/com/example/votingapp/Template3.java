package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Template3 extends AppCompatActivity {

    private boolean exists = false;
    //FireBase Reference
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myPollsRef = database.getReference("Polls");
    final DatabaseReference myUsersRef = database.getReference("Users");
    final String userId = myPollsRef.push().getKey();

    Button voteBtn;
    TextView question;
    TextView option1;
    TextView option2;
    TextView option3;
    RadioButton radioButtonOpt1;
    RadioButton radioButtonOpt2;
    RadioButton radioButtonOpt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template3);

        //Get user name
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("USER_NAME");
        final String questiontemp3 = intent.getStringExtra("questiontemp3");
        final String option1temp3 = intent.getStringExtra("option1");
        final String option2temp3 = intent.getStringExtra("option2");
        final String option3temp3 = intent.getStringExtra("option3");

        //Page Buttons
        voteBtn = findViewById(R.id.votebtn3);
        question = findViewById(R.id.questiontemp3);
        option1 = findViewById(R.id.option1temp3);
        option2 = findViewById(R.id.option2temp3);
        option3 = findViewById(R.id.option3temp3);
        radioButtonOpt1 = findViewById(R.id.radioBtnT3Opt1);
        radioButtonOpt2 = findViewById(R.id.radioBtnT3Opt2);
        radioButtonOpt3 = findViewById(R.id.radioBtnT3Opt3);
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        //VoteBtn animation
        voteBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    voteBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    voteBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });

        if(username != null && !username.equals("Admin")) {
            question.setText(questiontemp3);
            question.setEnabled(false);
            option1.setText(option1temp3);
            option1.setEnabled(false);
            option2.setText(option2temp3);
            option2.setEnabled(false);
            option3.setText(option3temp3);
            option3.setEnabled(false);
        }

        //OnClickListener for accountBtn -> GetAccount
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("Admin")){
                    adminAction();
                    return;
                }
                else if(!radioButtonOpt1.isChecked() && !radioButtonOpt2.isChecked() && !radioButtonOpt3.isChecked()){
                    Toast.makeText(Template3.this, "Please choose an option", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    userAction(username);
                    return;
                }
            }
        });

    }

    public void adminAction(){
        //take question
        String questiontemp3 = question.getText().toString();
        String option1temp3 = option1.getText().toString();
        String option2temp3 = option2.getText().toString();
        String option3temp3 = option3.getText().toString();

        //search if template already exists
        myPollsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);

                        if (username.equals("Template3")) {
                            ds.child("questiontemp3").getRef().setValue(questiontemp3);
                            ds.child("option1").getRef().setValue(option1temp3);
                            ds.child("option2").getRef().setValue(option2temp3);
                            ds.child("option3").getRef().setValue(option3temp3);
                            ds.child("option1votes").getRef().setValue(0);
                            ds.child("option2votes").getRef().setValue(0);
                            ds.child("option3votes").getRef().setValue(0);
                            exists = true;
                            Toast.makeText(Template3.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        //else if(username.matches("Template.")){
                        //    ds.getRef().removeValue();
                        //}
                    }
                    //else create Template
                    if(!exists) {
                        myPollsRef.child(userId).child("username").setValue("Template3");
                        myPollsRef.child(userId).child("questiontemp3").setValue(questiontemp3);
                        myPollsRef.child(userId).child("option1").setValue(option1temp3);
                        myPollsRef.child(userId).child("option2").setValue(option2temp3);
                        myPollsRef.child(userId).child("option3").setValue(option3temp3);
                        myPollsRef.child(userId).child("option1votes").setValue(0);
                        myPollsRef.child(userId).child("option2votes").setValue(0);
                        myPollsRef.child(userId).child("option3votes").setValue(0);
                        Toast.makeText(Template3.this, "Template created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //renew voted to 0 for all users
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.child("voted3").getRef().setValue(0);
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
        myPollsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String username = ds.child("username").getValue(String.class);
                        //update Template if exists
                        if(username.equals("Template3")){
                            //to do
                            if(radioButtonOpt1.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("option1votes").getValue().toString());
                                ds.child("option1votes").getRef().setValue(crtValue+1);
                            }
                            else if(radioButtonOpt2.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("option2votes").getValue().toString());
                                ds.child("option2votes").getRef().setValue(crtValue+1);
                            }
                            else if(radioButtonOpt3.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("option3votes").getValue().toString());
                                ds.child("option3votes").getRef().setValue(crtValue+1);
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //set voted to 1 for user
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        if (username.equals(crtUsername)) {
                            ds.child("voted3").getRef().setValue(1);
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