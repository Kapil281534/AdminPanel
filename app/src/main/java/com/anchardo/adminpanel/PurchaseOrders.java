package com.anchardo.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.anchardo.adminpanel.adapters.PurchaseOrdersAdapter;
import com.anchardo.adminpanel.adapters.UserAdapter;
import com.anchardo.adminpanel.models.PurchaseOrder;
import com.anchardo.adminpanel.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
// MainActivity.java

public class PurchaseOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PurchaseOrdersAdapter adapter;
    private EditText editTextSearch;

    public static List<PurchaseOrder> purchaseList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_orders);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        recyclerView = findViewById(R.id.recyclerViewPurchaseOrder);
        editTextSearch=findViewById(R.id.customerSearchProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        purchaseList = new ArrayList<>();
        adapter = new PurchaseOrdersAdapter(purchaseList,this);
        recyclerView.setAdapter(adapter);


        // Fetch user data from Firestore
        db.collection("purchases")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        PurchaseOrder purchase = documentSnapshot.toObject(PurchaseOrder.class);
                        purchase.setId(documentSnapshot.getId());
                        purchaseList.add(purchase);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(PurchaseOrders.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Handle errors
                });


        //Search feature
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {

                } else {
                    performSearch(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void performSearch(String searchText) {
        Query query =
                db.collection("purchases").orderBy("productName").startAt(searchText).endAt(searchText +
                        "\uf8ff");

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    purchaseList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        PurchaseOrder purchase = documentSnapshot.toObject(PurchaseOrder.class);
                        purchaseList.add(purchase);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PurchaseOrders.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    // Handle search failure
                });
    }
}
