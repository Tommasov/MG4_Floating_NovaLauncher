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
            startFloatingButtonService(context);
        }
    }

    private void startFloatingButtonService(Context context) {
        Intent serviceIntent = new Intent(context, FloatingButtonService.class);
        context.startForegroundService(serviceIntent);
        Log.d("BootReceiver", "FloatingButtonService started");
    }
}