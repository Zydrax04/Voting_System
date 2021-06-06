package com.example.votingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeMultipart;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class JavaMailAPI extends AsyncTask<Void,Void,Void>  {

    //Add those line in dependencies
    //implementation files('libs/activation.jar')
    //implementation files('libs/additionnal.jar')
    //implementation files('libs/mail.jar')

    //Need INTERNET permission

    //Variables
    private Context mContext;
    private Session mSession;
    private boolean isRecoveryEmail = false;
    private String mEmail;
    private String mSubject;
    private String mMessage;
    private Bitmap mbitmap = null;
    private File mFile = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "stegno.png");
    private ProgressDialog mProgressDialog;

    //Constructor
    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        //getBitMap();
    }
    //Constructor 2
    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage, boolean isRecoveryEmail) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.isRecoveryEmail = isRecoveryEmail;
    }


 /*   public void getBitMap(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("cat.jpg");
        final Bitmap[] lBitmap = new Bitmap[1];
        imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //to do
                Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                lBitmap[0] = mBitmap;
            }
        });
        this.mbitmap = lBitmap[0];
        System.out.println("Bitmap: " + mbitmap);
    }

    public void saveBitMap(){
        try{
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            OutputStream fOut = null;
            Integer counter = 0;
            System.out.println("*******************************************************************************************************************");
            System.out.println(path);

            File file = new File(path, "FitnessGirl"+counter+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            path.mkdirs();
            fOut = new FileOutputStream(file);

            //Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(mContext.getApplicationContext().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            this.mFile = file;
            System.out.println("Am salvat");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } */

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        mProgressDialog = ProgressDialog.show(mContext,"Sending message", "Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send
        mProgressDialog.dismiss();

        //Show success toast
        Toast.makeText(mContext,"Message Sent",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                    }
                });

        try {

            //Creating MimeMessage object
            MimeMessage message = new MimeMessage(mSession);

            //Setting sender address
            message.setFrom(new InternetAddress(Utils.EMAIL));
            //Adding receiver
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            //Adding subject
            message.setSubject(mSubject);
            //Adding message
            //message.setText(mMessage);

            //From baeldung tutorial
            BodyPart messageBodypart = new MimeBodyPart();
            messageBodypart.setText(mMessage);
            //add attachment
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //Uri tempUri = getImageUri(mContext.getApplicationContext(), mbitmap);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            //attachmentBodyPart.attachFile(new File(tempUri));
            //create file
            //File file = new File("");
            //OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            //mbitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            //os.close();
            //attachmentPart.attachFile(file);
            //add bodyParts to one MimeMultipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodypart);
            //multipart.addBodyPart(attachmentPart);
            //add multiPart to MimeMessage
            //message.setContent(multipart);

            //saveBitMap();
            if(!isRecoveryEmail) {
                attachmentBodyPart.attachFile(mFile);
                multipart.addBodyPart(attachmentBodyPart);
            }
            message.setContent(multipart);
            //Sending email
            Transport.send(message);

            //BodyPart messageBodyPart = new MimeBodyPart();

            //messageBodyPart.setText(mMessage);

            //Multipart multipart = new MimeMultipart();

            //multipart.addBodyPart(messageBodyPart);

            //messageBodyPart = new MimeBodyPart();

            //DataSource source = new FileDataSource("src\\main\\res\\drawable\\login.jpg");

            //messageBodyPart.setDataHandler(new DataHandler(source));

            //messageBodyPart.setFileName("src\\main\\res\\drawable\\login.jpg");

            //multipart.addBodyPart(messageBodyPart);

            //mm.setContent(multipart);


            // Send message
            //Transport.send(message);



        } catch (MessagingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}