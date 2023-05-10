package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MinimizedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("StrandHogg detection", "onCreate called");
        Intent intent = getIntent();
        if (intent.hasExtra("serviceMinimized")){ // Needed when service starts this activity. Clear the task, else onCreate isn't called when clicking the app icon
            Log.i("StrandHogg detection", "serviceMinimized set");
            this.finish();
            this.finishAffinity();
            this.finishAndRemoveTask();
            Intent i = new Intent(this.getApplicationContext(), MinimizedActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // Again needed in combination with moveTaskToBack(true) so that Android is forced to create a reference and onCreate is called upon clicking the app icon
            i.putExtra("launchMinimized", "resetTask");
            this.getApplicationContext().startActivity(i);
            return;
        }
        if(intent.hasExtra("launchMinimized")){ // Check if activity should be minimized to create the reference or we should restart the app for usage
            moveTaskToBack(true);
            Log.i("StrandHogg detection", "moved task to back");
        }else {
            Log.i("StrandHogg detection", "Checking for StrandHogg");
            if (!StrandhoggProtectionUtils.checkForAttack(this)) {
                Log.i("StrandHogg detection", "Did not detect StrandHogg");
                this.finish();
                this.finishAffinity();
                this.finishAndRemoveTask();
                if (StrandhoggProtectionUtils.androidVersionVulnerableToStrandhogg()) {
                    startService(new Intent(getBaseContext(), RestartMinimizedActivityService.class));
                }
                Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS); // Flag to make sure the app behaves like usual
                i.putExtra("restart", "frontRestart");
                this.getApplicationContext().startActivity(i);
            } else {
                Log.i("StrandHogg detection", "StrandHogg attack detected");
                StrandhoggProtectionUtils.respondToStrandhogg(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (
            getIntent().hasExtra("launchMinimized") &&
            StrandhoggProtectionUtils.androidVersionVulnerableToStrandhogg() &&
            StrandhoggProtectionUtils.systemKillsOtherTasksOnLaunch() &&
            StrandhoggProtectionUtils.getNumberOfRunningTasks(this) <= 1
        ) {
            Intent i = new Intent(this.getApplicationContext(), MinimizedActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.putExtra("launchMinimized", "resetTask");
            this.getApplicationContext().startActivity(i);
        }
    }
}