package com.example.farre.foodle;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private EditText editTextUsername;
    private Button buttonReset;
    private Button buttonDelete;
    private Button buttonAdd;
    private Button buttonSave;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private Switch Coeliac;
    private Switch Vegetarian;
    private Switch Vegan;
    private Switch HighProtein;
    private Switch LowCarbs;
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "SettingsActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userReference = FirebaseDatabase.getInstance().getReference().child("user").child(user);
        backButton = (ImageButton) findViewById(R.id.backButton);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        buttonSave= (Button) findViewById(R.id.buttonSave);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        Vegan = (Switch) findViewById(R.id.Vegan);
        Vegan.setOnClickListener(this);
        Vegan.setChecked(false);

        Coeliac = (Switch) findViewById(R.id.Coeliac);
        Coeliac.setOnClickListener(this);
        Coeliac.setChecked(false);

        Vegetarian = (Switch) findViewById(R.id.Vegetarian);
        Vegetarian.setOnClickListener(this);
        Vegetarian.setChecked(false);

        HighProtein = (Switch) findViewById(R.id.HighProtein);
        HighProtein.setOnClickListener(this);
        HighProtein.setChecked(false);

        LowCarbs = (Switch) findViewById(R.id.LowCarb);
        LowCarbs.setOnClickListener(this);
        LowCarbs.setChecked(false);

        buttonSave.setOnClickListener(this);
        backButton.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Switch");
        alertDialog.setMessage("ON");

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void checkUser() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String vegan = "false";
                        String coeliac = "false";
                        String vegetarian = "false";
                        String highProtein = "false";
                        String lowCarbs = "false";

                        String userName;
                        String yes = "true";
                        String no = "false";

                        User user = dataSnapshot.getValue(User.class);
                        userName = user.Name;
                        vegan = user.Vegan;
                        coeliac = user.Coeliac;
                        vegetarian = user.Vegetarian;
                        highProtein = user.Protein;
                        lowCarbs = user.Carbs;

                        if(vegan.equals(yes)) {
                            Vegan.setChecked(true);
                        } else if(coeliac.equals(yes)){
                            Coeliac.setChecked(true);
                        } else if(vegetarian.equals(yes)) {
                            Vegetarian.setChecked(true);
                        } else if(highProtein.equals(yes)) {
                            HighProtein.setChecked(true);
                        } else if(lowCarbs.equals(yes)) {
                            LowCarbs.setChecked(true);
                        }
                        editTextUsername.setText(userName);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                userReference.addValueEventListener(userListener);
            }
        }, 100);
    }

    private void saveUserInformation() {
        //Getting values from database
        String name = editTextUsername.getText().toString().trim();
        String veganV = "false";
        String coeliacV = "false";
        String vegetarianV = "false";
        String HighProteinV = "false";
        String LowCarbV = "false";

        if(Vegan.isChecked() == true) {
            veganV = "true";
        } else if(Vegan.isChecked() == false) {
            veganV = "false";
        }

        if(Coeliac.isChecked() == true) {
            coeliacV = "true";
        } else if (Coeliac.isChecked() == false) {
            coeliacV = "false";
        }

        if(Vegetarian.isChecked() == true) {
            vegetarianV = "true";
        } else if(Vegetarian.isChecked() == false) {
            vegetarianV = "false";
        }

        if(HighProtein.isChecked() == true) {
            HighProteinV = "true";
        } else if(HighProtein.isChecked() == false) {
            HighProteinV = "false";
        }

        if(LowCarbs.isChecked() == true) {
            LowCarbV = "true";
        } else if(LowCarbs.isChecked() == false) {
            LowCarbV = "false";
        }

        User userInformation = new User(LowCarbV, coeliacV, HighProteinV, vegetarianV, name, veganV);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("user").child(user).setValue(userInformation);
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == Vegan) {
            Vegan.setChecked(true);
        }
        if(view == Coeliac) {
            Coeliac.setChecked(true);
        }
        if(view == Vegetarian) {
            Vegetarian.setChecked(true);
        }
        if(view == HighProtein) {
            HighProtein.setChecked(true);
        }
        if(view == LowCarbs) {
            LowCarbs.setChecked(true);
        }
        if(view == buttonSave) {
            saveUserInformation();
        }
        if(view == backButton) {
            finish();

            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(view == buttonReset) {
            finish();

            startActivity(new Intent(this, ResetPassword.class));
        }
        if(view == buttonAdd) {
            finish();

            startActivity(new Intent(this, AddRecipe.class));
        }
        if(view == buttonDelete) {
            firebaseAuth = FirebaseAuth.getInstance();
            final String TAG = "Tag";
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG,"OK! Works fine!");
                        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Log.w(TAG,"Something is wrong!");
                    }
                }
            });
        }
    }
}

