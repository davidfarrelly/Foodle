package com.example.farre.foodle;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private EditText editTextUsername;
    private Button buttonReset;
    private Button buttonDelete;
    private Button buttonAdd;
    private Button buttonSave;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Switch Coeliac;
    private Switch Diabetic;
    private Switch Vegan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        databaseReference = FirebaseDatabase.getInstance().getReference();


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
        Diabetic = (Switch) findViewById(R.id.Diabetic);
        Diabetic.setOnClickListener(this);
        Diabetic.setChecked(false);

        buttonSave.setOnClickListener(this);
        backButton.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Switch");
        alertDialog.setMessage("ON");

    }

    private void saveUserInformation() {
        //Getting values from database
        String name = editTextUsername.getText().toString().trim();
        String veganV = "false";
        String coeliacV = "false";
        String diabeticV = "false";

        if(Vegan.isChecked() == true) {
            veganV = "true";
        }
        if(Coeliac.isChecked() == true) {
            coeliacV = "true";
        }
        if(Diabetic.isChecked() == true) {
            diabeticV = "true";
        }

        //String add = editTextAddress.getText().toString().trim();


        User userInformation = new User(coeliacV, diabeticV, name, veganV);

        //getting the current logged in user
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //FirebaseUser user = firebaseAuth.getCurrentUser().getUid();


        databaseReference.child("user").child(user).setValue(userInformation);

        //displaying a success toast
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
        if(view == Diabetic) {
            Diabetic.setChecked(true);
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

