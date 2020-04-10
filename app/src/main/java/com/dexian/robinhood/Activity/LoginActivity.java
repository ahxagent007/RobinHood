package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dexian.robinhood.R;
import com.dexian.robinhood.SharedPreffClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    String TAG = "XIAN";

    Button btn_login;
    EditText ET_pass, ET_email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        ET_pass = findViewById(R.id.ET_pass);
        ET_email = findViewById(R.id.ET_email);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ET_pass.length()>0 && ET_email.length()>0){
                    btn_login.setEnabled(false);
                    signIn(ET_email.getText().toString(), ET_pass.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "PLEASE ENTER EMAIL AND PASSWORD!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signIn(String email, String pass){

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.i(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new SharedPreffClass(getApplicationContext()).saveUserName(user.getDisplayName());
                            new SharedPreffClass(getApplicationContext()).saveUserID(user.getUid());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            btn_login.setEnabled(true);
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });

    }
}
