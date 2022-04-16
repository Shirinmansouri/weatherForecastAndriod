package com.ms.forecastweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Please enter you username and password", Toast.LENGTH_LONG).show();
                } else if(txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Please enter a password with more than 6 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    registerUser(txt_email, txt_password);
                }

//                startActivity(new Intent(RegisterActivity.this, SettingsActivity.class));
                //In order to stop user to use back button to come back to this screen we use finish()
                finish();
            }
        });
    }

    public void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registering User Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, SettingsActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}