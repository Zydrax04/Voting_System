package com.example.votingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CloudStorage {
    private Bitmap bitmap = null;
    private String mail;
    private String subject;
    private String message;
    private Context mContext;
    public CloudStorage(Context mContext, String mail, String subject, String message){
        this.mContext = mContext;
        this.mail = mail;
        this.subject = subject;
        this.message = message;

    }

    public Bitmap getBitmap(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("cat.jpg");
        imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //to do
                Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                //JavaMailAPI javaMailAPI = new JavaMailAPI(this,mContext ,mail,subject,message, mBitmap);
                //javaMailAPI.execute();

            }
        });
        return this.bitmap;
    }
}
