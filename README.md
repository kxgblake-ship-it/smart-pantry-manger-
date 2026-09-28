# Smart Pantry Manager

A Java Android app that helps reduce food waste by suggesting recipes based strictly on the ingredients you already have at home. No shopping trip required.

Built for **Mobile App Development 700 (Practical Assignment)**, Richfield Graduate Institute of Technology.

## What it does

- **Pantry management:** add, edit and delete ingredients (name, quantity, unit and an optional expiry date), with input validation on the form.
- **Suggested Recipes:** lists only the recipes you can make right now from your pantry.
- **Recipe Detail:** shows the full ingredient list and method for a recipe.
- **Settings:** a switch that highlights pantry items that are expired or expiring within 3 days.
- **Friendly empty states:** clear messages when the pantry is empty or no recipes match, instead of a blank screen.
- **Bottom navigation** between the Pantry, Recipes and Settings screens.

## The strict-matching rule

A recipe is suggested only if **every** ingredient it needs is in the pantry in at least the required quantity. If one ingredient is missing or short, the recipe is excluded. The matching copes with everyday messiness:

- upper/lower case and extra spaces
- simple plurals (egg / eggs, tomato / tomatoes)
- unit differences (g / kg, ml / l, unit / units / pieces)

## Database choice: SQLite

The app uses **SQLite** through `SQLiteOpenHelper`. I chose it because:

- it runs entirely on the device, so it needs no internet connection, accounts or backend server;
- data persists after the app is closed and reopened;
- plain SQL keeps every step visible, which makes the code easy to explain and test.

Tables: `pantry_items`, `recipes` and `recipe_ingredients` (a junction table linking each recipe to the ingredients it needs). 17 starter recipes are seeded on the first run.

## Tech

Java (no Kotlin), Android Studio, XML layouts, RecyclerView with custom adapters, Intents, SharedPreferences for the settings, Material Components. The app does not use maps, GPS or location services.

## Setup and run instructions

1. Install Android Studio.
2. Clone this repository (File > New > Project from Version Control, then paste the repository URL) or download it as a ZIP and open the folder.
3. Wait for the Gradle sync to finish.
4. Create an emulator in Device Manager (for example a Pixel phone with a recent Android version) or connect a phone with USB debugging turned on.
5. Press the green Run button.
6. To try the matching: add egg (2, unit), cheese (50, g) and butter (10, g) on the Pantry screen, then open the Recipes tab. Cheese Omelette should appear. Delete the butter and it disappears.

## Main classes

- `MainActivity`: Pantry List screen
- `AddEditIngredientActivity`: add/edit form with validation
- `SuggestedRecipeActivity`: recipes the user can make now
- `RecipeDetailActivity`: one recipe's ingredients and method
- `SettingsActivity`: expiry highlight switch
- `DatabaseHelper`: SQLite tables and CRUD methods
- `IngredientMatcher`: the strict-matching logic
- `SeedData`: the starter recipes
- `PantryAdapter` and `RecipeAdapter`: RecyclerView adapters
- `BottomNavHelper`: shared bottom navigation setup

## Author

Blake King, student number 402111106
GitHub repository: [paste your repository link here]