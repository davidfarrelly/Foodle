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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SwipeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private DatabaseReference mRecipeReference;
    private DatabaseReference newRef;
    private DatabaseReference myRef3;

    private TextView editTextName;
    private TextView editTextCategory;
    private TextView viewRecipe;

    private ImageView profileImageView;

    private ImageButton Back;
    private ImageButton Like;
    private ImageButton Dislike;

    private DatabaseReference mRef;
    private DatabaseReference userRef;
    private Query veganref;
    private Query coeliacref;
    private Query vegref;
    private Query proteinref;
    private Query carbsref;



    String imgURL;
    String recipeKey;
    Map<String, Recipe> likedRecipes = new HashMap<>();
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        userRef = FirebaseDatabase.getInstance().getReference().child("user").child(user);


        mRecipeReference = FirebaseDatabase.getInstance().getReference().child("recipe");
        veganref = FirebaseDatabase.getInstance().getReference().child("recipe").orderByChild("Category").equalTo("Vegan");
        vegref = FirebaseDatabase.getInstance().getReference().child("recipe").orderByChild("Category").equalTo("Vegetarian");
        coeliacref = FirebaseDatabase.getInstance().getReference().child("recipe").orderByChild("Category").equalTo("Coeliac");
        proteinref = FirebaseDatabase.getInstance().getReference().child("recipe").orderByChild("Category").equalTo("High Protein");
        carbsref = FirebaseDatabase.getInstance().getReference().child("recipe").orderByChild("Category").equalTo("Low Carbs");



        viewRecipe = (TextView) findViewById(R.id.ViewRecipe);
        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextCategory = (TextView) findViewById(R.id.editTextCategory);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);

        viewRecipe.setOnClickListener(this);
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
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String vegan;
                        String coeliac;
                        String vegetarian;
                        String protein;
                        String carbs;
                        String yes = "true";
                         User user = dataSnapshot.getValue(User.class);
                            vegan = user.Vegan;
                            coeliac = user.Coeliac;
                            vegetarian = user.Vegetarian;
                            protein = user.Protein;
                            carbs=user.Carbs;
                            if (vegan.equals(yes)) {
                                loadVegan();
                            } else if (coeliac.equals(yes)) {
                                loadCoeliac();
                            } else if (vegetarian.equals(yes)) {
                                loadVegatarian();
                            } else if(protein.equals(yes)) {
                                loadProtein();
                            } else if(carbs.equals(yes)) {
                                loadCarbs();
                            }
                            else {
                                loadRecipes();
                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                userRef.addValueEventListener(userListener);
            }
        }, 1000);
    }
    public void loadRecipes() {
        ValueEventListener recipeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    if(recipeSnapshot.getValue() != null) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use defaul image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview
                        likedRecipes.put(recipeKey, recipe);
                    } else {
                        loadPlaceholder();
                    }
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
    public void loadVegan() {
        ValueEventListener veganlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    if(recipeSnapshot.getValue() != null) {

                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use defaul image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    } else {
                        loadPlaceholder();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        veganref.addValueEventListener(veganlistener);
    }

    public void loadCoeliac() {
        ValueEventListener coeliaclistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use defaul image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    }
                }
                else {
                    loadPlaceholder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        coeliacref.addValueEventListener(coeliaclistener);
    }

    public void loadVegatarian() {
        ValueEventListener veglistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    if(recipeSnapshot.getValue() != null) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use default image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    } else {
                        loadPlaceholder();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        vegref.addValueEventListener(veglistener);
    }

    public void loadProtein() {
        ValueEventListener proteinlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use defaul image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    }
                }
                else {
                    loadPlaceholder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        proteinref.addValueEventListener(proteinlistener);
    }

    public void loadCarbs() {
        ValueEventListener carbslistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeKey = recipeSnapshot.getKey();
                        editTextName.setText(recipe.Name + ", " + recipe.Calories);
                        editTextCategory.setText(recipe.Category);
                        imgURL = recipe.Img;

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        profileImageView.getLayoutParams().height = (height / 100) * 65;

                        Picasso.with(getApplicationContext()).load(imgURL)//download URL
                                .placeholder(R.drawable.placeholder)//use defaul image
                                .error(R.drawable.placeholder)//if failed
                                .into(profileImageView);//imageview

                        likedRecipes.put(recipeKey, recipe);
                    }
                }
                else {
                    loadPlaceholder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        carbsref.addValueEventListener(carbslistener);
    }

    public void loadPlaceholder() {
        Picasso.with(getApplicationContext()).load(R.drawable.placeholder)//download URL
                .placeholder(R.drawable.placeholder)//use defaul image
                .error(R.drawable.placeholder)//if failed
                .into(profileImageView);//imageview
    }

    public void addRecipe() {
        DatabaseReference likedRef = FirebaseDatabase.getInstance().getReference().child("recipe");

        likedRef.child(recipeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                Map<String, String> recipe1 = new HashMap<>();

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
        if(view == viewRecipe) {
            finish();
            Intent intent = new Intent(getBaseContext(), ViewRecipe.class);
            intent.putExtra("RECIPE_KEY", recipeKey);
            startActivity(intent);
        }
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