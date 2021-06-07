package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RecoverAccountActivity extends AppCompatActivity {
    //Buttons and Textviews
    private TextView emailTextView;
    private TextView verificationCodeTextView;
    private Button sendEmailBtn;
    private Button verifyCodeBtn;
    private int generatedCode;
    private String deviceID;
    //firebase reference
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myUsersRef = database.getReference("Users");
    private final String userId = myUsersRef.push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_account);

        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
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
        this.generatedCode = 0;
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
        if(givenCode == this.generatedCode){
            Toast.makeText(RecoverAccountActivity.this, "Code validated!", Toast.LENGTH_SHORT).show();
            addSecondAuthorizedID();
            Toast.makeText(RecoverAccountActivity.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(RecoverAccountActivity.this, "Wrong Code!", Toast.LENGTH_SHORT).show();
    }

    public void addSecondAuthorizedID(){
        String email = emailTextView.getText().toString().trim();
        //Take all users
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        if(username.equals(email)) {
                            ds.child("recoveryID").getRef().setValue(deviceID);
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
    }
}