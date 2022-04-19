package com.ms.forecastweather.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ms.forecastweather.R;

public class UserAuthenticationActivity extends AppCompatActivity {

    private Button register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            //Implement the event handler method
            public void onClick(View v) {
                startActivity(new Intent(UserAuthenticationActivity.this, RegisterActivity.class));

                //In order to stop user to use back button to come back to this screen we use finish()
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            //Implement the event handler method
            public void onClick(View v) {
                startActivity(new Intent(UserAuthenticationActivity.this, LoginActivity.class));

                //In order to stop user to use back button to come back to this screen we use finish()
                finish();
            }
        });
    }
}
