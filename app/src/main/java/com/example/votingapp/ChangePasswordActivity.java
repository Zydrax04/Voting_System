package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {

    public void openAdminPanelActivity(){
        Intent intent = new Intent(this, AdminPanelActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Firebase reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        final String userId = myRef.push().getKey();

        //Username
        final String username = "Admin";

        final EditText oldPassword = findViewById(R.id.oldPassword);
        final EditText newPassword = findViewById(R.id.newPassword);
        final EditText newPassword2 = findViewById(R.id.newPassword2);

        Button changeBtn = findViewById(R.id.changeBtn);

        //clicklistener
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //old password
                String oldPasswd = oldPassword.getText().toString();
                //new password
                final String newPasswd = newPassword.getText().toString();
                //confirm new password
                String newPasswd2 = newPassword2.getText().toString();
                final String[] crtPasswd = new String[1];
                //get old password Hash
                MD5 md5Hasher = new MD5(oldPasswd);
                final String oldPasswdHash = md5Hasher.getMD5();

                if (newPasswd.equals(newPasswd2)) {

                    boolean checkPass1 = newPasswd.matches("[a-zA-Z0-9]{6,20}");
                    boolean checkPass2 = newPasswd2.matches("[a-zA-Z0-9]{6,20}");
                    if (!(checkPass1 && checkPass2)) {
                        Toast.makeText(ChangePasswordActivity.this, "Passwords should have between 6 and 20 alfanumeric chars!", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
                else {
                    Toast.makeText(ChangePasswordActivity.this, "Passwords are not the same!", Toast.LENGTH_SHORT).show();
                    return;
                }

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String crtUsername = ds.child("username").getValue(String.class);

                                if (username.equals(crtUsername)) {
                                    crtPasswd[0] = ds.child("password").getValue(String.class);
                                    if(oldPasswdHash.equals(crtPasswd[0])){
                                        MD5 md5Hasher = new MD5(newPasswd);
                                        String newPasswdHash = md5Hasher.getMD5();
                                        ds.child("password").getRef().setValue(newPasswdHash);
                                        openAdminPanelActivity();
                                    }
                                    else{
                                        Toast.makeText(ChangePasswordActivity.this, "Old Password does not match", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
}