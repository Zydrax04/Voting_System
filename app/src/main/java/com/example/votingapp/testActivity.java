package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ImageView avatarimg = findViewById(R.id.avatarimg);

        //OnClickListener for accountBtn -> GetAccount
        avatarimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child("cat.jpg");
                imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //to do
                        Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        avatarimg.setImageBitmap(mBitmap);
                        try{
                            File path = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES);
                            OutputStream fOut = null;
                            Integer counter = 0;
                            System.out.println("*******************************************************************************************************************");
                            System.out.println(path);

                            File file = new File(path, "cat"+counter+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                            path.mkdirs();
                            fOut = new FileOutputStream(file);

                            //Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                            fOut.flush(); // Not really required
                            fOut.close(); // do not forget to close the stream

                            MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}