package com.ms.forecastweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.forecastweather.Adaptors.ListViewAdapter;
import com.ms.forecastweather.Models.WeatherModel;

import java.util.ArrayList;

public class SearchHistoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Paginator paginator;
    private Button nextBtn,preBtn;
    private  int currentPage=0 , totalPages=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        // Initialize the assign value
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.History);
        // Perform Item selectionListener
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
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        return true;
                }
                return false;
            }
        });



        ListView lstHistory=(ListView) findViewById(R.id.lstHistory);
        paginator=new Paginator();
        nextBtn=(Button)findViewById(R.id.IdBtnNext);
        preBtn=(Button)findViewById(R.id.IdBtnPre);
        preBtn.setEnabled(false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<WeatherModel> lstWeather=new ArrayList<WeatherModel>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                      for(DataSnapshot temp: postSnapshot.getChildren())
                      {
                          lstWeather.add(temp.getValue(WeatherModel.class));
                      }
                  //  lstWeather.add(postSnapshot.getValue(WeatherModel.class));

                }

                Paginator.TOTAL_NUM_ITEM=lstWeather.size();
                Paginator.ITEM_PER_PAGE=7;
                Paginator.LAST_PAGE=Paginator.TOTAL_NUM_ITEM/Paginator.ITEM_PER_PAGE;
                Paginator.ITEM_REMAINING=Paginator.TOTAL_NUM_ITEM%Paginator.ITEM_PER_PAGE;
                totalPages=Paginator.TOTAL_NUM_ITEM/Paginator.ITEM_PER_PAGE;
                Paginator.realData=lstWeather;
           ListViewAdapter listViewAdapter=new ListViewAdapter(SearchHistoryActivity.this,R.layout.history_report, paginator.generatePage(currentPage));
            lstHistory.setAdapter(listViewAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                ListViewAdapter listViewAdapter=new ListViewAdapter(SearchHistoryActivity.this,R.layout.history_report, paginator.generatePage(currentPage));
                lstHistory.setAdapter(listViewAdapter);
                toggleButtons();
            }
        });
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                ListViewAdapter listViewAdapter=new ListViewAdapter(SearchHistoryActivity.this,R.layout.history_report, paginator.generatePage(currentPage));
                lstHistory.setAdapter(listViewAdapter);
                toggleButtons();
            }
        });
    }
    private void toggleButtons()
    {
        if(currentPage==totalPages)
        {
            nextBtn.setEnabled(false);
            preBtn.setEnabled(false);
        }
        else if (currentPage==0)
        {
            preBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }
        else if (currentPage>=1 && currentPage <=Paginator.LAST_PAGE)
        {
            preBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }
    }
}