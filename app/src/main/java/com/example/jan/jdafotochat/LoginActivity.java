package com.example.jan.jdafotochat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    EditText usernameLabel = null;
    EditText passwordLabel = null;
    Button loginBtn = null;
    Button registerBtn = null;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameLabel = findViewById(R.id.username);
        passwordLabel = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        // Button listeners
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameLabel.getText().toString();
                final String password = passwordLabel.getText().toString();
                CreateUser(mAuth, username, password);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameLabel.getText().toString();
                final String password = passwordLabel.getText().toString();
                LoginUser(mAuth, username, password);


            }
        });
    }

    private void LoginUser(final FirebaseAuth mAuth, String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("LOGIN", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this, MediaActivity.class));
                        } else {
                            Log.i("LOGIN", "signInWithEmail:failure");
                        }
                    }
                });
    }

    public void CreateUser(final FirebaseAuth mAuth, final String username, final String password) {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("ACCOUNT", "success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            WriteUserOnDatabase(username, password);
                        }
                        else {
                            Log.i("ACCOUNT", "failed" + task.getException());
                        }
                    }
                });
    }

    public void WriteUserOnDatabase(String username, String password) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference users = db.getReference().child("users");
        UUID uuid = UUID.randomUUID();
        DatabaseReference user = users.child(uuid.toString());
        DatabaseReference email = user.child("email");
        DatabaseReference psw = user.child("password");

        email.setValue(username);
        psw.setValue(password);
    }
}
