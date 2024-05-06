package com.anchardo.adminpanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anchardo.adminpanel.adapters.AdapterForSubPurchasesListView;
import com.anchardo.adminpanel.models.PurchaseOrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;import java.util.Locale;


public class UserDetails extends AppCompatActivity {
    DocumentReference documentReference;
    AdapterForSubPurchasesListView adapter;
    ListView listView;
    FloatingActionButton fab, addOrder;
    Map<String, Object> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        listView = findViewById(R.id.purchasesListView);
        fab = findViewById(R.id.fab);
        addOrder = findViewById(R.id.addOrders);
        documentReference = FirebaseFirestore.getInstance().collection("users").document(getIntent().getStringExtra("documentID"));


        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                hashMap.putAll(documentSnapshot.getData());
                Toast.makeText(UserDetails.this, "document exist", Toast.LENGTH_LONG);
                // Get data from Firestore document
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String city = documentSnapshot.getString("city");
                String id = documentSnapshot.getId();
                String phone = documentSnapshot.getString("phone");

                // Find TextViews by their IDs
                TextView textViewName = findViewById(R.id.textViewName);
                TextView textViewEmail = findViewById(R.id.textViewEmail);
                TextView textViewCity = findViewById(R.id.textViewCity);
                TextView textViewID = findViewById(R.id.textViewID);
                TextView textViewPhone = findViewById(R.id.textViewPhone);

                // Set data to TextViews
                textViewName.setText(name);
                textViewEmail.setText(email);
                textViewCity.setText("City:     " + city);
                textViewID.setText("ID:       " + id);
                textViewPhone.setText("Phone:    " + phone);


                List<DocumentReference> referencesList = new ArrayList<>();

                // Retrieve the references stored in the document
                for (int i = 1; ; i++) {
                    String fieldName = "order" + i;
                    if (documentSnapshot.contains(fieldName)) {
                        DocumentReference reference = documentSnapshot.getDocumentReference(fieldName);
                        referencesList.add(reference);
                    } else {
                        break;
                    }
                }

