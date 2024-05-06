package com.anchardo.adminpanel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_screen);

        //This method is for transparent Notification area
        // and present in every Activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



        //Buttons for further navigation

        Button buttonActivity1 = findViewById(R.id.signup); // Sign Up btn
        Button buttonActivity2 = findViewById(R.id.login);  // Login btn


        //listeneres

        buttonActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Sign Up Activity
                Intent intent = new Intent(SelectScreen.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        buttonActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Login Activity
                Intent intent = new Intent(SelectScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
