package com.example.gpstracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
private FirebaseAuth  mAuth;
private TextView ForgotPassword,LoginRegister;
EditText Username,Userpassword;
private Button Login;
private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Username = (EditText) findViewById(R.id.username);
        Userpassword = (EditText) findViewById(R.id.userpassword);
        ForgotPassword = (TextView) findViewById(R.id.Forgot_Password);
        Login = (Button) findViewById(R.id.register);
        LoginRegister = (TextView) findViewById(R.id.login_register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
       mAuth = FirebaseAuth.getInstance();
        Login.setOnClickListener(this);
        LoginRegister.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
            switch(view.getId()){

                case R.id.login_register:

                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
break;
                case R.id.register:
                    userLogin();
                    break;

            }

    }

    private void userLogin() {
        String Email, Password;
        Email = Username.getText().toString().trim();
        Password = Userpassword.getText().toString().trim();
        if(Email.isEmpty()){
            Username.setError("Email is required");
            Username.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Username.setError("Email is invalid");
            Username.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            Userpassword.setError("Password is required");
            Userpassword.requestFocus();
            return;
        }
        if(Password.length()<6){
            Userpassword.setError("Min password is 6 characters");
            Userpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();

                    startActivity(new Intent(LoginActivity.this,MapsActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }

            }



        });
    }
    }




