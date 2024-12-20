package com.tommasov.mg4floatingnovalauncher;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class FloatingButtonService extends Service {
    private static final String CHANNEL_ID = "FloatingButtonServiceChannel";
    private WindowManager windowManager;
    private View floatingButton;

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();

        checkOverlayPermission();

        if (Settings.canDrawOverlays(this)) {
            createNotificationChannel();
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("MG4 Nova Launcher Floating Button Service")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ismart_launcher)
                    .build();
            startForeground(1, notification);

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            floatingButton = LayoutInflater.from(this).inflate(R.layout.layout_floating_button, null);

            int layoutFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutFlags = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutFlags = WindowManager.LayoutParams.TYPE_PHONE;
            }

            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    layoutFlags,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.RIGHT;
            params.x = 0;
            params.y = 0;

            windowManager.addView(floatingButton, params);

            floatingButton.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchY;
                private static final int CLICK_ACTION_THRESHOLD = 10;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            floatingButton.setPressed(true);
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            int deltaY = (int) (event.getRawY() - initialTouchY);

                            if (Math.abs(deltaY) > CLICK_ACTION_THRESHOLD) {
                                params.y = initialY + deltaY;
                                windowManager.updateViewLayout(floatingButton, params);
                            }
                            return true;

                        case MotionEvent.ACTION_UP:
                            floatingButton.setPressed(false);

                            if (Math.abs(event.getRawY() - initialTouchY) <= CLICK_ACTION_THRESHOLD) {
                                floatingButton.performClick();
                            }

                            return true;

                        case MotionEvent.ACTION_CANCEL:
                            floatingButton.setPressed(false);
                            return true;
                    }
                    return false;
                }
            });


            floatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.teslacoilsw.launcher");
                    if (intent != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(FloatingButtonService.this, "Nova Launcher not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingButton != null) windowManager.removeView(floatingButton);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Floating Button Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please enable overlay permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent launchIntent = new Intent(this, SplashActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }
    }
}
