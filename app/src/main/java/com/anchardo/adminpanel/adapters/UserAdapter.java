package com.anchardo.adminpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anchardo.adminpanel.R;
import com.anchardo.adminpanel.UserDetails;
import com.anchardo.adminpanel.models.User;

import java.util.List;

// UsersAdapter.java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    Context context;

    public UserAdapter(List<User> userList,Context context) {
        this.userList = userList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewCity.setText(user.getCity());
        holder.textViewPhone.setText(user.getPhone());
holder.details.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, UserDetails.class).putExtra("documentID",user.getId()));
    }
});

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewCity,textViewPhone;
        Button details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            details=itemView.findViewById(R.id.userDetailsBtn);
            textViewCity = itemView.findViewById(R.id.textViewCity);
            textViewPhone = itemView.findViewById(R.id.textViewphone);
        }
    }
}
