package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RecoverAccountActivity extends AppCompatActivity {
    //Buttons and Textviews
    private TextView emailTextView;
    private TextView verificationCodeTextView;
    private Button sendEmailBtn;
    private Button verifyCodeBtn;
    private int generatedCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_account);

        //Get Buttons and TextViews
        emailTextView = findViewById(R.id.emailForRecover);
        verificationCodeTextView = findViewById(R.id.verificationCode);
        sendEmailBtn = findViewById(R.id.sendRecoverEmail);
        verifyCodeBtn = findViewById(R.id.verifyCodeBtn);

        //Click Listener for recoverBtn
        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateVerficationCode();
                sendMail();
            }
        });
        //Click Listener for code validation
        verifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateGivenCode();
            }
        });
    }

    private void generateVerficationCode(){
        for(int i = 0; i < 5; i++) {
            int min = 0;
            int max = 9;
            Random r = new Random();
            int i1 = r.nextInt(max - min + 1) + min;
            this.generatedCode = this.generatedCode * 10 + i1;
        }
    }

    private void sendMail() {

        String mail = emailTextView.getText().toString().trim();
        String subject = "Voting App Recover Account";
        String message = "Your verification code is " + this.generatedCode;


        //CloudStorage cloudStorage = new CloudStorage(RegisterActivity.this,mail, subject, message);
        //cloudStorage.getBitmap();
        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message, true);

        javaMailAPI.execute();

    }

    private void validateGivenCode(){
        String givenCodeText = verificationCodeTextView.getText().toString();
        int givenCode = Integer.parseInt(givenCodeText);
        if(givenCode == this.generatedCode)
            Toast.makeText(RecoverAccountActivity.this, "Code validated!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(RecoverAccountActivity.this, "Wrong Code!", Toast.LENGTH_SHORT).show();
    }
}