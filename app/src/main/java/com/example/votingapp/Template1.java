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

public class Template1 extends AppCompatActivity {
    private boolean exists = false;
    //FireBase Reference
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myPollsRef = database.getReference("Polls");
    final DatabaseReference myUsersRef = database.getReference("Users");
    final String userId = myPollsRef.push().getKey();

    //EditText
    TextView question;
    Button voteBtn;
    RadioButton yesRadioButton;
    RadioButton noRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template1);

        //Get user name
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("USER_NAME");
        final String questiontemp1 = intent.getStringExtra("questiontemp1");

        //Page Buttons
        voteBtn = findViewById(R.id.votebtn1);
        question = findViewById(R.id.questiontemp1);
        yesRadioButton = findViewById(R.id.yes);
        noRadioButton = findViewById(R.id.no);

        if(username != null && !username.equals("Admin")) {
            question.setText(questiontemp1);
            question.setEnabled(false);
        }
        //OnClickListener for accountBtn -> GetAccount
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.equals("Admin")){
                    userAction(username);
                    return;
                }
                else {
                    adminAction();
                }
            }
        });
    }

    public void adminAction(){
        //take question
        String questiontemp1 = question.getText().toString();

        //search if template already exists
        myPollsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        //update Template if exists
                        if (username.equals("Template1")) {
                            ds.child("questiontemp1").getRef().setValue(questiontemp1);
                            ds.child("yes").getRef().setValue(0);
                            ds.child("no").getRef().setValue(0);
                            exists = true;
                            Toast.makeText(Template1.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if(username.matches("Template.")){
                            ds.getRef().removeValue();
                        }
                    }
                    //else create Template
                    if(!exists) {
                        myPollsRef.child(userId).child("username").setValue("Template1");
                        myPollsRef.child(userId).child("questiontemp1").setValue(questiontemp1);
                        myPollsRef.child(userId).child("yes").setValue(0);
                        myPollsRef.child(userId).child("no").setValue(0);
                        Toast.makeText(Template1.this, "Template created successfully", Toast.LENGTH_SHORT).show();
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
                        ds.child("voted").getRef().setValue(0);
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
                        if(username.equals("Template1")){
                            //to do
                            if(yesRadioButton.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("yes").getValue().toString());
                                ds.child("yes").getRef().setValue(crtValue+1);
                            }
                            else if(noRadioButton.isChecked()){
                                int crtValue = Integer.parseInt(ds.child("no").getValue().toString());
                                ds.child("no").getRef().setValue(crtValue+1);
                            }
                            else{
                                Toast.makeText(Template1.this, "Please choose an option", Toast.LENGTH_SHORT).show();
                                return;
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
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
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