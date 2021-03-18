package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Template3 extends AppCompatActivity {

    private boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template3);

        //FireBase Reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        final String userId = myRef.push().getKey();

        //Get user name
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("USER_NAME");
        final String questiontemp3 = intent.getStringExtra("questiontemp3");
        final String option1temp3 = intent.getStringExtra("option1");
        final String option2temp3 = intent.getStringExtra("option2");
        final String option3temp3 = intent.getStringExtra("option3");

        //Page Buttons
        Button voteBtn = findViewById(R.id.votebtn3);
        TextView question = findViewById(R.id.questiontemp3);
        TextView option1 = findViewById(R.id.option1temp3);
        TextView option2 = findViewById(R.id.option2temp3);
        TextView option3 = findViewById(R.id.option3temp3);

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
                if(!username.equals("Admin")){
                    Toast.makeText(Template3.this, "Clicked!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //take question
                String questiontemp3 = question.getText().toString();
                String option1temp3 = option1.getText().toString();
                String option2temp3 = option2.getText().toString();
                String option3temp3 = option3.getText().toString();

                //search if template already exists
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    exists = true;
                                    Toast.makeText(Template3.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //else create Template
                            if(!exists) {
                                myRef.child(userId).child("username").setValue("Template3");
                                myRef.child(userId).child("questiontemp3").setValue(questiontemp3);
                                myRef.child(userId).child("option1").setValue(option1temp3);
                                myRef.child(userId).child("option2").setValue(option2temp3);
                                myRef.child(userId).child("option3").setValue(option3temp3);
                                Toast.makeText(Template3.this, "Template created successfully", Toast.LENGTH_SHORT).show();
                                finish();
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
        });

    }
}