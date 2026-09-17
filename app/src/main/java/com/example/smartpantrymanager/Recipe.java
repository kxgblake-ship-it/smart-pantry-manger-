package com.example.smartpantrymanager;

import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> requiredIngredients; // filled in when needed

    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<RecipeIngredient> getRequiredIngredients() {
        return requiredIngredients;
    }

    public void setRequiredIngredients(List<RecipeIngredient> requiredIngredients) {
        this.requiredIngredients = requiredIngredients;
    }
}