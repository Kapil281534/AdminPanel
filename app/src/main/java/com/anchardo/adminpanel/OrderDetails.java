package com.anchardo.adminpanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {
    private TextView textViewProductName, textViewQuantity, textViewPricing, textViewMRP,
            textViewPurchaseOrderId, textViewCustomerId, textViewAddress, textViewCity,
            textViewPincode;

    private EditText editTextProductName, editTextQuantity, edittextViewPricing, edittextViewMRP,
            edittextViewAddress,
            edittextViewCity, edittextViewPincode;

    DocumentReference docRef ;


    private FloatingActionButton toggleButton;
    private boolean isEditing = false;
    Map<String, Object> documentHashMap = new HashMap<>();

    private Button viewCustomerBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);



        docRef=
                FirebaseFirestore.getInstance().collection("purchases").document(getIntent().getStringExtra("documentID"));
        //Notification Area
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //initialization
        viewCustomerBtn = findViewById(R.id.viewCustomerDetails);
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        textViewPricing = findViewById(R.id.textViewPricing);
        textViewMRP = findViewById(R.id.textViewMRP);
        textViewPurchaseOrderId = findViewById(R.id.textViewPurchaseOrderId);
        textViewCustomerId = findViewById(R.id.textViewCustomerId);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewCity = findViewById(R.id.textViewCity);
        textViewPincode = findViewById(R.id.textViewPincode);

        editTextProductName = findViewById(R.id.meditTextProductName);
        editTextQuantity = findViewById(R.id.meditTextQuantity);
        edittextViewAddress = findViewById(R.id.meditTextAddress);
        edittextViewCity = findViewById(R.id.meditTextCity);
        edittextViewMRP = findViewById(R.id.meditTextMRP);
        edittextViewPricing = findViewById(R.id.meditTextPricing);
        edittextViewPincode = findViewById(R.id.meditTextPinCode);

        toggleButton = findViewById(R.id.editToggle);



        setupToggleBtn();
        updateData();
    }

    private void setupToggleBtn() {
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    // Change EditTexts back to TextViews
                    String productName, quantity, pricing, mrp, address, city, pincode;

                    productName = editTextProductName.getText().toString();
                    quantity = editTextQuantity.getText().toString();
                    pricing = edittextViewPricing.getText().toString();
                    mrp = edittextViewMRP.getText().toString();
                    address = edittextViewAddress.getText().toString();
                    city = edittextViewCity.getText().toString();
                    pincode = edittextViewPincode.getText().toString();


                    // Check if any EditText field is empty
                    if (productName.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        editTextProductName.setError("Field required");
                        editTextProductName.requestFocus();
                    }
                    if (quantity.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        editTextQuantity.setError("Field required");
                        editTextQuantity.requestFocus();
                    }
                    if (pricing.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        edittextViewPricing.setError("Field required");
                        edittextViewPricing.requestFocus();
                    }
                    if (mrp.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        edittextViewMRP.setError("Field required");
                        edittextViewMRP.requestFocus();
                    }
                    if (address.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        edittextViewAddress.setError("Field required");
                        edittextViewAddress.requestFocus();
                    }
                    if (city.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        edittextViewCity.setError("Field required");
                        edittextViewCity.requestFocus();
                    }
                    if (pincode.isEmpty()) {
                        // Set error message and request focus for the empty EditText field
                        edittextViewPincode.setError("Field required");
                        edittextViewPincode.requestFocus();
                    }

// Check if any EditText field is empty
                    if (productName.isEmpty() || quantity.isEmpty() || pricing.isEmpty() || mrp.isEmpty() || address.isEmpty() || city.isEmpty() || pincode.isEmpty()) {
                        // Notify the user that one or more fields are empty
                        Toast.makeText(OrderDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        // All fields are filled, proceed with further actions
                        // For example, you can save the data to Firestore or perform other operations
                        documentHashMap.put("productName", productName);
                        documentHashMap.put("quantity", quantity);
                        documentHashMap.put("price", pricing);
                        documentHashMap.put("mrp", mrp);
                        documentHashMap.put("address", address);
                        documentHashMap.put("city", city);
                        documentHashMap.put("pincode", pincode);

                        docRef.set(documentHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OrderDetails.this, "Data Updated Succesfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OrderDetails.this,OrderDetails.class).putExtra("documentID",docRef.getId()));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                } else {

                    // Change TextViews to EditTexts
                    editTextProductName.setText("");
                    editTextQuantity.setText("");
                    edittextViewPincode.setText("");
                    edittextViewMRP.setText("");
                    edittextViewCity.setText("");
                    edittextViewAddress.setText("");
                    edittextViewPricing.setText("");

                    editTextProductName.setHint(textViewProductName.getText().toString());
                    editTextQuantity.setHint(textViewQuantity.getText().toString());
                    edittextViewPincode.setHint(textViewPincode.getText().toString());
                    edittextViewMRP.setHint(textViewMRP.getText().toString());
                    edittextViewCity.setHint(textViewCity.getText().toString());
                    edittextViewAddress.setHint(textViewAddress.getText().toString());
                    edittextViewPricing.setHint(textViewPricing.getText().toString());

                    // Hide TextViews, show EditTexts
                    textViewProductName.setVisibility(View.GONE);
                    textViewQuantity.setVisibility(View.GONE);
                    textViewPincode.setVisibility(View.GONE);
                    textViewMRP.setVisibility(View.GONE);
                    textViewCity.setVisibility(View.GONE);
                    textViewAddress.setVisibility(View.GONE);
                    textViewPricing.setVisibility(View.GONE);


                    editTextProductName.setVisibility(View.VISIBLE);
                    editTextQuantity.setVisibility(View.VISIBLE);
                    edittextViewPincode.setVisibility(View.VISIBLE);
                    edittextViewMRP.setVisibility(View.VISIBLE);
                    edittextViewCity.setVisibility(View.VISIBLE);
                    edittextViewAddress.setVisibility(View.VISIBLE);
                    edittextViewPricing.setVisibility(View.VISIBLE);

                    // Change button text
                    toggleButton.setImageDrawable(getResources().getDrawable(R.drawable.done,
                            OrderDetails.this.getTheme()));
                }

                // Toggle editing state
                isEditing = !isEditing;
            }
        });
    }

    private void updateData() {
        // Assuming you have a reference to the document

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                documentHashMap.putAll(documentSnapshot.getData());

                // Get data from the document snapshot
                String productName = documentSnapshot.getString("productName");
                String quantity = documentSnapshot.getString("quantity");
                String pricing = documentSnapshot.getString("price");
                String mrp = documentSnapshot.getString("mrp");
                String customerId = documentSnapshot.getString("customerID");
                String address = documentSnapshot.getString("address");
                String city = documentSnapshot.getString("city");
                String pincode = documentSnapshot.getString("pincode");

                // Populate TextViews with the data
                textViewProductName.setText("Product Name: " + productName);
                textViewQuantity.setText("Quantity: " + quantity);
                textViewPricing.setText("Pricing: " + pricing);
                textViewMRP.setText("MRP: " + mrp);
                textViewPurchaseOrderId.setText("Purchase Order ID: " + documentSnapshot.getId());
                textViewCustomerId.setText("Customer ID: " + customerId);
                textViewAddress.setText("Address: " + address);
                textViewCity.setText("City: " + city);
                textViewPincode.setText("Pincode: " + pincode);
                viewCustomerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OrderDetails.this, UserDetails.class).putExtra("documentID", customerId));
                    }
                });
            } else {
                Toast.makeText(OrderDetails.this, "The Purchase Details Does Not exists", Toast.LENGTH_SHORT);
                finish();
                // Document does not exist
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(OrderDetails.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT);
            finish();
            // Handle failure to fetch document
        });
    }
}

