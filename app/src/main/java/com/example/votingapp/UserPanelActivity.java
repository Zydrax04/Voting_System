package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class UserPanelActivity extends AppCompatActivity {
    private boolean voted = false;

    public void openTemplateActivity(Intent i) {
        startActivity(i);
    }

    public void openResultsActivity(Intent i){
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        //FireBase Reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myUsersRef = database.getReference("Users");
        final DatabaseReference myPollsRef = database.getReference("Polls");
        final ArrayList<String> template = new ArrayList<>();

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("FULL_NAME");
        final String email = intent.getStringExtra("USER_NAME");

        //page Buttons
        Button voteBtn = findViewById(R.id.votebtn);
        Button resultsBtn = findViewById(R.id.resultsBtn);
        TextView title = findViewById(R.id.titletextView2);
        title.setText("Welcome " + username);
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

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

        resultsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    resultsBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    resultsBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });
        
        //check if user voted already
        myUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        if(username.equals(email) && ds.child("voted").getValue(Long.class) == 1){
                            voted = true; //check if user has already voted
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

        //Take Templates
        myPollsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    template.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        if(username.equals("Template1")){
                            //users.add(new User(username));
                            template.add(username); //name
                            template.add(ds.child("questiontemp1").getValue(String.class)); //question
                            template.add(ds.child("yes").getValue(Long.class).toString()); //number of yes answers
                            template.add(ds.child("no").getValue(Long.class).toString()); //number of no answers
                        }
                        if(username.equals("Template2")){
                            template.add(username); //name
                            template.add(ds.child("questiontemp2").getValue(String.class)); //question
                            template.add(ds.child("option1").getValue(String.class)); //answer option1
                            template.add(ds.child("option2").getValue(String.class)); //answer option2
                            template.add(ds.child("option1votes").getValue(Long.class).toString()); //answer option1 votes
                            template.add(ds.child("option2votes").getValue(Long.class).toString()); //answer option2 votes
                        }
                        if(username.equals("Template3")){
                            template.add(username);
                            template.add(ds.child("questiontemp3").getValue(String.class));
                            template.add(ds.child("option1").getValue(String.class));
                            template.add(ds.child("option2").getValue(String.class));
                            template.add(ds.child("option3").getValue(String.class));
                            template.add(ds.child("option1votes").getValue(Long.class).toString());
                            template.add(ds.child("option2votes").getValue(Long.class).toString());
                            template.add(ds.child("option3votes").getValue(Long.class).toString());
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

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voted){
                    Toast.makeText(UserPanelActivity.this, "Already voted Sir!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(template.size() == 0){ //check if template exists
                    Toast.makeText(UserPanelActivity.this, "No Poll available yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                //template name
                String templateName = template.get(0);
                String templateQuestion = template.get(1);
                if(templateName.equals("Template1")) {
                    Intent i = new Intent(getBaseContext(), Template1.class);
                    i.putExtra("USER_NAME", email);
                    i.putExtra("questiontemp1", templateQuestion);
                    openResultsActivity(i);
                }
                if(templateName.equals("Template2")){
                    Intent i = new Intent(getBaseContext(), Template2.class);
                    i.putExtra("USER_NAME", email);
                    i.putExtra("questiontemp2", templateQuestion);
                    i.putExtra("option1", template.get(2));
                    i.putExtra("option2", template.get(3));
                    openTemplateActivity(i);
                }
                if(templateName.equals("Template3")){
                    Intent i = new Intent(getBaseContext(), Template3.class);
                    i.putExtra("USER_NAME", email);
                    i.putExtra("questiontemp3", templateQuestion);
                    i.putExtra("option1", template.get(2));
                    i.putExtra("option2", template.get(3));
                    i.putExtra("option3", template.get(4));
                    openTemplateActivity(i);
                }
            }
        });

        resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(template.size() == 0){ //check if template exists
                    Toast.makeText(UserPanelActivity.this, "No Poll available yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                //template name and question
                String templateName = template.get(0);
                String templateQuestion = template.get(1);

                if(templateName.equals("Template1")) {
                    String yesCount = template.get(2);
                    String noCount = template.get(3);
                    Intent i = new Intent(getBaseContext(), ResultsActivity.class);
                    i.putExtra("TEMPLATE_NAME", templateName);
                    i.putExtra("questiontemp", templateQuestion);
                    i.putExtra("yes", yesCount);
                    i.putExtra("no", noCount);
                    openTemplateActivity(i);
                }
                if(templateName.equals("Template2")){
                    Intent i = new Intent(getBaseContext(), ResultsActivity.class);
                    i.putExtra("TEMPLATE_NAME", templateName);
                    i.putExtra("questiontemp", templateQuestion);
                    i.putExtra("option1", template.get(2));
                    i.putExtra("option2", template.get(3));
                    i.putExtra("option1votes", template.get(4));
                    i.putExtra("option2votes", template.get(5));
                    openTemplateActivity(i);
                }
                if(templateName.equals("Template3")){
                    Intent i = new Intent(getBaseContext(), ResultsActivity.class);
                    i.putExtra("TEMPLATE_NAME", templateName);
                    i.putExtra("questiontemp", templateQuestion);
                    i.putExtra("option1", template.get(2));
                    i.putExtra("option2", template.get(3));
                    i.putExtra("option3", template.get(4));
                    i.putExtra("option1votes", template.get(5));
                    i.putExtra("option2votes", template.get(6));
                    i.putExtra("option3votes", template.get(7));
                    openTemplateActivity(i);
                }
            }
        });

    }

}