package com.example.farre.foodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddRecipe extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private EditText editTextName;
    private EditText  editTextCalories;
    private EditText editTextIngredients;
    private EditText editTextDirections;
    private EditText editTextCategory;
    private EditText editImageURL;
    private ImageButton backButton3;

    private Button buttonAddRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextCalories = (EditText) findViewById(R.id.editTextCalories);
        editTextIngredients = (EditText) findViewById(R.id.editTextIngredients);
        editTextDirections = (EditText) findViewById(R.id.editTextDirections);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editImageURL = (EditText) findViewById(R.id.editImageURL);
        buttonAddRecipe = (Button) findViewById(R.id.buttonAddRecipe);
        buttonAddRecipe.setOnClickListener(this);
        backButton3 = (ImageButton) findViewById(R.id.backButton3);
        backButton3.setOnClickListener(this);

        myRef = FirebaseDatabase.getInstance().getReference();

    }

    public void addRecipe() {
        Map<String, String> recipe_map = new HashMap<String, String>();

        String name = editTextName.getText().toString().trim();
        String calories = editTextCalories.getText().toString().trim();
        String ingredients = editTextIngredients.getText().toString().trim();
        String directions = editTextDirections.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String URL = editImageURL.getText().toString().trim();

        recipe_map.put("Calories", calories);
        recipe_map.put("Category", category);
        recipe_map.put("Directions", directions);
        recipe_map.put("Ingredients", ingredients);
        recipe_map.put("Name", name);
        recipe_map.put("Img", URL);

        myRef = myRef.child("recipe");
        myRef.push().setValue(recipe_map);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAddRecipe) {
            addRecipe();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view == backButton3) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
    }
}
