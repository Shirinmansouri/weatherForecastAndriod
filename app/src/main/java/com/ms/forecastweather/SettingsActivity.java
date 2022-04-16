package com.ms.forecastweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button goToHomeScreen;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // Initialize the assign value
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.setting);

        // Perform ItemselectionListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), BluetoothActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.setting:
                        return true;
                }
                return false;
            }
        });


        goToHomeScreen = findViewById(R.id.gotohomescreen);

        goToHomeScreen.setOnClickListener(new View.OnClickListener() {
            //Implement the event handler method
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));

                //In order to stop user to use back button to come back to this screen we use finish()
                finish();
            }
        });

        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            //Implement the event handler method
            public void onClick(View v) {
//                startActivity(new Intent(SettingsActivity.this, UserAuthenticationActivity.class));

                FirebaseAuth.getInstance().signOut();

                Toast.makeText(SettingsActivity.this, "Sign Out Successfully", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(SettingsActivity.this, UserAuthenticationActivity.class));
                startActivity(new Intent(SettingsActivity.this, UserAuthenticationActivity.class));

                //In order to stop user to use back button to come back to this screen we use finish()
//                finish();
            }
        });
    }
}