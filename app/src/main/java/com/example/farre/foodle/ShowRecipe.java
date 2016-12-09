package com.example.farre.foodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowRecipe extends AppCompatActivity {

    private TextView recipeName;
    private TextView recipeCalories;
    private TextView recipeCategory;
    private TextView recipeIngredients;
    private TextView recipeDirections;
    private ImageView recipeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        recipeName = (TextView) findViewById(R.id.recipeName);
        String s = getIntent().getStringExtra("RECIPE_KEY");
        recipeName.setText(s);
    }
}
