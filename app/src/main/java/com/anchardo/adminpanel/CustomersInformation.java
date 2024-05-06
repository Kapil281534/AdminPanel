package com.anchardo.adminpanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anchardo.adminpanel.adapters.UserAdapter;
import com.anchardo.adminpanel.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
// MainActivity.java

public class CustomersInformation extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private EditText editTextSearch;

    Button btnShowDialog;
    public static List<User> userList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_information);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



        recyclerView = findViewById(R.id.recyclerView);
        btnShowDialog = findViewById(R.id.addCustomerbtn);
        editTextSearch = findViewById(R.id.customerSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(adapter);


        // Fetch user data from Firestore
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        user.setId(documentSnapshot.getId());
                        userList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {


                    // Handle errors
                });



        //Search Feature
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {

                } else {
                    performSearch(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Assuming you have a button named btnShowDialog
        btnShowDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog_custom);

            builder.setPositiveButton("Create", (dialogInterface, i) -> {
                // Handle positive button click (Create)
                // You can retrieve data from EditText fields here
                Dialog dialog = (Dialog) dialogInterface;
                EditText editTextFieldName = dialog.findViewById(R.id.editTextFieldName);
                EditText editTextFieldPhone = dialog.findViewById(R.id.editTextFieldPhone);
                EditText editTextFieldEmail = dialog.findViewById(R.id.editTextFieldEmail);
                EditText editTextFieldCity = dialog.findViewById(R.id.editTextFieldCity);

                String name = editTextFieldName.getText().toString();
                String phone = editTextFieldPhone.getText().toString();
                String email = editTextFieldEmail.getText().toString();
                String city = editTextFieldCity.getText().toString();


                FirebaseFirestore db = FirebaseFirestore.getInstance();


                String randomId = UUID.randomUUID().toString();
// Access the "users" collection and create a new document
                DocumentReference userRef = db.collection("users").document(randomId);

// Create a map to hold the user data
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", name);
                userData.put("phone", phone);
                userData.put("email", email);
                userData.put("city", city);

// Set the document data
                userRef.set(userData)
                        .addOnSuccessListener(aVoid -> {
                            // Document successfully written
                            Toast.makeText(this, "Customer Created Succesfully",
                                    Toast.LENGTH_SHORT).show();
                            // You can perform any action here after successfully writing the document

                            startActivity(new Intent(CustomersInformation.this,UserDetails.class).putExtra("documentID",randomId));

                        })
                        .addOnFailureListener(e -> {
                            // Error writing document
                            Toast.makeText(this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            // Handle failure to write the document
                        });
                // Process the data as needed
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                // Handle negative button click (Cancel)
                dialogInterface.dismiss();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }


    private void performSearch(String searchText) {
        Query query = db.collection("users").orderBy("city").startAt(searchText).endAt(searchText + "\uf8ff");

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CustomersInformation.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    // Handle search failure
                });
    }
}
