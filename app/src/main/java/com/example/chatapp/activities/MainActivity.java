package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

// this activity for SignUp.

    TextInputLayout username, email, status, password;
    String  nameValue, emailValue, statusValue, passwordValue;
    Button signUp;
// ---------------------------------------------------------------------------------------------------------------


    FirebaseAuth auth; // object

    DatabaseReference reference;

    TextView SignInActivity; // if you have an account



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

// if the user have an account, let her/his in HomeActivity
        if(FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        }

        username = (TextInputLayout) findViewById(R.id.TextInputNameLayout);
        email = (TextInputLayout) findViewById(R.id.TextInputEmailLayout);
        status = (TextInputLayout) findViewById(R.id.TextInputStatusLayout);
        password = (TextInputLayout) findViewById(R.id.TextInputPassword);
        SignInActivity = (TextView) findViewById(R.id.signInActivity);

    // if you have an account:
        SignInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }
        });

        signUp = (Button) findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // retrieve data from the TextInputLayout fields:
                nameValue = username.getEditText().getText().toString().trim();
                emailValue = email.getEditText().getText().toString().trim();
                statusValue = status.getEditText().getText().toString().trim();
                passwordValue = password.getEditText().getText().toString().trim();

                // check the fields to ensure that aren't empty:
                if (nameValue.isEmpty() || emailValue.isEmpty() || statusValue.isEmpty() || passwordValue.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                   register(nameValue, emailValue, statusValue, passwordValue);
                }

                }
        });


    }

    private void register(String nameV, String emailV, String statusV, String passwordV) {

        auth.createUserWithEmailAndPassword(emailV, passwordV).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())  // to execute commands
                {
                   // return the user info
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    // register the users info using HashMap
                    HashMap<String, String> hashMap = new HashMap<>();

                    // add the values:
                    hashMap.put("id", userId);
                    hashMap.put("username", nameV);
                    hashMap.put("email", emailV);
                    hashMap.put("status", statusV);
                    hashMap.put("password", passwordV);

                    hashMap.put("imageUrl", "default"); // new value added, each new account will take a default value for imageUrl

                // pass all values to reference for put them under the child "userId":
                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            finish();
                        }

                        else {
                            Toast.makeText(MainActivity.this, "SignUp failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }

                else
                {
                 //  Toast.makeText(MainActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(
                            MainActivity.this,
                            task.getException().getMessage(),
                            Toast.LENGTH_LONG
                    ).show();


                }

            }
        });
    }


}



