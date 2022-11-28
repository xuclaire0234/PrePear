/**
 * Class Name: LoginActivity
 * Version Information: Version 1.0.0
 * Create Date: Nov 8th, 2022
 * Author: Shihao Liu
 * Copyright Notice:
 */

package com.example.prepear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepear.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


/**
 * This class is used by the user to sign in with his/her account and password for accessing and using PrePear app
 */
public class LoginActivity extends AppCompatActivity {
    // on below: initialize class variables
    private TextInputEditText userEmailInput;
    private TextInputEditText userPasswordInput;
    ProgressBar loginStatusProgressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailInput = findViewById(R.id.userEmail_input_text);
        userPasswordInput = findViewById(R.id.password_input);
        TextView registerText = findViewById(R.id.new_user_register_text);
        TextView forgetPasswordText = findViewById(R.id.forget_password_text);
        Button loginButton = findViewById(R.id.inputs_confirm_button);
        loginStatusProgressBar = findViewById(R.id.user_login_status);
        loginStatusProgressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) { // Check if the user is already registered
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }


        // On below part: the user chooses to register a new account
        registerText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),
                RegisterActivity.class)));

        // On below part: the user selects "Forget Password?" option and reset user's password
        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(v.getContext()); // EditText input field for user to enter user email for receiving password-reset link
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext()); // initialize the password-set web window
                passwordResetDialog.setTitle("Reset Your Password ?"); // set the password-reset web window title
                passwordResetDialog.setMessage("Enter your email to Receive Reset Password Link"); //
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On below part: extract the email and send the reset link
                        String userEmail = resetEmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(userEmail) // send the password-reset email containing the corresponding link to the user
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LoginActivity.this,
                                                "Reset Link has been sent to your email.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this,
                                                "Error occurs when sending Reset Link !" + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the current dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = Objects.requireNonNull(userEmailInput.getText()).toString().trim();
                String userPassword = Objects.requireNonNull(userPasswordInput.getText()).toString().trim();

                if (TextUtils.isEmpty(userEmail)) { // if user input field for user's email is
                    userEmailInput.setError("Please Enter Your Email!");
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) { //
                    userPasswordInput.setError("Please Enter Your Password!");
                    return;
                }
                if (userPassword.length() < 6) { //
                    userPasswordInput.setError("User Password Minimum Length is 6.");
                    return;
                }

                loginStatusProgressBar.setVisibility(View.VISIBLE);

                // On below part: authenticate the user
                firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { //
                                    Toast.makeText(LoginActivity.this,"Successful Login, Welcome back!",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    loginStatusProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}