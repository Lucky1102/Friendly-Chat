package com.rup.registeractivity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private EditText user;
    private Button button;
    private String DisplayName;
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Name");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser= mAuth.getCurrentUser();
        uid= currentuser.getUid();


        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");

        user = (EditText) findViewById(R.id.username);
        button=(Button)findViewById(R.id.submit);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveToDatabase();
            }
        });

    }

    private void SaveToDatabase()
    {
        DisplayName=user.getText().toString();
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("name", DisplayName);
        productMap.put("uid", uid);

        UsersRef.child(uid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent= new Intent(ProfileActivity.this,FriendsList.class);
                            startActivity(intent);

                            Toast.makeText(ProfileActivity.this, "Added to Database successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            String message = task.getException().toString();
                            Toast.makeText(ProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

}
