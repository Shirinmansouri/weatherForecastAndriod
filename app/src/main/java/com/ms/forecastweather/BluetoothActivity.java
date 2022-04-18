package com.ms.forecastweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv, bluetoothStatusText;
    ImageView mBlueTv;
    Button mOnBtn, mOffBtn, mDiscoveredBtn, mPairedBtn;
    Switch bluetoothStatus;
    ListView businessesList;

    BluetoothAdapter mBlueAdapter;
    ArrayAdapter businessesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Initialized Bluetooth Status switch
        bluetoothStatus = findViewById(R.id.switchBluetooth);

        // Bluetooth Status text view
        bluetoothStatusText = findViewById(R.id.bluetoothStatus);

        // Initialize the assign value
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.explore);



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
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        startActivity(new Intent(getApplicationContext(), SearchHistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // Change Bluetooth On/Off Status
        bluetoothStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    if (!mBlueAdapter.isEnabled()){
                        showToast("Turing On Bluetooth...");
                        // intent to on Bluetooth
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_ENABLE_BT);
                        bluetoothStatusText.setText("Bluetooth is One");
                    }
                    else {
                        showToast("Bluetooth is already on");
                        bluetoothStatusText.setText("Bluetooth is One");
                    }
                } else {
                    if (mBlueAdapter.isEnabled()){
                        mBlueAdapter.disable();
                        showToast("Turning Bluetooth Off");
                        bluetoothStatusText.setText("Bluetooth is Off");
//                    mBlueTv.setImageResource(R.drawable.ic_action_off);
                    } else {
                        showToast("Bluetooth is already off");
                        bluetoothStatusText.setText("Bluetooth is Off");
                    }
                }
            }
        });


//        mStatusBlueTv = findViewById(R.id.statusBluetoothTV);
//        mPairedTv = findViewById(R.id.pairedTv);
//        mBlueTv = findViewById(R.id.bluetoothTV);
//        mOnBtn = findViewById(R.id.onBtn);
//        mOffBtn = findViewById(R.id.offBtn);
//        mDiscoveredBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        // adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        businessesList = findViewById(R.id.businessesList);

        ArrayList<String> businessesArrayList = new ArrayList<>();
        ArrayList<String> businessesURL = new ArrayList<>();

        //                    ArrayAdapter businessesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, businessesArrayList);
        businessesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, businessesArrayList);

        businessesArrayAdapter.notifyDataSetChanged();

        businessesList.setAdapter(businessesArrayAdapter);

        Intent intent = new Intent(this, BusinessDetailsActivity.class);









        businessesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            intent.putExtra("mapType", mapType);
//                            intent.putExtra("Location", restaurants.get(i) + "," + lonLats.get(i));
//                businessesArrayAdapter.notifyDataSetChanged();

//                if (businessesArrayList.get(i).equals("2B:8C:1F:2A:0B:A9")){
//                    startActivity(intent);
//                }
                intent.putExtra("url", businessesURL.get(i));
                startActivity(intent);

            }
        });


//        // check if bluetooth is available
//        if (mBlueAdapter == null){
//            mStatusBlueTv.setText("Bluetooth is not available");
//        } else {
//            mStatusBlueTv.setText("Bluetooth is available");
//        }

        // set image according to the bluetooth status(on/off)
//        if (mBlueAdapter.isEnabled()){
//            mBlueTv.setImageResource(R.drawable.ic_action_on);
//        }
//        else {
//            mBlueTv.setImageResource(R.drawable.ic_action_off);
//        }

//        // on btn click
//        mOnBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!mBlueAdapter.isEnabled()){
//                     showToast("Turing On Bluetooth...");
//                     // intent to on Bluetooth
//                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(intent, REQUEST_ENABLE_BT);
//                }
//                else {
//                    showToast("Bluetooth is already on");
//                }
//            }
//        });
//        // Discover bluetooth btn
//        mDiscoveredBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!mBlueAdapter.isDiscovering()){
//                    showToast("Making your device discoverable");
//                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                    startActivityForResult(intent, REQUEST_ENABLE_BT);
//                }
//            }
//        });
//        // off btn click
//        mOffBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mBlueAdapter.isEnabled()){
//                    mBlueAdapter.disable();
//                    showToast("Turning Bluetooth Off");
////                    mBlueTv.setImageResource(R.drawable.ic_action_off);
//                } else {
//                    showToast("Bluetooth is already off");
//                }
//            }
//        });
        // get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()){
//                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    // Each attributes of Bluetooth device can be call using device. (like: device.getName()).
                    for (BluetoothDevice device: devices){
//                        mPairedTv.append("\nDevice" + device.getName() + "," + device.getAddress());

                        FirebaseFirestore.getInstance().collection("businesses").whereEqualTo("macaddress", device.getAddress())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                        Log.d("Document", documentSnapshot.get("url").toString());
                                        Toast.makeText(BluetoothActivity.this, "This is: " + documentSnapshot.get("name").toString(), Toast.LENGTH_LONG).show();
                                        businessesArrayList.add(documentSnapshot.get("name").toString());
                                        businessesURL.add(documentSnapshot.get("url").toString());
                                    }
                                }
                                else {
                                    Log.d("Document", "task was not Successful");
                                    Toast.makeText(BluetoothActivity.this, "ndlasndklasndklasndklans", Toast.LENGTH_LONG).show();
                                }
                                businessesArrayAdapter.notifyDataSetChanged();
                            }
                        });


                    }
                    businessesArrayAdapter.notifyDataSetChanged();
                }
                else {
                    // bluetooth is off so can not get paired devices
                    showToast("Turn on Bluetooth to get paired devices");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    // bluetooth is one
//                    mBlueTv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                } else {
                    // under denied to turn bluetooth on
                    showToast("couldn't turn on the bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // toast message function
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}