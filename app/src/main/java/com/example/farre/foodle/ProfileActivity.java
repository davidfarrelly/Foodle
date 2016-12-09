package com.example.farre.foodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
//import com.firebase.client.AuthData;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    public static String name;

    //view objects
    private EditText editTextname;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonSettings;
    private Button buttonSearch;
    private Button buttonViewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonViewRecipe = (Button) findViewById(R.id.ViewRecipe);
        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonViewRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == buttonSettings) {
            finish();
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if(view == buttonViewRecipe) {
            finish();
            startActivity(new Intent(this, SwipeActivity.class));
        }
        if(view == buttonSearch) {
            finish();
            startActivity(new Intent(this, UserListActivity.class));
        }
    }
}
