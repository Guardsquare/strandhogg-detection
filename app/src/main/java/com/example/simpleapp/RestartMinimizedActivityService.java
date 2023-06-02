package com.example.simpleapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RestartMinimizedActivityService extends Service {
    public RestartMinimizedActivityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override public void onTaskRemoved(Intent rootIntent) {
        this.stopSelf();
        StrandhoggProtectionUtils.stopForegroundDetectionService(this.getApplicationContext());
        Intent i = new Intent(this.getApplicationContext(), MinimizedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); //Needed to hide the activity and force Android to create the reference again
        i.putExtra("serviceMinimized", "detectionService");
        startActivity(i);
    }
}
