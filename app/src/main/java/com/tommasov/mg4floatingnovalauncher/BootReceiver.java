package com.tommasov.mg4floatingnovalauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(context)) {
                    startFloatingButtonService(context);
                } else {
                    Intent overlayPermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + context.getPackageName()));
                    overlayPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(overlayPermissionIntent);
                }
            } else {
                startFloatingButtonService(context);
            }
        }
    }

    private void startFloatingButtonService(Context context) {
        Intent serviceIntent = new Intent(context, FloatingButtonService.class);
        context.startForegroundService(serviceIntent);
        Log.d("BootReceiver", "FloatingButtonService started");
    }
}