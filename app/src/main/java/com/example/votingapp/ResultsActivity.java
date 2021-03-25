package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private TextView question;
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        question = findViewById(R.id.question_res);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);

        //Get template
        final Intent intent = getIntent();
        final String templateName = intent.getStringExtra("TEMPLATE_NAME");
        final String questiontemp = intent.getStringExtra("questiontemp");
        final String answer1format;
        final String answer2format;
        final String answer3format;
        question.setText(questiontemp);
        if(templateName.equals("Template1")){
            answer1format = intent.getStringExtra("yes");
            answer2format = intent.getStringExtra("no");
            answer1.setText("yes: " + answer1format);
            answer2.setText("no: " + answer2format);
            answer3.setVisibility(View.INVISIBLE);
        }
        else if(templateName.equals("Template2")){
            answer1format = intent.getStringExtra("option1") + ": " + intent.getStringExtra("option1votes");
            answer2format = intent.getStringExtra("option2") + ": " + intent.getStringExtra("option2votes");
            answer1.setText(answer1format);
            answer2.setText(answer2format);
            answer3.setVisibility(View.INVISIBLE);
        }
        else if(templateName.equals("Template3")){
            answer1format = intent.getStringExtra("option1") + ": " + intent.getStringExtra("option1votes");
            answer2format = intent.getStringExtra("option2") + ": " + intent.getStringExtra("option2votes");
            answer3format = intent.getStringExtra("option3") + ": " + intent.getStringExtra("option3votes");
            answer1.setText(answer1format);
            answer2.setText(answer2format);
            answer3.setText(answer3format);
        }
    }
}