package com.example.simpleapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleObserver;

import java.util.Timer;
import java.util.TimerTask;

public class ForegroundDetectionService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID
            = "STRANDHOGG_FOREGROUND_DETECTION_NOTIFICATION_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 1;

    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Foreground service", "onStartCommand called");

        this.doStartForeground();
        this.scheduleDetection();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Foreground service", "onDestroy called");
        super.onDestroy();
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    private void doStartForeground() {
        Log.i("Foreground service", "doStartForeground called");
        this.initNotificationChannel();
        Notification notification = this.initNotification();
        this.startForeground(NOTIFICATION_ID, notification);
    }

    private void initNotificationChannel() {
        Log.i("Foreground service", "initNotificationChannel called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.protection_notification_channel_title);
            String description = getString(R.string.protection_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification initNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);
        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.foreground_detection_notification_title))
            .setContentText(getText(R.string.foreground_detection_notification_message))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build();
    }

    private void scheduleDetection() {
        Log.i("Foreground service", "scheduleDetection called");
        final long period = 2000;
        this.timer = new Timer();
        this.timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("Foreground service", "TimerTask running");
                        if (StrandhoggProtectionUtils.checkForAttack(ForegroundDetectionService.this)) {
                            Log.i("Foreground service", "StrandHogg attack detected");
                            StrandhoggProtectionUtils.respondToStrandhogg(ForegroundDetectionService.this);
                        }
                    }
                },
                0,
                period
        );
    }
}
