package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewProfile extends AppCompatActivity {

    Toolbar toolbar;

    TextView username, email, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        toolbar = (Toolbar) findViewById(R.id.viewProfileToolbar);

        setSupportActionBar(toolbar);

        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        status = (TextView) findViewById(R.id.status);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Profile");
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // retrieve the user's data based on the value of id
        String userID = FirebaseAuth.getInstance().getUid(); // id

        // object of database reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // Listener for reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    // register the data using User model class
                    User user = snapshot.child(userID).getValue(User.class);

                    // showing tha data inside ViewProfile Activity:
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                    status.setText(user.getStatus());
                }
                else
                {
                    Toast.makeText(ViewProfile.this, "No data selected!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }//End onCreate
}