package com.anchardo.adminpanel.adapters;

import static com.anchardo.adminpanel.PurchaseOrders.purchaseList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anchardo.adminpanel.OrderDetails;
import com.anchardo.adminpanel.R;
import com.anchardo.adminpanel.models.PurchaseOrder;
import com.anchardo.adminpanel.models.User;

import java.util.List;

// UsersAdapter.java
public class PurchaseOrdersAdapter extends RecyclerView.Adapter<PurchaseOrdersAdapter.ViewHolder> {

    private List<PurchaseOrder> purchaseOrdersList;
    Context context;

    public PurchaseOrdersAdapter(List<PurchaseOrder> purchaseOrderList,Context context) {
        this.purchaseOrdersList = purchaseOrderList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pruchase, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseOrder purchase = purchaseList.get(position);
        holder.textViewName.setText(purchase.getProductName());
        holder.textViewmrp.setText("MRP: $"+purchase.getMrp());
        holder.textViewQuantity.setText("QTY: "+purchase.getQuantity());
        holder.textViewPricing.setText("price: $"+purchase.getPrice());
        holder.textViewPurchasedate.setText(purchase.getDate());

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OrderDetails.class).putExtra(
                        "documentID",purchase.getId()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return purchaseOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewQuantity, textViewmrp,textViewPricing,textViewPurchasedate;
        Button details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewproductName);
            details=itemView.findViewById(R.id.productDetailsBtn);
            textViewQuantity = itemView.findViewById(R.id.textViewquantity);
            textViewPurchasedate=itemView.findViewById(R.id.textViewpurchasedate);
            textViewmrp = itemView.findViewById(R.id.textViewmrp);
            textViewPricing = itemView.findViewById(R.id.textViewpricing);
        }
    }
}
