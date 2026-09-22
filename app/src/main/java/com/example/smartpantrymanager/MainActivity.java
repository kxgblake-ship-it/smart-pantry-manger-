package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        // Seed the database with starter recipes on first run only
        if (dbHelper.isRecipeTableEmpty()) {
            SeedData.populate(dbHelper);
        }

        recyclerView = findViewById(R.id.recyclerPantry);
        textEmptyState = findViewById(R.id.textEmptyState);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start with an empty list - loadPantryItems() will fill it in onResume()
        adapter = new PantryAdapter(dbHelper.getAllPantryItems(), this);
        recyclerView.setAdapter(adapter);

        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // refresh every time this screen becomes visible
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter.updateData(items);

        // Show/hide the empty-state message depending on whether we have any items
        if (items.isEmpty()) {
            textEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

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
        loadPantryItems(); // refresh the list immediately after deleting
    }
}