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

    // Normalizes a unit string for comparison: lowercase, trim only (no plural stripping - units like "ml" shouldn't be touched)
    public static String normalizeUnit(String unit) {
        if (unit == null) return "";
        return unit.trim().toLowerCase();
    }

    // Checks whether the pantry contains enough of a single required ingredient
    public static boolean pantryHasEnoughOf(RecipeIngredient required, List<PantryItem> pantryItems) {
        String requiredNameNormalized = normalizeName(required.getIngredientName());
        String requiredUnitNormalized = normalizeUnit(required.getUnit());

        for (PantryItem pantryItem : pantryItems) {
            String pantryNameNormalized = normalizeName(pantryItem.getName());
            String pantryUnitNormalized = normalizeUnit(pantryItem.getUnit());

            boolean namesMatch = requiredNameNormalized.equals(pantryNameNormalized);
            boolean unitsMatch = requiredUnitNormalized.equals(pantryUnitNormalized);
            boolean enoughQuantity = pantryItem.getQuantity() >= required.getRequiredQuantity();

            if (namesMatch && unitsMatch && enoughQuantity) {
                return true;
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

    // Counts how many required ingredients are MISSING (used for optional "Almost There" stretch feature)
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