package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private android.widget.TextView textNoMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle("Suggested Recipes");

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerRecipes);
        textNoMatches = findViewById(R.id.textNoMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Recipe> allRecipes = dbHelper.getAllRecipesWithIngredients();
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();

        // Apply the strict-matching rule: only keep recipes where EVERY ingredient is satisfied
        List<Recipe> suggestedRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (IngredientMatcher.canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }

        RecipeAdapter adapter = new RecipeAdapter(suggestedRecipes, recipe -> {
            // We'll connect this to Recipe Detail screen next
        });
        recyclerView.setAdapter(adapter);

        if (suggestedRecipes.isEmpty()) {
            textNoMatches.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoMatches.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}