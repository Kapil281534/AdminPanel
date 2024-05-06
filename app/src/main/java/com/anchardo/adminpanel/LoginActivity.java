package com.anchardo.adminpanel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Checking if user is already logged in
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to another activity
            openHomePage();
        } else {
            // User is not logged in, proceed with login process
            setContentView(R.layout.activity_login);
            // Add login UI setup code here
        }



        editTextEmail = findViewById(R.id.edittextemail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.realloginbutton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }


        //Login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to next activity or any other action
                    openHomePage();
                } else {
                    // If sign in fails, display a message to the user based on the exception
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        // No account found with the given email address
                        Toast.makeText(LoginActivity.this, "No account found with this email", Toast.LENGTH_SHORT).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // Incorrect password
                        Toast.makeText(LoginActivity.this, "Incorrect password/Email", Toast.LENGTH_SHORT).show();
                    } else {
                        // Other errors
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openHomePage() {
        startActivity(new Intent(LoginActivity.this, HomeScreen.class));
        finish();
    }

    public void forgotPass(View view) {
        startActivity(new Intent(this, ForgotPassword.class));// navigate to new activity
        finish();  //Optional: finish the LoginActivity so the user cannot navigate back to it
        // using the back button

    }
}
