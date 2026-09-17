package com.example.smartpantrymanager;

public class PantryItem {

    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate; // nullable - not every item has one

    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters - other classes (like RecyclerView adapter) will use these
    // to read data out of a PantryItem object without touching the fields directly
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}