package com.example.lab2_mobiledevelopment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    //private static final String TAG = "MainActivity";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(savedInstanceState == null){
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
    }

//    public void maps(View view) {
//        Intent redirect = new Intent(MainActivity.this,MapsActivity.class);
//        startActivity(redirect);
//    }

    public void contacts(View view) {
        Intent redirect = new Intent(MainActivity.this,ContactsActivity.class);
        startActivity(redirect);
    }

}
