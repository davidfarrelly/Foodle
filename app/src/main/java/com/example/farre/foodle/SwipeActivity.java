package com.example.farre.foodle;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SwipeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private DatabaseReference mRecipeReference;
    private DatabaseReference newRef;
    private DatabaseReference myRef3;
    private DatabaseReference veganRef;

    private TextView editTextName;
    private TextView editTextCategory;

    private ImageView profileImageView;

    private ImageButton Back;
    private ImageButton Like;
    private ImageButton Dislike;

    private DatabaseReference mRef;
    private DatabaseReference databaseReference;
    private DatabaseReference userRef;

    String imgURL;
    String recipeKey;
    Map<String, Recipe> likedRecipes = new HashMap<String,Recipe>();
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String vegan = "false";
    String coeliac = "false";
    String diabetic = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        userRef = FirebaseDatabase.getInstance().getReference().child("user").child(user);


        mRecipeReference = FirebaseDatabase.getInstance().getReference().child("recipe");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextCategory = (TextView) findViewById(R.id.editTextCategory);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);

        Back = (ImageButton) findViewById(R.id.Back);
        Back.setOnClickListener(this);
        Like = (ImageButton) findViewById(R.id.Like);
        Like.setOnClickListener(this);
        Dislike = (ImageButton) findViewById(R.id.Dislike);
        Dislike.setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onStart() {
        super.onStart();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        vegan = user.Vegan; //Here I am retrieving the string from firebase database, which is either "yes" or "no"
                        coeliac = user.Coeliac;
                        diabetic = user.Diabetic;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                userRef.addValueEventListener(userListener);
            }
        }, 5000);

        if (vegan == "true") {
            veganRef = mRef.child("recipe");

            veganRef.orderByChild("Category").equalTo("Vegan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.thai_sweetfire_chicken)//use defaul image
                                .error(R.drawable.thai_sweetfire_chicken)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });
        } else {
            ValueEventListener recipeListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        int width = displaymetrics.widthPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.thai_sweetfire_chicken)//use defaul image
                                .error(R.drawable.thai_sweetfire_chicken)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mRecipeReference.addValueEventListener(recipeListener);
        }
    }

    public void addRecipe() {
        DatabaseReference likedRef = FirebaseDatabase.getInstance().getReference().child("recipe");


        likedRef.child(recipeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                Map<String, String> recipe1 = new HashMap<String, String>();

                Recipe recipe2 = snapshot.getValue(Recipe.class);

                String name = recipe2.Name;
                String calories = recipe2.Calories;
                String ingredients = recipe2.Ingredients;
                String directions = recipe2.Directions;
                String category = recipe2.Category;
                String imgUrl = recipe2.Img;

                recipe1.put("Calories", calories);
                recipe1.put("Category", category);
                recipe1.put("Directions", directions);
                recipe1.put("Img", imgUrl);
                recipe1.put("Ingredients", ingredients);
                recipe1.put("Name", name);

                myRef3 = mRef.child("likedrecipe");
                myRef3.push().setValue(recipe1);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == Back) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(view == Like) {
            addRecipe();
            newRef = FirebaseDatabase.getInstance().getReference().child("recipe");
            //Toast.makeText(SwipeActivity.this,recipeKey,Toast.LENGTH_LONG).show();
            newRef.child(recipeKey).removeValue();
        }
        if(view == Dislike) {
            newRef = FirebaseDatabase.getInstance().getReference().child("recipe");
            //Toast.makeText(SwipeActivity.this,recipeKey,Toast.LENGTH_LONG).show();
            newRef.child(recipeKey).removeValue();
    }
    }
}