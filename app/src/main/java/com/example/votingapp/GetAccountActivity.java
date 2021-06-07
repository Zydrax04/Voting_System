package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private Button recoveryBtn;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    private String encodedMsg = "";
    private String resultMsg = "";
    private TextView msgTextView;
    private String deviceId;
    final String secretKey = "1l0v3crypt0graphy!";

    public void openAdminPanelActivity(){
        Intent intent = new Intent(this, AdminPanelActivity.class);
        startActivity(intent);
    }
    public void openRecoverAccountActivity(){
        Intent intent = new Intent(this, RecoverAccountActivity.class);
        startActivity(intent);
    }

    public void openUserPanelActivity(Intent i) {
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        //Decrypt the taken Code
        AES Aes = new AES();
        //DES Des = new DES();
        //String plainText = Des.decrypt(codedMsg);
        String decryptedString = AES.decrypt(result, secretKey);

        return decryptedString;
    }

    public void completeLoginFields(){
        EditText userField = findViewById(R.id.userText);
        EditText passwordField = findViewById(R.id.passwordText);
        if(encodedMsg == null) { //AES returns null if it can not decrypt the resulted message thus there is no message
            setTextView("No Account found in image!");
            return;
        }
        if(encodedMsg.length() == 0) //preventive measure in case AES can decrypt the random string
            return;
        String[] credidentials = encodedMsg.split(":");
        String email = credidentials[0];
        if(credidentials.length == 3 && credidentials[2].equals(deviceId)){ //id in photo matches phone id
            userField.setText(credidentials[0]);
            passwordField.setText(credidentials[1]);
            return;
        }
        for(User crtUser : users){ //check if user has a 2nd device authorized to log in
            if(email.equals(crtUser.getUsername()) && deviceId.equals(crtUser.getDeviceId())){
                userField.setText(credidentials[0]);
                passwordField.setText(credidentials[1]);
                return;
            }
        }
        userField.setText("");
        passwordField.setText("");
        Toast.makeText(GetAccountActivity.this, "Detected Login from another Device! \n Please contact Administrator!", Toast.LENGTH_SHORT).show();
        /*if(credidentials.length == 3 && credidentials[2].equals(deviceId)) {
            userField.setText(credidentials[0]);
            passwordField.setText(credidentials[1]);
        }else {
            userField.setText("");
            passwordField.setText("");
        }
        if(credidentials.length == 3 && !credidentials[2].equals(deviceId))
            Toast.makeText(GetAccountActivity.this, "Detected Login from another Device! \n Please contact Administrator!", Toast.LENGTH_SHORT).show();
        */
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
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Button uploadBtn = findViewById(R.id.uploadBtn);
        Button loginBtn = findViewById(R.id.login);
        AccountImage = (ImageView) findViewById(R.id.uploadedPhoto);
        msgTextView = findViewById(R.id.MsgtextView);
        recoveryBtn = findViewById(R.id.recoveryBtn);
        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        uploadBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    uploadBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    uploadBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });

        loginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    loginBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    loginBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });
        recoveryBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_DOWN){
                    recoveryBtn.startAnimation(scaleUp);
                }
                else if(event.getAction()==event.ACTION_UP){
                    recoveryBtn.startAnimation(scaleDown);
                }
                return false;
            }
        });


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

        //Click Listener for recoverBtn
        recoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecoverAccountActivity();
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
                        if(!username.matches("Template.")) {
                            String firstName = ds.child("firstName").getValue(String.class);
                            String lastName = ds.child("lastName").getValue(String.class);
                            String ID = ds.child("recoveryID").getValue(String.class);
                            User user = new User(username, password, firstName, lastName, ID);
                            users.add(user);
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

        //OnClickListener for loginBtn -> Template
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView userText = findViewById(R.id.userText);
                TextView passwordText = findViewById(R.id.passwordText);
                String username = userText.getText().toString().trim();
                String password = passwordText.getText().toString();
                String fullName = "user";
                //Make Password Hash
                MD5 md5 = new MD5(password);
                String hashedPassword = md5.getMD5();

                boolean found = false;
                for (User user1 : users) {
                    if (user1.getUsername().equals(username) && user1.getPassword().equals(hashedPassword)) {
                        fullName = user1.getFirstName() + " " + user1.getLastName();
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
                        i.putExtra("FULL_NAME", fullName);
                        openUserPanelActivity(i);
                    }
                } else
                    Toast.makeText(GetAccountActivity.this, "Wrong login credentials", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Method to set image to ImageView of chosen Photo from the Gallert
    @RequiresApi(api = Build.VERSION_CODES.O)
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