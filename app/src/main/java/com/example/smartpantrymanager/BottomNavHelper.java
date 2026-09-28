package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Shared bottom navigation bar setup, used by the three main screens
// (Pantry, Recipes, Settings) so the navigation code lives in one place.
public class BottomNavHelper {

    public static void setup(Activity activity, int selectedItemId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);

        // Highlight the tab for the screen we're currently on
        nav.setSelectedItemId(selectedItemId);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) {
                return true; // already on this screen, nothing to do
            }

            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = MainActivity.class;
            } else if (id == R.id.nav_recipes) {
                target = SuggestedRecipeActivity.class;
            } else {
                target = SettingsActivity.class;
            }

            Intent intent = new Intent(activity, target);
            // REORDER_TO_FRONT: if that screen already exists, bring it forward
            // instead of stacking a duplicate. NO_ANIMATION: tabs switch instantly.
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        });
    }
}