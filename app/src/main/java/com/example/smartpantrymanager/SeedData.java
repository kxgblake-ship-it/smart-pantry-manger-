package com.example.smartpantrymanager;

public class SeedData {

    // Called once, only if the recipes table is empty (checked in DatabaseHelper)
    public static void populate(DatabaseHelper dbHelper) {

        addRecipe(dbHelper, "Cheese Omelette",
                "Whisk eggs, pour into a hot pan with butter, sprinkle cheese, fold and serve.",
                new Object[][]{
                        {"egg", 2.0, "unit"},
                        {"cheese", 50.0, "g"},
                        {"butter", 10.0, "g"}
                });

        addRecipe(dbHelper, "Garlic Fried Rice",
                "Fry garlic in oil until golden, add cooked rice, stir-fry until heated through.",
                new Object[][]{
                        {"rice", 200.0, "g"},
                        {"garlic", 2.0, "unit"},
                        {"oil", 15.0, "ml"}
                });

        addRecipe(dbHelper, "Tomato Onion Pasta",
                "Saute onion and garlic, add chopped tomato, simmer, mix with cooked pasta.",
                new Object[][]{
                        {"pasta", 150.0, "g"},
                        {"tomato", 2.0, "unit"},
                        {"onion", 1.0, "unit"},
                        {"garlic", 1.0, "unit"}
                });

        addRecipe(dbHelper, "Cheese Toast",
                "Butter bread, top with cheese, grill until golden and melted.",
                new Object[][]{
                        {"bread", 2.0, "slice"},
                        {"cheese", 40.0, "g"},
                        {"butter", 5.0, "g"}
                });

        addRecipe(dbHelper, "Vegetable Rice Bowl",
                "Steam rice, saute onion, garlic and tomato, combine and serve warm.",
                new Object[][]{
                        {"rice", 200.0, "g"},
                        {"onion", 1.0, "unit"},
                        {"garlic", 1.0, "unit"},
                        {"tomato", 1.0, "unit"}
                });

        addRecipe(dbHelper, "Milk Pancakes",
                "Mix flour, milk and egg into a batter, fry spoonfuls on a hot pan until golden.",
                new Object[][]{
                        {"flour", 150.0, "g"},
                        {"milk", 200.0, "ml"},
                        {"egg", 1.0, "unit"}
                });

        addRecipe(dbHelper, "Onion Garlic Soup",
                "Saute onion and garlic in butter, add stock, simmer 15 minutes, blend if desired.",
                new Object[][]{
                        {"onion", 2.0, "unit"},
                        {"garlic", 2.0, "unit"},
                        {"butter", 10.0, "g"},
                        {"stock", 500.0, "ml"}
                });

        addRecipe(dbHelper, "Egg Fried Rice",
                "Scramble egg in a hot pan, add cooked rice and garlic, stir-fry together.",
                new Object[][]{
                        {"egg", 2.0, "unit"},
                        {"rice", 200.0, "g"},
                        {"garlic", 1.0, "unit"}
                });

        addRecipe(dbHelper, "Tomato Cheese Bread",
                "Top bread slices with sliced tomato and cheese, grill until melted.",
                new Object[][]{
                        {"bread", 2.0, "slice"},
                        {"tomato", 1.0, "unit"},
                        {"cheese", 40.0, "g"}
                });

        addRecipe(dbHelper, "Buttered Pasta",
                "Cook pasta, toss with melted butter and a pinch of salt.",
                new Object[][]{
                        {"pasta", 150.0, "g"},
                        {"butter", 15.0, "g"},
                        {"salt", 2.0, "g"}
                });

        addRecipe(dbHelper, "Milk Rice Pudding",
                "Simmer rice in milk on low heat, stirring occasionally, until thickened.",
                new Object[][]{
                        {"rice", 100.0, "g"},
                        {"milk", 400.0, "ml"}
                });

        addRecipe(dbHelper, "Garlic Butter Toast",
                "Mix softened butter with crushed garlic, spread on bread, grill until crisp.",
                new Object[][]{
                        {"bread", 2.0, "slice"},
                        {"butter", 15.0, "g"},
                        {"garlic", 1.0, "unit"}
                });

        addRecipe(dbHelper, "Cheesy Rice Bake",
                "Layer cooked rice with cheese, bake until melted and bubbling.",
                new Object[][]{
                        {"rice", 200.0, "g"},
                        {"cheese", 60.0, "g"}
                });

        addRecipe(dbHelper, "Tomato Egg Stir-fry",
                "Scramble egg, set aside, saute tomato until soft, combine and season.",
                new Object[][]{
                        {"egg", 2.0, "unit"},
                        {"tomato", 2.0, "unit"},
                        {"oil", 10.0, "ml"}
                });

        addRecipe(dbHelper, "Simple Pancakes",
                "Mix flour, milk and a pinch of salt into batter, fry until golden on both sides.",
                new Object[][]{
                        {"flour", 150.0, "g"},
                        {"milk", 180.0, "ml"},
                        {"salt", 1.0, "g"}
                });

        addRecipe(dbHelper, "Onion Cheese Omelette",
                "Whisk egg, saute onion until soft, pour egg over, top with cheese, fold and serve.",
                new Object[][]{
                        {"egg", 2.0, "unit"},
                        {"onion", 1.0, "unit"},
                        {"cheese", 30.0, "g"}
                });

        addRecipe(dbHelper, "Garlic Tomato Rice",
                "Fry garlic until fragrant, add chopped tomato and cooked rice, stir until combined.",
                new Object[][]{
                        {"rice", 200.0, "g"},
                        {"garlic", 2.0, "unit"},
                        {"tomato", 1.0, "unit"}
                });
    }

    // Helper method: inserts one recipe, then loops through its ingredients and inserts each one
    private static void addRecipe(DatabaseHelper dbHelper, String name, String instructions, Object[][] ingredients) {
        long recipeId = dbHelper.insertRecipe(name, instructions);

        for (Object[] ingredient : ingredients) {
            String ingredientName = (String) ingredient[0];
            double quantity = (Double) ingredient[1];
            String unit = (String) ingredient[2];
            dbHelper.insertRecipeIngredient((int) recipeId, ingredientName, quantity, unit);
        }
    }
}