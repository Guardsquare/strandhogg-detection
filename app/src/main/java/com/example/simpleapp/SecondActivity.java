package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrandhoggProtectionUtils.registerCreateActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(view -> openThirdActivity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StrandhoggProtectionUtils.registerDestroyActivity();
    }

    public void openThirdActivity(){
        Intent intent = new Intent(this, ThirdActivity.class);
        this.startActivity(intent);
    }
}