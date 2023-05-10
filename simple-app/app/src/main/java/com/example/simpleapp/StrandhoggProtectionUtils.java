package com.example.simpleapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.ActivityManager.AppTask;
import static android.app.ActivityManager.RunningTaskInfo;

public final class StrandhoggProtectionUtils {

    private StrandhoggProtectionUtils() {
        throw new UnsupportedOperationException();
    }

    private static final String LAUNCHABLE_ACTIVITY_CLASS_NAME = MinimizedActivity.class.getName();
    private static final AtomicInteger NUM_ACTIVITIES_LIMIT = new AtomicInteger(1);

    public static boolean androidVersionVulnerableToStrandhogg() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.R;  // Only versions below 11 are vulnerable
    }

    public static boolean systemKillsOtherTasksOnLaunch() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static int getNumberOfRunningTasks(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningTasks(20).size();
    }

    public static boolean checkForAttack(Context context) {
        if (!androidVersionVulnerableToStrandhogg()) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(20);
        for (RunningTaskInfo task : taskList) {
            if (isStrandhoggTask(task, context)) {
                return true;
            }
        }
        return false;
    }

    public static void respondToStrandhogg(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (AppTask appTask : am.getAppTasks()) {
            appTask.finishAndRemoveTask();
        }
    }

    public static void startForegroundDetectionService(Context applicationContext) {
        if (!androidVersionVulnerableToStrandhogg()) {
            return;
        }
        Intent foregroundDetectionIntent =
                new Intent(applicationContext, ForegroundDetectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(foregroundDetectionIntent);
        } else {
            applicationContext.startService(foregroundDetectionIntent);
        }
    }

    public static void stopForegroundDetectionService(Context applicationContext) {
        if (!androidVersionVulnerableToStrandhogg()) {
            return;
        }
        Intent foregroundDetectionIntent =
                new Intent(applicationContext, ForegroundDetectionService.class);
        applicationContext.stopService(foregroundDetectionIntent);
    }

    public static void registerCreateActivity() {
        NUM_ACTIVITIES_LIMIT.incrementAndGet();
    }

    public static void registerDestroyActivity() {
        NUM_ACTIVITIES_LIMIT.decrementAndGet();
    }

    private static boolean isStrandhoggTask(RunningTaskInfo task,
                                            Context context) {
        Log.i("numActivities", "" + task.numActivities);

        if (!task.baseActivity.getPackageName().equals(context.getPackageName())) {
            return false;
        }

        return launchingFromLaunchActivity(task)
                ? task.numActivities > NUM_ACTIVITIES_LIMIT.get() + 1
                : task.numActivities > NUM_ACTIVITIES_LIMIT.get();
    }

    private static boolean launchingFromLaunchActivity(RunningTaskInfo task) {
        return LAUNCHABLE_ACTIVITY_CLASS_NAME.equals(task.topActivity.getClassName()) &&
               LAUNCHABLE_ACTIVITY_CLASS_NAME.equals(task.baseActivity.getClassName());
    }
}
