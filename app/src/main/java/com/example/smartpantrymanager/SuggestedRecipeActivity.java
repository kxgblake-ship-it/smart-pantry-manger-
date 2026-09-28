package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Suggested Recipes screen: lists only the recipes the user can make right now,
// using the strict-matching rule in IngredientMatcher.
public class SuggestedRecipeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TextView textNoMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle("Suggested Recipes");

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerRecipes);
        textNoMatches = findViewById(R.id.textNoMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavHelper.setup(this, R.id.nav_recipes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes(); // recalculate every time the screen appears
    }

    private void loadSuggestedRecipes() {
        List<Recipe> allRecipes = dbHelper.getAllRecipesWithIngredients();
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();

        // Strict rule: keep a recipe only if EVERY ingredient is satisfied
        List<Recipe> suggestedRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (IngredientMatcher.canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }

        // Tapping a recipe opens its detail screen, passing only the recipe ID
        RecipeAdapter adapter = new RecipeAdapter(suggestedRecipes, recipe -> {
            Intent intent = new Intent(SuggestedRecipeActivity.this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Friendly message instead of a blank screen when nothing matches
        if (suggestedRecipes.isEmpty()) {
            textNoMatches.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoMatches.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}