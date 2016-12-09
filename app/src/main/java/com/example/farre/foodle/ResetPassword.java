package com.example.farre.foodle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    //defining view objects
    private EditText EmailAddress;
    private Button Reset, CancelButton;
    //defining firebaseauth object
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //initializing views
        EmailAddress = (EditText) findViewById(R.id.email_input);
        Reset = (Button) findViewById(R.id.Reset_button);
        CancelButton = (Button) findViewById(R.id.cancel_button);

        //Getting firebase authentication
        auth = FirebaseAuth.getInstance();

        CancelButton.setOnClickListener(this);
        Reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == CancelButton) {
            finish();
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if(view == Reset) {
            String email = EmailAddress.getText().toString().trim();
            //if the user hasn't entered anything in the email box
            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(ResetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(ResetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }
    }
}