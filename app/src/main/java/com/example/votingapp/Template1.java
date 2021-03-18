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

import java.util.ArrayList;

public class Template1 extends AppCompatActivity {
    private boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template1);

        //FireBase Reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        final String userId = myRef.push().getKey();
        final ArrayList<User> users = new ArrayList<>();

        //Get user name
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("USER_NAME");
        final String questiontemp1 = intent.getStringExtra("questiontemp1");

        //Page Buttons
        Button voteBtn = findViewById(R.id.votebtn1);
        TextView question = findViewById(R.id.questiontemp1);
        if(username != null && !username.equals("Admin")) {
            question.setText(questiontemp1);
            question.setEnabled(false);
        }
        //OnClickListener for accountBtn -> GetAccount
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.equals("Admin")){
                    Toast.makeText(Template1.this, "Clicked", Toast.LENGTH_SHORT).show();
                    return;
                }
                //take question
                String questiontemp1 = question.getText().toString();

                //search if template already exists
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String username = ds.child("username").getValue(String.class);

                                if (username.equals("Template1")) {
                                    ds.child("questiontemp1").getRef().setValue(questiontemp1);
                                    exists = true;
                                    Toast.makeText(Template1.this, "Template updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //else create Template
                            if(!exists) {
                                myRef.child(userId).child("username").setValue("Template1");
                                myRef.child(userId).child("questiontemp1").setValue(questiontemp1);
                                Toast.makeText(Template1.this, "Template created successfully", Toast.LENGTH_SHORT).show();
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