                List<PurchaseOrder> purchaseOrderList = new ArrayList<>();
                adapter = new AdapterForSubPurchasesListView(this, purchaseOrderList);
                listView.setAdapter(adapter);
                setUpFab();
                setUpAddOrders();
                // Fetch data for each referenced document
                for (DocumentReference reference : referencesList) {

                    reference.get().addOnSuccessListener(referencedDocument -> {

                        if (referencedDocument.exists()) {
                            // Handle the referenced document data here
                            // You can access the data using referencedDocument.getData() method
                            PurchaseOrder purchase = new PurchaseOrder();
                            purchase.setProductName(referencedDocument.getString("productName"));
                            purchase.setDate(referencedDocument.getString("date"));
                            purchase.setId(referencedDocument.getId());
                            purchaseOrderList.add(purchase);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(UserDetails.this, "Reference Data does not exist", Toast.LENGTH_SHORT).show();

                            // Referenced document does not exist
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(UserDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        // Handle failure to fetch referenced document
                    });
                }


            } else {
                Toast.makeText(this, "The User data is not available", Toast.LENGTH_SHORT).show();
                // Document does not exist
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to get the information " + e.getMessage(), Toast.LENGTH_SHORT).show();
            // Handle failures
        });
    }

    private void setUpAddOrders() {
        addOrder.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.add_purchase_edittexts);

            builder.setPositiveButton("Create", (dialogInterface, i) -> {
                // Handle positive button click (Create)
                // You can retrieve data from EditText fields here
                Dialog dialog = (Dialog) dialogInterface;
                EditText editTextFieldProductName = dialog.findViewById(R.id.editTextProductName);
                EditText editTextFieldMrp = dialog.findViewById(R.id.editTextMRP);
                EditText editTextFieldQuantity = dialog.findViewById(R.id.editTextQuantity);
                EditText editTextFieldPricing = dialog.findViewById(R.id.editTextPricing);

                String productName = editTextFieldProductName.getText().toString();
                String mrp = editTextFieldMrp.getText().toString();
                String quantity = editTextFieldQuantity.getText().toString();
                String pricing = editTextFieldPricing.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date currentDate = new Date();
                String date =  sdf.format(currentDate);


                FirebaseFirestore db = FirebaseFirestore.getInstance();


                String randomId = UUID.randomUUID().toString();
                // Access the "purchases" collection and create a new document
                DocumentReference purchaseDocumentRef = db.collection("purchases").document(randomId);

                // Create a map to hold the user data
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("productName", productName);
                orderDetails.put("mrp", mrp);
                orderDetails.put("quantity", quantity);
                orderDetails.put("price", pricing);
                orderDetails.put("date",date);
                orderDetails.put("customerID",documentReference.getId());

                purchaseDocumentRef.set(orderDetails).addOnSuccessListener(documentReference -> {
                    // Document successfully added

                    // Add the document reference to your existing HashMap
                    hashMap.put(findNextFieldName(), purchaseDocumentRef);
                    Toast.makeText(UserDetails.this, "Purchase Order Created Succesfully \n Add Shipping Details", Toast.LENGTH_LONG).show();


                    /**
                     * Now in this small section we are asking for shipping details
                     */

                    showAlertBoxForShipping(purchaseDocumentRef,orderDetails);





                }).addOnFailureListener(e -> {
                    // Error adding document
                    Toast.makeText(UserDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });

            // Process the data as needed


            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                // Handle negative button click (Cancel)
                dialogInterface.dismiss();
            });


            AlertDialog alertDialog = builder.create();


            alertDialog.show();
        });
    }

    private void showAlertBoxForShipping(DocumentReference purchaseDocumentRef,Map<String,
            Object> orderDetails) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.shipping_edittexts);

                builder.setPositiveButton("Add", (dialogInterface, i) -> {
                    // Handle positive button click (Create)
                    // You can retrieve data from EditText fields here
                    Dialog dialog = (Dialog) dialogInterface;
                    EditText editTextFieldAddress = dialog.findViewById(R.id.editTextAddress);
                    EditText editTextFieldCity = dialog.findViewById(R.id.editTextCity);
                    EditText editTextFieldPincode = dialog.findViewById(R.id.editTextPincode);

                    String address = editTextFieldAddress.getText().toString();
                    String city = editTextFieldCity.getText().toString();
                    String pincode = editTextFieldPincode.getText().toString();


                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a map to hold the user data

                    orderDetails.put("address", address);
                    orderDetails.put("city", city);
                    orderDetails.put("pincode", pincode);

                    purchaseDocumentRef.set(orderDetails).addOnSuccessListener(documentReference -> {
                        // Document successfully Updated

                        // Add the document reference to your existing HashMap
                        Toast.makeText(UserDetails.this, "Purchase Order Created Succesfully \n Add Shipping Details", Toast.LENGTH_LONG).show();
                        updateDocument();

                    }).addOnFailureListener(e -> {
                        // Error adding document
                        Toast.makeText(UserDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                });

                // Process the data as needed


                builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // Handle negative button click (Cancel)
                    dialogInterface.dismiss();
                });


                AlertDialog alertDialog = builder.create();


                alertDialog.show();


    }


    private String findNextFieldName() {
        int maxOrder = 0;

        // Iterate through the keys of the HashMap
        for (String key : hashMap.keySet()) {
            if (key.startsWith("order")) {
                try {
                    // Extract the order number from the key
                    int orderNumber = Integer.parseInt(key.substring(5));
                    if (orderNumber > maxOrder) {
                        maxOrder = orderNumber;
                    }
                } catch (NumberFormatException e) {
                    // Ignore keys that don't follow the orderX format
                }
            }
        }

        // Increment the maxOrder to find the next field name
        return "order" + (maxOrder + 1);
    }

    void setUpFab() {
        fab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog_custom);

            builder.setPositiveButton("Update", (dialogInterface, i) -> {
                // Handle positive button click (Create)
                // You can retrieve data from EditText fields here
                Dialog dialog = (Dialog) dialogInterface;
                EditText editTextFieldName = dialog.findViewById(R.id.editTextFieldName);
                EditText editTextFieldPhone = dialog.findViewById(R.id.editTextFieldPhone);
                EditText editTextFieldEmail = dialog.findViewById(R.id.editTextFieldEmail);
                EditText editTextFieldCity = dialog.findViewById(R.id.editTextFieldCity);

                String name = editTextFieldName.getText().toString();
                String email = editTextFieldEmail.getText().toString();
                String phone = editTextFieldPhone.getText().toString();
                String city = editTextFieldCity.getText().toString();


                FirebaseFirestore db = FirebaseFirestore.getInstance();


                String randomId = UUID.randomUUID().toString();
// Access the "users" collection and create a new document
                DocumentReference userRef = db.collection("users").document(documentReference.getId());

// Create a map to hold the user data
                hashMap.put("name", name);
                hashMap.put("phone", phone);
                hashMap.put("city", city);
                hashMap.put("email", email);

// Set the document data
                updateDocument();
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

    void updateDocument() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(documentReference.getId()).update(hashMap).addOnSuccessListener(aVoid -> {
            // Document updated successfully
            Toast.makeText(this, "Data Updated Succesfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserDetails.this, UserDetails.class).putExtra("documentID", documentReference.getId()));
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            // Error updating document

        });
    }
}