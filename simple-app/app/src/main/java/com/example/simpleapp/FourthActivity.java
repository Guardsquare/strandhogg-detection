package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrandhoggProtectionUtils.registerCreateActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StrandhoggProtectionUtils.registerDestroyActivity();
    }
}