package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    public static final String EXTRA_ITEM_NAME = "extra_item_name";
    public static final String EXTRA_ITEM_QUANTITY = "extra_item_quantity";
    public static final String EXTRA_ITEM_UNIT = "extra_item_unit";
    public static final String EXTRA_ITEM_EXPIRY = "extra_item_expiry";

    private EditText editName, editQuantity, editUnit, editExpiryDate;
    private TextView textError;
    private DatabaseHelper dbHelper;

    private int editingItemId = -1; // -1 means we're ADDING a new item, not editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        textError = findViewById(R.id.textError);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Check if we were launched in EDIT mode (an item ID was passed in)
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            editingItemId = intent.getIntExtra(EXTRA_ITEM_ID, -1);
            // Pre-fill the form with the existing item's data
            editName.setText(intent.getStringExtra(EXTRA_ITEM_NAME));
            editQuantity.setText(String.valueOf(intent.getDoubleExtra(EXTRA_ITEM_QUANTITY, 0)));
            editUnit.setText(intent.getStringExtra(EXTRA_ITEM_UNIT));
            editExpiryDate.setText(intent.getStringExtra(EXTRA_ITEM_EXPIRY));
            setTitle("Edit Ingredient");
        } else {
            setTitle("Add Ingredient");
        }

        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiryDate = editExpiryDate.getText().toString().trim();

        // ---------- VALIDATION ----------
        if (name.isEmpty()) {
            showError("Ingredient name is required.");
            return;
        }
        if (quantityText.isEmpty()) {
            showError("Quantity is required.");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.");
            return;
        }
        if (quantity <= 0) {
            showError("Quantity must be greater than zero.");
            return;
        }
        if (unit.isEmpty()) {
            showError("Unit is required (e.g. g, ml, unit).");
            return;
        }

        // Expiry date is optional - store null if left blank
        String expiryToSave = expiryDate.isEmpty() ? null : expiryDate;

        // ---------- SAVE (insert or update depending on mode) ----------
        if (editingItemId == -1) {
            dbHelper.insertPantryItem(name, quantity, unit, expiryToSave);
        } else {
            dbHelper.updatePantryItem(editingItemId, name, quantity, unit, expiryToSave);
        }

        finish(); // close this screen, return to MainActivity
    }

    private void showError(String message) {
        textError.setText(message);
        textError.setVisibility(View.VISIBLE);
    }
}