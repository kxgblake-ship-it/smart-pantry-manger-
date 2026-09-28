package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// Pantry List screen: shows every pantry item and lets the user add, edit and delete them.
public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Pantry");

        dbHelper = new DatabaseHelper(this);

        // Seed the starter recipes on first run only
        if (dbHelper.isRecipeTableEmpty()) {
            SeedData.populate(dbHelper);
        }

        recyclerView = findViewById(R.id.recyclerPantry);
        textEmptyState = findViewById(R.id.textEmptyState);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(dbHelper.getAllPantryItems(), this);
        recyclerView.setAdapter(adapter);

        // Add mode: open the form with no extras
        fabAddItem.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEditIngredientActivity.class)));

        BottomNavHelper.setup(this, R.id.nav_pantry);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Re-read the Settings toggle every time this screen appears,
        // so a change made in Settings shows up as soon as we come back
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        adapter.setHighlightExpiring(prefs.getBoolean(SettingsActivity.KEY_EXPIRY_ALERTS, false));

        loadPantryItems();
    }

    // Reloads the list from the database and shows/hides the empty-state message
    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter.updateData(items);

        if (items.isEmpty()) {
            textEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Edit mode: pack the item's data into the Intent so the form can pre-fill itself
    @Override
    public void onEditClicked(PantryItem item) {
        Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_NAME, item.getName());
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_QUANTITY, item.getQuantity());
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_UNIT, item.getUnit());
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_EXPIRY, item.getExpiryDate());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        dbHelper.deletePantryItem(item.getId());
        loadPantryItems(); // refresh immediately after deleting
    }
}