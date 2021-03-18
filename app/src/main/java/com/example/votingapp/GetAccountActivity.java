package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class GetAccountActivity extends AppCompatActivity {

    private final ArrayList<User> users = new ArrayList<>();
    private ImageView AccountImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    private String encodedMsg = "";
    private String resultMsg = "";
    private TextView msgTextView;

    public void openAdminPanelActivity(){
        Intent intent = new Intent(this, AdminPanelActivity.class);
        startActivity(intent);
    }

    public void openUserPanelActivity(Intent i) {
        startActivity(i);
    }

    String getEncodedMessage(Bitmap bm){
        //take code out of image -> out of stegnography
        DecodeStegnography stegnoDecoder = new DecodeStegnography(bm);
        boolean foundMsg = stegnoDecoder.getDecoded();
        if(!foundMsg){
            resultMsg = "No Account found in image!";
            return "No Account found in image!";
        }
        resultMsg = "Found Account in image!";
        String codedMsg = stegnoDecoder.getEncodedMsg();
        //decode the taken code
        DecodeBinary decoder = new DecodeBinary(codedMsg);
        String result = decoder.decodeMessage();
        return result;
    }

    public void completeLoginFields(){
        EditText userField = findViewById(R.id.userText);
        EditText passwordField = findViewById(R.id.passwordText);
        String[] credidentials = encodedMsg.split(":");
        if(credidentials.length == 2) {
            userField.setText(credidentials[0]);
            passwordField.setText(credidentials[1]);
        }else {
            userField.setText("");
            passwordField.setText("");
        }
    }

    public void setTextView(String result){
        msgTextView.setText(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_account);

        //FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        final String userId = myRef.push().getKey();

        //Layout Items
        Button uploadBtn = findViewById(R.id.uploadBtn);
        Button loginBtn = findViewById(R.id.login);
        AccountImage = (ImageView) findViewById(R.id.uploadedPhoto);
        msgTextView = findViewById(R.id.MsgtextView);

        //Click Listener for upload Button to open Gallery
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Sellect Picture"), PICK_IMAGE);

            }
        });

        //Take all users
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String username = ds.child("username").getValue(String.class);
                        String password = ds.child("password").getValue(String.class);
                        User user = new User(username, password);
                        users.add(user);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //OnClickListener for loginBtn -> Template
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView userText = findViewById(R.id.userText);
                TextView passwordText = findViewById(R.id.passwordText);
                String username = userText.getText().toString();
                String password = passwordText.getText().toString();
                //Make Password Hash
                MD5 md5 = new MD5(password);
                String hashedPassword = md5.getMD5();

                boolean found = false;
                String username2 = "";
                for (User user1 : users) {
                    if (user1.getUsername().equals(username) && user1.getPassword().equals(hashedPassword)) {
                        username2 = user1.getUsername();
                        found = true;
                        break;
                    }
                }
                if (found) {
                    //Toast.makeText(GetAccountActivity.this, "User Found!", Toast.LENGTH_SHORT).show();
                    if(username.equals("Admin"))
                        openAdminPanelActivity();
                    else{
                        Intent i = new Intent(getBaseContext(), UserPanelActivity.class);
                        i.putExtra("USER_NAME", username);
                        openUserPanelActivity(i);
                    }
                } else
                    Toast.makeText(GetAccountActivity.this, "Wrong login credentials", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Method to set image to ImageView of chosen Photo from the Gallert
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                AccountImage.setImageBitmap(bitmap);
                encodedMsg = getEncodedMessage(bitmap);
                setTextView(resultMsg);
                completeLoginFields();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}