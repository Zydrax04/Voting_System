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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class UserPanelActivity extends AppCompatActivity {
    private boolean[] voted = {false, false, false};
    Calendar crtCalendar = Calendar.getInstance();
    Calendar pollCalendar;
    String email = "";
    final ArrayList<ArrayList<String>> templates = new ArrayList<>();
    Button a1stVoteBtn;
    Button a2ndVoteBtn;
    Button a3rdVoteBtn;
    Button a1stResultsBtn;
    Button a2ndResultsBtn;
    Button a3rdResultsBtn;

    public void openSelectedActivity(Intent i) {
        startActivity(i);
    }


    public void makeVisible(){
        if(templates.size() == 2){ //2 templates are available
            //set unavailable Buttons invisible
            a2ndResultsBtn.setVisibility(View.VISIBLE);
            a2ndVoteBtn.setVisibility(View.VISIBLE);
        }
        if(templates.size() == 3){
            //set unavailable Buttons invisible
            a2ndResultsBtn.setVisibility(View.VISIBLE);
            a2ndVoteBtn.setVisibility(View.VISIBLE);
            a3rdResultsBtn.setVisibility(View.VISIBLE);
            a3rdVoteBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        //FireBase Reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myUsersRef = database.getReference("Users");
        final DatabaseReference myPollsRef = database.getReference("Polls");

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("FULL_NAME");
        email = intent.getStringExtra("USER_NAME");

        //page Buttons
        a1stVoteBtn = findViewById(R.id.a1stvotebtn);
        a2ndVoteBtn = findViewById(R.id.a2ndvotebtn);
        a3rdVoteBtn = findViewById(R.id.a3rdvotebtn);
        a1stResultsBtn = findViewById(R.id.a1stresultsBtn);
        a2ndResultsBtn = findViewById(R.id.a2ndresultsBtn);
        a3rdResultsBtn = findViewById(R.id.a3rdresultsBtn);
        TextView title = findViewById(R.id.titletextView2);
        title.setText("Welcome " + username);
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        //set unavailable Buttons invisible
        a2ndResultsBtn.setVisibility(View.INVISIBLE);
        a2ndVoteBtn.setVisibility(View.INVISIBLE);
        a3rdResultsBtn.setVisibility(View.INVISIBLE);
        a3rdVoteBtn.setVisibility(View.INVISIBLE);

        a1stVoteBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    a1stVoteBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    a1stVoteBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });

        a1stResultsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    a1stResultsBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    a1stResultsBtn.startAnimation(scaleDown);
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
                        if(username.equals(email)){
                            if(ds.child("voted1").getValue(Long.class) == 1){
                                voted[0] = true; //check if user has already voted
                            }
                            if(ds.child("voted2").getValue(Long.class) == 1){
                                voted[1] = true; //check if user has already voted
                            }
                            if(ds.child("voted3").getValue(Long.class) == 1){
                                voted[2] = true; //check if user has already voted
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

        //Take Templates
        myPollsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                templates.clear(); //clear old templates so that they are up to date
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ArrayList<String> template = new ArrayList<>(); //create new instance of tmeplate each iteration
                        String username = ds.child("username").getValue(String.class);
                        if(username.equals("Template1")){
                            template.clear();
                            //users.add(new User(username));
                            template.add(username); //name
                            template.add(ds.child("questiontemp1").getValue(String.class)); //question
                            template.add(ds.child("yes").getValue(Long.class).toString()); //number of yes answers
                            template.add(ds.child("no").getValue(Long.class).toString()); //number of no answers
                            template.add(ds.child("year").getValue(Long.class).toString()); //year
                            template.add(ds.child("month").getValue(Long.class).toString()); //month
                            template.add(ds.child("day").getValue(Long.class).toString()); //day
                        }
                        if(username.equals("Template2")){
                            template.clear();
                            template.add(username); //name
                            template.add(ds.child("questiontemp2").getValue(String.class)); //question
                            template.add(ds.child("option1").getValue(String.class)); //answer option1
                            template.add(ds.child("option2").getValue(String.class)); //answer option2
                            template.add(ds.child("option1votes").getValue(Long.class).toString()); //answer option1 votes
                            template.add(ds.child("option2votes").getValue(Long.class).toString()); //answer option2 votes
                            template.add(ds.child("year").getValue(Long.class).toString()); //year
                            template.add(ds.child("month").getValue(Long.class).toString()); //month
                            template.add(ds.child("day").getValue(Long.class).toString()); //day
                        }
                        if(username.equals("Template3")){
                            template.clear();
                            template.add(username); //name
                            template.add(ds.child("questiontemp3").getValue(String.class));
                            template.add(ds.child("option1").getValue(String.class));
                            template.add(ds.child("option2").getValue(String.class));
                            template.add(ds.child("option3").getValue(String.class));
                            template.add(ds.child("option1votes").getValue(Long.class).toString());
                            template.add(ds.child("option2votes").getValue(Long.class).toString());
                            template.add(ds.child("option3votes").getValue(Long.class).toString());
                            template.add(ds.child("year").getValue(Long.class).toString()); //year
                            template.add(ds.child("month").getValue(Long.class).toString()); //month
                            template.add(ds.child("day").getValue(Long.class).toString()); //day
                        }
                        if(template.size() != 0){
                            templates.add(template); //Add existing template to available templates
                        }
                    }
                    makeVisible();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        a1stVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedPoll(0);
            }
        });

        a2ndVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSelectedPoll(1);
            }
        });

        a3rdVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedPoll(2);
            }
        });

        a1stResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedResults(0);
            }
        });

        a2ndResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedResults(1);
            }
        });

        a3rdResultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedResults(2);
            }
        });

    }

    public void openSelectedPoll(int number){

        ArrayList<String> template = new ArrayList<>();

        for(String data : templates.get(number)) //3rd template
            template.add(data);
        //template name
        String templateName = template.get(0);
        String templateQuestion = template.get(1);

        if((templateName.equals("Template1") && voted[0]) || (templateName.equals("Template2") && voted[1]) || (templateName.equals("Template3") && voted[2])){
            Toast.makeText(UserPanelActivity.this, "Already voted Sir!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(templateName.equals("Template1")) {
            int year = Integer.parseInt(template.get(4));
            int month = Integer.parseInt(template.get(5));
            int day = Integer.parseInt(template.get(6));
            boolean isOnTime = checkCalendar(year, month, day);
            if(!isOnTime)
                return;
            Intent i = new Intent(getBaseContext(), Template1.class);
            i.putExtra("USER_NAME", email);
            i.putExtra("questiontemp1", templateQuestion);
            openSelectedActivity(i);
        }
        if(templateName.equals("Template2")){
            int year = Integer.parseInt(template.get(6));
            int month = Integer.parseInt(template.get(7));
            int day = Integer.parseInt(template.get(8));
            boolean isOnTime = checkCalendar(year, month, day);
            if(!isOnTime)
                return;
            Intent i = new Intent(getBaseContext(), Template2.class);
            i.putExtra("USER_NAME", email);
            i.putExtra("questiontemp2", templateQuestion);
            i.putExtra("option1", template.get(2));
            i.putExtra("option2", template.get(3));
            openSelectedActivity(i);
        }
        if(templateName.equals("Template3")){
            int year = Integer.parseInt(template.get(8));
            int month = Integer.parseInt(template.get(9));
            int day = Integer.parseInt(template.get(10));
            boolean isOnTime = checkCalendar(year, month, day);
            if(!isOnTime)
                return;
            Intent i = new Intent(getBaseContext(), Template3.class);
            i.putExtra("USER_NAME", email);
            i.putExtra("questiontemp3", templateQuestion);
            i.putExtra("option1", template.get(2));
            i.putExtra("option2", template.get(3));
            i.putExtra("option3", template.get(4));
            openSelectedActivity(i);
        }
    }

    public void openSelectedResults(int number){
        ArrayList<String> template = new ArrayList<>();
        if(templates.size() == 0){ //check if template exists
            Toast.makeText(UserPanelActivity.this, "No Poll available yet", Toast.LENGTH_SHORT).show();
            return;
        }
        template.clear();
        for(String data : templates.get(number)) //3rd template
            template.add(data);
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
            openSelectedActivity(i);
        }
        if(templateName.equals("Template2")){
            Intent i = new Intent(getBaseContext(), ResultsActivity.class);
            i.putExtra("TEMPLATE_NAME", templateName);
            i.putExtra("questiontemp", templateQuestion);
            i.putExtra("option1", template.get(2));
            i.putExtra("option2", template.get(3));
            i.putExtra("option1votes", template.get(4));
            i.putExtra("option2votes", template.get(5));
            openSelectedActivity(i);
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
            openSelectedActivity(i);
        }
    }

    public boolean checkCalendar(int year, int month, int day){
        Calendar pollCalendar = Calendar.getInstance();
        pollCalendar.set(Calendar.YEAR, year);
        pollCalendar.set(Calendar.MONTH, month);
        pollCalendar.set(Calendar.DAY_OF_MONTH, day);
        if(crtCalendar.compareTo(pollCalendar) > 0){ //means crtCalendar is past due date
            Toast.makeText(UserPanelActivity.this, "Deadline passed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}