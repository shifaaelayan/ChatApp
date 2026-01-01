package com.example.chatapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.adapter.userAdapter;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewContacts extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    userAdapter userAdapter;
    List<User> mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        toolbar = (Toolbar) findViewById(R.id.viewContactToolbar);


        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mUser = new ArrayList<>();

        getUser(); // method

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Contacts");
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewContacts.this,HomeActivity.class));
                finish();
            }
        });
    }

    private void getUser() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        // Listener:
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class); // storing the retrieve data inside the model class
                    if(!user.getId().equals(firebaseUser.getUid())) // expect current user
                    {
                        // adding users
                        mUser.add(user);
                    }
                    // pass values to the adapter
                    userAdapter  = new userAdapter(getApplicationContext(), mUser);

                    // Linked adapter with recyclerView
                    recyclerView.setAdapter(userAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}