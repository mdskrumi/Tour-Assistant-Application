package com.example.rumi.tourassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private EditText userEmailEditText , userPassEditText ;
    private TextView signUpTextView , statusTextView;
    private Button signInButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPassEditText = findViewById(R.id.userPasswordEditText);
        signUpTextView = findViewById(R.id.createNewUserTextView);
        statusTextView = findViewById(R.id.statusTextView);
        signInButton = findViewById(R.id.signInButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Pressing SIGN UP Text View


        if(firebaseUser != null){
            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
        }


        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.create_account_dialog,null);

                final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                        .setView(view)
                        .show();

                final EditText newUserEmail , newUserPass , newUserConfirmPass;
                Button signUPButton , cancleButton;


                newUserEmail = view.findViewById(R.id.newUserEmailEditText);
                newUserPass = view.findViewById(R.id.newUserPasswordEditText);
                newUserConfirmPass = view.findViewById(R.id.newUserComfirmPasswordEditText);

                signUPButton = view.findViewById(R.id.signupButton);
                cancleButton = view.findViewById(R.id.cancleButton);

                // Creating a New USER

                signUPButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = newUserEmail.getText().toString();
                        String pass = newUserPass.getText().toString();
                        String cPass = newUserConfirmPass.getText().toString();

                        if(email.isEmpty()){
                            newUserEmail.setError("This Field is Required");
                            return;
                        }
                        if(pass.isEmpty()){
                            newUserPass.setError("This Field is Required");
                            return;
                        }
                        if(cPass.isEmpty()){
                            newUserConfirmPass.setError("This Field is Required");
                            return;
                        }
                        if(!cPass.equals(pass)){
                            newUserPass.setError("Password Does Not Match");
                            return;
                        }
                        if(pass.length() < 6){
                            newUserPass.setError("Password is too Short");
                            return;
                        }

                        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            statusTextView.setText("Account Created");
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            alertDialog.cancel();
                                        }
                                    }
                                })
                                .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if(e.getMessage().equals("The email address is badly formatted.")){
                                            newUserEmail.setError("Invalid Email");
                                        }
                                        if(e.getMessage().equals("The email address is already in use by another account.")){
                                            newUserEmail.setError("Email address is already in use");
                                        }
                                        statusTextView.setText("Account Not Created");
                                        Log.e("MY ERROR", "onFailure: " + e.getMessage() );
                                    }
                                });
                    }
                });
                cancleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = userEmailEditText.getText().toString();
                String userPass = userPassEditText.getText().toString();

                if(userEmail.isEmpty()){
                    userEmailEditText.setError("This Field is Required");
                    return;
                }
                if(userPass.isEmpty()){
                    userPassEditText.setError("This Field is Required");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(userEmail,userPass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    statusTextView.setText("SIGNED IN");
                                    startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                                }

                            }
                        })
                        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                statusTextView.setText("Wrong Email Or Password");
                            }
                        });
            }
        });




    }
}
