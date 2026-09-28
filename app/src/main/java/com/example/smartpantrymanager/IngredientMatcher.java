package com.example.smartpantrymanager;

import java.util.List;

public class IngredientMatcher {

    // Normalizes an ingredient name for comparison:
    // lowercase, trim whitespace, and strip a simple trailing "s" for basic plural handling
    public static String normalizeName(String name) {
        if (name == null) return "";
        String normalized = name.trim().toLowerCase();
        if (normalized.endsWith("s") && normalized.length() > 1) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    // Normalizes a unit string for comparison: lowercase, trim only
    public static String normalizeUnit(String unit) {
        if (unit == null) return "";
        return unit.trim().toLowerCase();
    }

    // Maps the many ways a unit can be written onto one base unit:
    // weight -> "g", volume -> "ml", countable things -> "unit"
    // Anything we don't recognise is left as-is (so it only matches itself)
    public static String baseUnit(String unit) {
        String u = normalizeUnit(unit);
        switch (u) {
            case "g": case "gram": case "grams":
            case "kg": case "kgs": case "kilogram": case "kilograms":
                return "g";
            case "ml": case "millilitre": case "millilitres":
            case "milliliter": case "milliliters":
            case "l": case "litre": case "litres": case "liter": case "liters":
                return "ml";
            case "unit": case "units": case "piece": case "pieces": case "pc": case "pcs":
                return "unit";
            default:
                return u;
        }
    }

    // Converts a quantity into its base unit (e.g. 0.5 kg -> 500 g, 2 l -> 2000 ml)
    public static double toBaseQuantity(double quantity, String unit) {
        String u = normalizeUnit(unit);
        switch (u) {
            case "kg": case "kgs": case "kilogram": case "kilograms":
            case "l": case "litre": case "litres": case "liter": case "liters":
                return quantity * 1000;
            default:
                return quantity;
        }
    }

    // Checks whether the pantry contains enough of a single required ingredient
    public static boolean pantryHasEnoughOf(RecipeIngredient required, List<PantryItem> pantryItems) {
        String requiredName = normalizeName(required.getIngredientName());
        String requiredBaseUnit = baseUnit(required.getUnit());
        double requiredAmount = toBaseQuantity(required.getRequiredQuantity(), required.getUnit());

        for (PantryItem pantryItem : pantryItems) {
            boolean namesMatch = requiredName.equals(normalizeName(pantryItem.getName()));
            boolean unitsMatch = requiredBaseUnit.equals(baseUnit(pantryItem.getUnit()));

            if (namesMatch && unitsMatch) {
                double pantryAmount = toBaseQuantity(pantryItem.getQuantity(), pantryItem.getUnit());
                if (pantryAmount >= requiredAmount) {
                    return true;
                }
            }
        }
        return false; // no matching pantry item found with enough quantity
    }

    // THE CORE RULE: a recipe only qualifies if EVERY required ingredient is satisfied
    public static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        List<RecipeIngredient> required = recipe.getRequiredIngredients();

        if (required == null || required.isEmpty()) {
            return false; // a recipe with no defined ingredients can't be "makeable"
        }

        for (RecipeIngredient ingredient : required) {
            if (!pantryHasEnoughOf(ingredient, pantryItems)) {
                return false; // even ONE missing/insufficient ingredient disqualifies the whole recipe
            }
        }
        return true; // every single required ingredient was found in sufficient quantity
    }

    // Counts how many required ingredients are MISSING (used for the optional "Almost There" stretch)
    public static int countMissingIngredients(Recipe recipe, List<PantryItem> pantryItems) {
        List<RecipeIngredient> required = recipe.getRequiredIngredients();
        if (required == null) return 0;

        int missingCount = 0;
        for (RecipeIngredient ingredient : required) {
            if (!pantryHasEnoughOf(ingredient, pantryItems)) {
                missingCount++;
            }
        }
        return missingCount;
    }
}