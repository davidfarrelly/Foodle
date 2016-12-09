package com.example.farre.foodle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RecipeBook extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayout;

    private static final String TAG = "RecipeBook";
    private DatabaseReference mRecipeReference;

    private ImageButton Back;
    String recipeKey;
    Map<String, Recipe> recipeMap = new HashMap<String, Recipe>();
    private Context mContext;

    LinearLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__book);

        recipeMap.clear();


        mRecipeReference = FirebaseDatabase.getInstance().getReference().child("likedrecipe");
        linearLayout = (LinearLayout) findViewById(R.id.rl);
        Back = (ImageButton) findViewById(R.id.Back);
        Back.setOnClickListener(this);
        linearLayout.removeAllViewsInLayout();


        ValueEventListener recipeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    recipeKey = recipeSnapshot.getKey();
                    recipe.id = recipeKey;

                    recipeMap.put(recipeKey, recipe);
                    displayRecipes(recipeMap);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mRecipeReference.addValueEventListener(recipeListener);
    }

    public void displayRecipes(final Map<String, Recipe> list) {
        for(Recipe recipe : list.values()) {
            final String recName = recipe.Name;
            final String recId = recipe.id;

            // Get the application context
            mContext = getApplicationContext();
            // Get the widgets reference from XML layout
            mRelativeLayout = (LinearLayout) findViewById(R.id.rl);
            // Initialize a new CardView
            CardView card = new CardView(mContext);
            // Set the CardView layoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            card.setLayoutParams(params);

            card.setRadius(9);
            card.setContentPadding(15, 15, 15, 15);
            card.setCardBackgroundColor(Color.parseColor("#f1f1f1"));
            card.setMaxCardElevation(15);
            card.setCardElevation(9);
            card.setUseCompatPadding(true);

            // Initialize a new TextView to put in CardView
            final TextView tv = new TextView(mContext);
            tv.setLayoutParams(params);
            tv.setText(recName);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tv.setTextColor(Color.BLACK);
            card.addView(tv);

            // Finally, add the CardView in root layout
            mRelativeLayout.addView(card);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                        //startActivity(new Intent(RecipeBook.this, ProfileActivity.class));
                    Intent intent = new Intent(getBaseContext(), ShowRecipe.class);
                    intent.putExtra("RECIPE_KEY", recId);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v == Back) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

    }
}