package com.example.farre.foodle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RecipeBook";
    private DatabaseReference mRecipeReference;
    ValueEventListener mRecipeListener;
    RecipeAdapter adapter;
    private RecyclerView mRecipeRecycler;
    private ImageButton Backbutton;

    String recipeKey;
    ArrayList<Recipe> recipeMap = new ArrayList<>();
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mRecipeRecycler = (RecyclerView) findViewById(R.id.rvRecipes);
        Backbutton = (ImageButton) findViewById(R.id.Backbutton);
        Backbutton.setOnClickListener(this);

        mRecipeReference = FirebaseDatabase.getInstance().getReference().child("likedrecipe");
        mRecipeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener recipeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    recipeKey = recipeSnapshot.getKey();
                    recipe.id = recipeKey;
                    name = recipe.Name;
                    recipeMap.add(recipe);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mRecipeReference.addValueEventListener(recipeListener);

        mRecipeListener = recipeListener;

        // Listen for comments
        adapter = new RecipeAdapter(this, mRecipeReference);
        mRecipeRecycler.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mRecipeListener!= null) {
            mRecipeReference.removeEventListener(mRecipeListener);
        }

        //RecipeAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        if(v == Backbutton) {
            startActivity(new Intent(this, ProfileActivity.class));

        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        Button messageButton;
        private String id;

        RecipeViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
            messageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(messageButton.getContext() , ViewRecipe.class);
            // here pass id through intent
            intent.putExtra("recipeId" , id);
            messageButton.getContext().startActivity(intent);
        }
    }

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

        private List<Recipe> mRecipes = new ArrayList<>();
        private List<String> mRecipeIds = new ArrayList<>();

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        RecipeAdapter(Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;


            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    Recipe recipe = dataSnapshot.getValue(Recipe.class);

                    recipeKey = dataSnapshot.getKey();
                    recipe.id = recipeKey;
                    mRecipeIds.add(recipe.id);
                    mRecipes.add(recipe);
                    notifyItemInserted(mRecipes.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    Recipe newrecipe = dataSnapshot.getValue(Recipe.class);
                    String recipeKey = dataSnapshot.getKey();

                    int recipeIndex = mRecipeIds.indexOf(recipeKey);
                    if (recipeIndex > -1) {
                        mRecipes.set(recipeIndex, newrecipe);

                        notifyItemChanged(recipeIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + recipeKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    String recipeKey = dataSnapshot.getKey();

                    int recipeIndex = mRecipeIds.indexOf(recipeKey);
                    if (recipeIndex > -1) {
                        mRecipeIds.remove(recipeIndex);
                        mRecipes.remove(recipeIndex);

                        notifyItemRemoved(recipeIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + recipeKey);
                    }

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postRecipes:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;
        }


        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.nameTextView.setText(recipe.Name);
            holder.messageButton.setText("View");
            holder.id = mRecipeIds.get(position);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }
    }
}