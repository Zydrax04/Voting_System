package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    public EditText mEmail;
    public EditText mSubject;
    public EditText mMessage;
    public EditText firstNameTextView;
    public EditText lastNameTextView;
    private String alphabt = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final ArrayList<User> users = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");
    private final String userId = myRef.push().getKey();
    private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        ImageView getStarted = findViewById(R.id.getStarted);
        mEmail = (EditText)findViewById(R.id.emailAddr);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        Button registerBtn = findViewById(R.id.registerBtn);
        //make email field and register Button invisible
        registerBtn.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);
        firstNameTextView.setVisibility(View.INVISIBLE);
        lastNameTextView.setVisibility(View.INVISIBLE);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child("cat.jpg");
                imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //to do
                        Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        getStarted.setImageBitmap(mBitmap);
                        try{
                            File path = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES);
                            OutputStream fOut = null;
                            Integer counter = 0;

                            File file = new File(path, "cat.png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                            path.mkdirs();
                            fOut = new FileOutputStream(file);

                            //Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a PNG with 85% compression rate
                            fOut.flush(); // Not really required
                            fOut.close(); // do not forget to close the stream

                            MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                            getStarted.setEnabled(false);
                            //make email field and register Button visible
                            registerBtn.setVisibility(View.VISIBLE);
                            mEmail.setVisibility(View.VISIBLE);
                            firstNameTextView.setVisibility(View.VISIBLE);
                            lastNameTextView.setVisibility(View.VISIBLE);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File mFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "cat.png");
                Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath());
                //get Account
                String message = mEmail.getText().toString().trim() + ":" + "6cJf#9de84";
                //make sure block is divisible by 64
                while ((message.length() + deviceID.length() + 1) % 8 != 0){
                    int min = 0;
                    int max = 51;
                    Random r = new Random();
                    int i1 = r.nextInt(max - min + 1) + min;
                    message = message.concat(String.valueOf(alphabt.charAt(i1)));
                }
                String formedPassword = message.split(":")[1]; //take password to form account
                //add deviceID
                message = message.concat(":" + deviceID);
                //transform in binary
                BinaryCode code = new BinaryCode();
                code.binaryCode(message);
                String encodedMessage = code.giveStack(); //get message in binary
                //add DES encryption
                DES Des = new DES();
                String cipherText = Des.encrypt(encodedMessage);
                cipherText = cipherText.concat("0101110000110000"); //add String terminator \0
                EncodeStegnography stegnography = new EncodeStegnography(cipherText, bitmap); //write message in image bitmap
                bitmap = stegnography.getImage();

                try{
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    OutputStream fOut = null;
                    Integer counter = 0;

                    File file = new File(path, "stegno.png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    path.mkdirs();
                    fOut = new FileOutputStream(file);

                    //Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a PNG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                boolean createdSuccesfull = false;
                createdSuccesfull = createAccount(formedPassword);
                if(createdSuccesfull)
                    sendMail();
            }
        });
    }

    private void sendMail() {

        String mail = mEmail.getText().toString().trim();
        String subject = "Voting App Register";
        String message = "Thank you for Registering";


        //CloudStorage cloudStorage = new CloudStorage(RegisterActivity.this,mail, subject, message);
        //cloudStorage.getBitmap();
        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();

    }

    private boolean createAccount(String password){
        String mail = mEmail.getText().toString().trim();
        String firstName = firstNameTextView.getText().toString().trim();
        String lastName = lastNameTextView.getText().toString().trim();
        MD5 md5Hasher = new MD5(password);
        String hashedPasswd = md5Hasher.getMD5();

        boolean checkUser = mail.matches("[a-zA-Z0-9_]{6,20}@[a-zA-Z0-9]+\\.com");
        boolean checkName = firstName.matches("[A-Za-z]{2,}") && lastName.matches("[A-Za-z]{2,}");
        if(!checkName){
            Toast.makeText(RegisterActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkUser) {
            boolean found = false;
            for (User user1 : users)
                if (user1.getUsername().equals(mail)) {
                    found = true;
                    break;
                }
            if (found) {
                Toast.makeText(RegisterActivity.this, "Email is already registered!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                myRef.child(userId).child("username").setValue(mail);
                myRef.child(userId).child("password").setValue(hashedPasswd);
                myRef.child(userId).child("firstName").setValue(firstName);
                myRef.child(userId).child("lastName").setValue(lastName);
                myRef.child(userId).child("voted").setValue(0);
                return true;
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}