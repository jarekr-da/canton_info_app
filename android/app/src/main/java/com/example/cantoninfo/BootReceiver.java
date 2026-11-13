package com.example.cantoninfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Launches the app after the device boots.
 *
 * Android 10+ blocks activity starts from the background, including from boot receivers,
 * unless the app holds the "display over other apps" (SYSTEM_ALERT_WINDOW) permission.
 * Grant it once over adb:
 *   adb shell appops set com.example.cantoninfo SYSTEM_ALERT_WINDOW allow
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "CantonInfoBoot";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!Intent.ACTION_BOOT_COMPLETED.equals(action)
                && !"android.intent.action.QUICKBOOT_POWERON".equals(action)
                && !"com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(context)) {
            Log.w(TAG, "SYSTEM_ALERT_WINDOW not granted; Android may block the launch on boot. "
                    + "Run: adb shell appops set " + context.getPackageName() + " SYSTEM_ALERT_WINDOW allow");
        }

        Intent launch = new Intent(context, MainActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(launch);
        Log.i(TAG, "Launched MainActivity after " + action);
    }
}
