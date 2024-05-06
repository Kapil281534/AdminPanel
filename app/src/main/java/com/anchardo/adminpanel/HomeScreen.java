package com.anchardo.adminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.net.URLEncoder;

public class HomeScreen extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        //transparent notification area
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //initialise
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CardView cardView= findViewById(R.id.customerManangeCV);
        CardView cardView2= findViewById(R.id.ordersManageCV);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        //listeners
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,CustomersInformation.class));
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,PurchaseOrders.class));
            }
        });


        //setup naviagtion drawer listeners
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.nav_rate_us) {
                    // Handle Feature 1 click
                    Toast.makeText(HomeScreen.this, "Rating Us", Toast.LENGTH_SHORT).show();
                    rateUs();
                } else if (itemId == R.id.nav_feature_2) {
                    // Handle Feature 2 click
                    // URL of the webpage to open
                    String url = "https://softskills.web.app";

                    // Create Intent to open URL in web browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                }else if (itemId == R.id.nav_log_out) {
                    // Handle Feature 2 click
                    Toast.makeText(HomeScreen.this, "Logging Out", Toast.LENGTH_SHORT).show();
                    logOut();
                }
                // Add more if-else conditions for other items if needed
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //additional things
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void rateUs() {

        /**
         * this code is for Rate Us Feature
         * by using whatsapp
         */



        // Number to send the message to
        String phoneNumber = "91 9109345593";

        // Message to pre-fill
        String message = "Hello, I used your 'Admin Panel' App and out of 5 I will rate this app > ";

        Context context=this;

        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url =
                    "https://api.whatsapp.com/send?phone="+ phoneNumber +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            context.startActivity(i);


        } catch (Exception e){

            e.printStackTrace();
        }/*
        // Create the Intent with the ACTION_SENDTO action
        // This action specifies that the Intent should be used to send data to someone, but does not send the data itself
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // Set the data (URI) for the Intent, specifying the WhatsApp URL with the phone number and message
        // The phone number must be prefixed with "whatsapp://" and the message can be appended using the "text" parameter
        // Note: Use Uri.encode() to properly encode the message text
        intent.setData(Uri.parse("whatsapp://send?phone=" + phoneNumber + "&text=" + Uri.encode(message)));

        // Start the Intent to open WhatsApp with the pre-filled message
        startActivity(intent);*/
    }

    private void logOut() {
        //logout code
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SelectScreen.class));
        finish();
    }

    // Include onOptionsItemSelected() method to handle drawer toggle button click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
