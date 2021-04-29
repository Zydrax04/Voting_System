package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAccountActivity extends AppCompatActivity {

    private Button addBtn;
    private TextView cnpTextView;
    private ArrayList<String> CNPlist = new ArrayList<>();

    //FireBase Reference
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myCNPRef = database.getReference("CNP");
    private final String userId = myCNPRef.push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);


        //Page Buttons and TextFields
        addBtn = findViewById(R.id.createBtn);
        cnpTextView = findViewById(R.id.cnpTextField);

        //Take CNPs
        myCNPRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    CNPlist.add( ds.child("ID").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        //addAccountBtn Click Listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                String cnpText = cnpTextView.getText().toString().trim();
                System.out.println(CNPlist.size() + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                for(String cnp : CNPlist){
                    System.out.println(cnp + " &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    if(cnp.equals(cnpText)){
                        Toast.makeText(AddAccountActivity.this, "ID exists already!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                myCNPRef.child(userId).child("ID").setValue(cnpText);
                myCNPRef.child(userId).child("taken").setValue(0);
                finish();
            }
        });
    }
}