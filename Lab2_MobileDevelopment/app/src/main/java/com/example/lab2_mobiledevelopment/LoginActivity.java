package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private FirebaseAuth auth;

    // UI references
    private AutoCompleteTextView emailView;
    private EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.login_password);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void Login(View view){
        //Reset errors
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time when user try to login

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        // check for a valid password
        if(email.matches("")){
            Toast.makeText(this, "You need to enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.matches("")){
            Toast.makeText(this, "You need to enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(password) && !checkValidPassword(password)){
            passwordView.setError("Password must be at least 5 characters");
        }


        if(!checkValidEmail(email) && !TextUtils.isEmpty(email)){
            emailView.setError("Your email is not valid");
            //Toast.makeText(this, "Your email is invalid",Toast.LENGTH_SHORT).show();
        }


        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to user
                        // If signin is successful, the auth state listener will be notified and logic
                        // to handle signed in

                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public boolean checkValidPassword(String password){
        return  password.length() >= 5;
    }

    public boolean checkValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void redirectToSignUpPage(View view){
        Intent redirect = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(redirect);
    }
//    public void redirectToForgotPasswordPage(View view){
//        Intent redirect = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//        startActivity(redirect);
//    }
}
