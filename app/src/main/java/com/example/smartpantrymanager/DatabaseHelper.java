package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table 1: pantry_items - what the user actually has at home
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)"; // nullable - not every item needs one

        // Table 2: recipes - the seeded recipe collection
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "instructions TEXT NOT NULL)";

        // Table 3: recipe_ingredients - junction table linking recipes to what they need
        String createRecipeIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "ingredient_name TEXT NOT NULL, " +
                "required_quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + TABLE_RECIPES + "(id))";

        db.execSQL(createPantryTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple approach for now: drop and recreate tables on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }
    // ---------- CREATE ----------
    public long insertPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate); // can be null

        long newRowId = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return newRowId; // returns -1 if insert failed
    }

    // ---------- READ (all items) ----------
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, "name ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
                String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));

                items.add(new PantryItem(id, name, quantity, unit, expiryDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    // ---------- UPDATE ----------
    public int updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        int rowsAffected = db.update(TABLE_PANTRY, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // ---------- DELETE ----------
    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // ---------- Insert a recipe (used by seed data) ----------
    public long insertRecipe(String name, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("instructions", instructions);

        long newRowId = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return newRowId;
    }

    // ---------- Insert a recipe ingredient requirement (used by seed data) ----------
    public long insertRecipeIngredient(int recipeId, String ingredientName, double requiredQuantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName);
        values.put("required_quantity", requiredQuantity);
        values.put("unit", unit);

        long newRowId = db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
        db.close();
        return newRowId;
    }

    // ---------- Get all recipes (without their ingredients attached yet) ----------
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, "name ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                recipes.add(new Recipe(id, name, instructions));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;
    }

    // ---------- Get the required ingredients for one specific recipe ----------
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                "recipe_id = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int recId = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
                String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name"));
                double requiredQty = cursor.getDouble(cursor.getColumnIndexOrThrow("required_quantity"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
                ingredients.add(new RecipeIngredient(id, recId, ingredientName, requiredQty, unit));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ingredients;
    }

    // ---------- Get all recipes WITH their ingredients attached ----------
// This is the method the Suggested Recipes screen will actually call
    public List<Recipe> getAllRecipesWithIngredients() {
        List<Recipe> recipes = getAllRecipes();
        for (Recipe recipe : recipes) {
            List<RecipeIngredient> ingredients = getIngredientsForRecipe(recipe.getId());
            recipe.setRequiredIngredients(ingredients);
        }
        return recipes;
    }

    // ---------- Check if recipes table is empty (used to decide whether to seed) ----------
    public boolean isRecipeTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RECIPES, null);
        boolean isEmpty = true;
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }
        cursor.close();
        db.close();
        return isEmpty;
    }

}