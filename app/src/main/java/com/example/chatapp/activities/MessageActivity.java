package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView username;

    ImageView btn_send;
    EditText txt_send;
    FirebaseUser firebaseUser;

    Intent intent;
    DatabaseReference reference; // because by the userId we will retrieve the user's data

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // solved the design:
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager); // pass object

        // receive the userId:
        intent = getIntent();
        String userId = intent.getStringExtra("userId");


        Toolbar toolbar = findViewById(R.id.MessageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MessageActivity.this,ViewContacts.class);
                startActivity(intent);
                finish();
            }
        });


        btn_send = (ImageView) findViewById(R.id.btn_send) ;
        txt_send = (EditText) findViewById(R.id.text_send) ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Listener
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // retrieve the text from EditText and store it
                String msg = txt_send.getText().toString().trim();

                // check if empty:
                if(!msg.isEmpty())
                {
                    sendMsg(firebaseUser.getUid(), userId, msg);

                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty msg!", Toast.LENGTH_SHORT).show();
                }
            // make editText empty after send the msg:
                txt_send.setText("");
            }
        });

        // initializing
        username = (TextView) findViewById(R.id.username);
        profile_img = (CircleImageView) findViewById(R.id.profile_img);



        reference = FirebaseDatabase.getInstance().getReference("users").child(userId); // to send command to the database
        //Listener:
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // storing the retrieve data using User model class
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());

                // show the profileImage inside circleImageView
                if(user.getImageUrl().equals("default"))
                {
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_img);
                }

                // invoke method
                readMessages(firebaseUser.getUid(), userId, user.getImageUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // method
    private void sendMsg(String sender, String receiver, String msg )
    {
          DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();

          // storing value using HashMap collection
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", msg);

        // pass values to the DB
        reference1.child("Chats").push().setValue(hashMap);
    }

    // method to receive the chats data
    private void readMessages(String myId, String userId, String ImgUrl)
    {
       mChat = new ArrayList<>();

       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats"); // object
       // Listener
       reference1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               // to solved duplicated issue:
               mChat.clear();

               for(DataSnapshot dataSnapshot: snapshot.getChildren())
               {
                   // storing received values using Chat model class
                   Chat chat = dataSnapshot.getValue(Chat.class);
                   if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId))
                   {
                       // add data to the arrayList
                       mChat.add(chat);
                   }

                   // pass data to adapter
                   messageAdapter = new MessageAdapter(getApplicationContext(), mChat, ImgUrl);
                   recyclerView.setAdapter(messageAdapter); // linked recycler with adapter

              }
           }



           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}