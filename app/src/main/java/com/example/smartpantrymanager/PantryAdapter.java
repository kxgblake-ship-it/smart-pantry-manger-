package com.example.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems;
    private final OnItemActionListener listener;
    private boolean highlightExpiring = false; // controlled by the Settings toggle

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

    // Lets MainActivity pass in the Settings toggle value
    public void setHighlightExpiring(boolean highlightExpiring) {
        this.highlightExpiring = highlightExpiring;
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

        // Expiry line: hidden if no date, otherwise shown (highlighted if the setting is on)
        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.textItemExpiry.setVisibility(View.GONE);
        } else {
            String label = "Expires: " + expiry;
            int color = Color.parseColor("#666666");

            if (highlightExpiring) {
                long daysLeft = daysUntil(expiry);
                if (daysLeft < 0) {
                    label = "Expired: " + expiry;
                    color = Color.parseColor("#CC0000");
                } else if (daysLeft <= 3) {
                    label = "Expiring soon: " + expiry;
                    color = Color.parseColor("#E65100");
                }
            }

            holder.textItemExpiry.setText(label);
            holder.textItemExpiry.setTextColor(color); // set every time: rows get recycled
            holder.textItemExpiry.setVisibility(View.VISIBLE);
        }

        holder.buttonEdit.setOnClickListener(v -> listener.onEditClicked(item));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    // Days from today until the expiry date (negative = already expired)
    private long daysUntil(String expiryDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            format.setLenient(false);
            Date expiry = format.parse(expiryDate);
            if (expiry == null) return Long.MAX_VALUE;

            // Today at midnight, so we compare whole days, not hours
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long diffMillis = expiry.getTime() - today.getTimeInMillis();
            return Math.round(diffMillis / (24.0 * 60 * 60 * 1000));
        } catch (ParseException e) {
            return Long.MAX_VALUE; // unreadable date: never flag it
        }
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    // ViewHolder: holds references to one row's views
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemQuantity;
        TextView textItemExpiry;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            textItemExpiry = itemView.findViewById(R.id.textItemExpiry);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}