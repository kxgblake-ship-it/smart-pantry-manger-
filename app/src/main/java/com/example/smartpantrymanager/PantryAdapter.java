package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems;
    private final OnItemActionListener listener;

    // Interface so MainActivity can be told when edit/delete is tapped on a row
    public interface OnItemActionListener {
        void onEditClicked(PantryItem item);
        void onDeleteClicked(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryItems, OnItemActionListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    // Lets MainActivity refresh the list after add/edit/delete
    public void updateData(List<PantryItem> newItems) {
        this.pantryItems = newItems;
        notifyDataSetChanged(); // tells RecyclerView to redraw everything
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.textItemName.setText(item.getName());

        String quantityText = item.getQuantity() + " " + item.getUnit();
        holder.textItemQuantity.setText(quantityText);

        holder.buttonEdit.setOnClickListener(v -> listener.onEditClicked(item));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    // ViewHolder: holds references to one row's views
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemQuantity;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}