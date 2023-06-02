package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(view -> openSecondActivity());
    }

    @Override
    protected void onStop() {
        super.onStop();
        StrandhoggProtectionUtils.startForegroundDetectionService(this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        StrandhoggProtectionUtils.stopForegroundDetectionService(this.getApplicationContext());
    }

    public void openSecondActivity(){
        Intent intent = new Intent(this, SecondActivity.class);
        this.startActivity(intent);
    }
}
