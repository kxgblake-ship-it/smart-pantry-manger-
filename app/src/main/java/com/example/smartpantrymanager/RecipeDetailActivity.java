package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

// Recipe Detail screen: shows one recipe's full ingredient list and method.
// It receives only the recipe ID through the Intent and loads the rest from the database.
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle("Recipe Details");

        // Show a back arrow in the title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textName = findViewById(R.id.textDetailName);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textInstructions = findViewById(R.id.textDetailInstructions);

        // Receive the recipe ID sent by SuggestedRecipeActivity
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Recipe recipe = dbHelper.getRecipeById(recipeId);

        if (recipe == null) {
            textName.setText("Recipe not found");
            return;
        }

        textName.setText(recipe.getName());
        textInstructions.setText(recipe.getInstructions());

        // Build the ingredient list as one bulleted block of text
        List<RecipeIngredient> ingredients = dbHelper.getIngredientsForRecipe(recipeId);
        StringBuilder builder = new StringBuilder();
        for (RecipeIngredient ingredient : ingredients) {
            builder.append("• ")
                    .append(formatQuantity(ingredient.getRequiredQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }
        textIngredients.setText(builder.toString().trim());
    }

    // Called when the back arrow in the title bar is tapped
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Shows 2.0 as "2" but keeps 0.5 as "0.5", so it reads naturally
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }
        return String.valueOf(quantity);
    }
}