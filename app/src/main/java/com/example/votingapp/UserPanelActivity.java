package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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
        final DatabaseReference myRef = database.getReference("Users");
        final ArrayList<String> template = new ArrayList<>();

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("FULL_NAME");

        Button voteBtn = findViewById(R.id.votebtn);
        Button resultsBtn = findViewById(R.id.resultsBtn);
        TextView title = findViewById(R.id.titletextView2);
        title.setText("Welcome " + username);
        
        //Take Templates
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        template.clear();
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
                //template name
                String templateName = template.get(0);
                String templateQuestion = template.get(1);
                if(templateName.equals("Template1")) {
                    Intent i = new Intent(getBaseContext(), Template1.class);
                    i.putExtra("USER_NAME", username);
                    i.putExtra("questiontemp1", templateQuestion);
                    openResultsActivity(i);
                }
                if(templateName.equals("Template2")){
                    Intent i = new Intent(getBaseContext(), Template2.class);
                    i.putExtra("USER_NAME", username);
                    i.putExtra("questiontemp2", templateQuestion);
                    i.putExtra("option1", template.get(2));
                    i.putExtra("option2", template.get(3));
                    openTemplateActivity(i);
                }
                if(templateName.equals("Template3")){
                    Intent i = new Intent(getBaseContext(), Template3.class);
                    i.putExtra("USER_NAME", username);
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
                //template name
                String templateName = template.get(0);
                String templateQuestion = template.get(1);
                String yesCount = template.get(2);
                String noCount = template.get(3);

                if(templateName.equals("Template1")) {
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