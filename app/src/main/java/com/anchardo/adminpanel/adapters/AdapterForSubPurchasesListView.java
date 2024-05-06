package com.anchardo.adminpanel.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.anchardo.adminpanel.OrderDetails;
import com.anchardo.adminpanel.R;
import com.anchardo.adminpanel.models.PurchaseOrder;

import java.util.List;

public class AdapterForSubPurchasesListView extends ArrayAdapter<PurchaseOrder> {

    private Context context;
    private List<PurchaseOrder> customObjectList;

    public AdapterForSubPurchasesListView(Context context, List<PurchaseOrder> customObjectList) {
        super(context, 0, customObjectList);
        this.context = context;
        this.customObjectList = customObjectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_purchase, parent, false);
        }

        TextView textViewProductName = listItemView.findViewById(R.id.textViewProductName);
        TextView textViewDate = listItemView.findViewById(R.id.textViewDate);
        Button buttonDetails = listItemView.findViewById(R.id.buttonDetails);

        PurchaseOrder purchaseOrder = customObjectList.get(position);

        // Set data to views
        textViewProductName.setText(purchaseOrder.getProductName());
        textViewDate.setText(String.valueOf(purchaseOrder.getDate())); // Assuming field2 is an int

        // Handle details button click
        buttonDetails.setOnClickListener(view -> {
            context.startActivity(new Intent(context, OrderDetails.class).putExtra("documentID",
                    purchaseOrder.getId()));
        });

        return listItemView;
    }
}
