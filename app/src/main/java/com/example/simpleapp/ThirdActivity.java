package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrandhoggProtectionUtils.registerCreateActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(view -> openFourthActivity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StrandhoggProtectionUtils.registerDestroyActivity();
    }

    public void openFourthActivity(){
        Intent intent = new Intent(this, FourthActivity.class);
        this.startActivity(intent);
    }
}
