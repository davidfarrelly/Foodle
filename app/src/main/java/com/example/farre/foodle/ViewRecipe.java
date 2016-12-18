package com.example.farre.foodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ViewRecipe extends AppCompatActivity implements View.OnClickListener {

    private TextView nameView;
    private TextView ingredientsView;
    private TextView caloriesView;
    private TextView categoryView;
    private TextView directionsView;
    private ImageView recipeImageView;
    private ImageButton Back;

    private static final String TAG = "ViewRecipe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        nameView = (TextView) findViewById(R.id.nameView);
        ingredientsView = (TextView) findViewById(R.id.ingredientsView);
        caloriesView = (TextView) findViewById(R.id.caloriesView);
        categoryView = (TextView) findViewById(R.id.categoryView);
        directionsView = (TextView) findViewById(R.id.directionsView);
        recipeImageView = (ImageView) findViewById(R.id.recipeImageView);
        Back = (ImageButton) findViewById(R.id.Back);
        Back.setOnClickListener(this);


        String s = getIntent().getStringExtra("recipeId");
        String t = getIntent().getStringExtra("RECIPE_KEY");
        if(s == null) {
            FirebaseDatabase.getInstance().getReference().child("recipe").child(t)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Recipe recipe = dataSnapshot.getValue(Recipe.class);

                            nameView.setText(recipe.Name);
                            ingredientsView.setText(recipe.Ingredients);
                            caloriesView.setText(recipe.Calories);
                            categoryView.setText(recipe.Category);
                            directionsView.setText(recipe.Directions);

                            String imgURL = recipe.Img;
                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                            int height = displaymetrics.heightPixels;
                            int width = displaymetrics.widthPixels;
                            recipeImageView.getLayoutParams().height = (height / 100) * 65;


                            Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                    .placeholder(R.drawable.thai_sweetfire_chicken)//use defaul image
                                    .error(R.drawable.thai_sweetfire_chicken)//if failed
                                    .into(recipeImageView);//imageview
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else {
            FirebaseDatabase.getInstance().getReference().child("likedrecipe").child(s)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Recipe recipe = dataSnapshot.getValue(Recipe.class);

                            nameView.setText(recipe.Name);
                            ingredientsView.setText(recipe.Ingredients);
                            caloriesView.setText(recipe.Calories);
                            categoryView.setText(recipe.Category);
                            directionsView.setText(recipe.Directions);

                            String imgURL = recipe.Img;
                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                            int height = displaymetrics.heightPixels;
                            recipeImageView.getLayoutParams().height = (height / 100) * 65;

                            Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                    .placeholder(R.drawable.placeholder)//use defaul image
                                    .error(R.drawable.placeholder)//if failed
                                    .into(recipeImageView);//imageview
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        if(v == Back) {
            ViewRecipe.this.finish();
            startActivity(new Intent(this, UserListActivity.class));
        }
    }
}
