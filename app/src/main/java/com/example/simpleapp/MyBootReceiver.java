package com.example.simpleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!StrandhoggProtectionUtils.androidVersionVulnerableToStrandhogg()) {
            return;
        }
        Log.i("StrandHogg detection", "Called boot receiver");
        Intent i = new Intent(context, MinimizedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.putExtra("launchMinimized", "bootReceiver");
        context.startActivity(i);
    }
}