package com.example.votingapp;

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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RegisterActivity extends AppCompatActivity {

    public EditText mEmail;
    public EditText mSubject;
    public EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ImageView getStarted = findViewById(R.id.getStarted);
        mEmail = (EditText)findViewById(R.id.emailAddr);
        Button registerBtn = findViewById(R.id.registerBtn);
        //make email field and register Button invisible
        registerBtn.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);

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
                String message = mEmail.getText().toString() + ":" + "1234" + "\\0";
                BinaryCode code = new BinaryCode();
                code.binaryCode(message);
                String encodedMessage = code.giveStack(); //get message in binary
                EncodeStegnography stegnography = new EncodeStegnography(encodedMessage, bitmap); //write message in image bitmap
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


}