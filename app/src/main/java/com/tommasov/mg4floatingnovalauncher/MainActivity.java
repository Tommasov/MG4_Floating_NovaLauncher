package com.tommasov.mg4floatingnovalauncher;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startFloatingButtonService();
            }
        } else {
            startFloatingButtonService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Il permesso è stato concesso, avvia il servizio
                    startFloatingButtonService();
                } else {
                    // Permesso negato, mostra un messaggio all'utente
                    Log.e("FloatingButtonApp", "Overlay permission not granted");
                }
            }
        }
    }

    private void startFloatingButtonService() {
        Intent intent = new Intent(this, FloatingButtonService.class);
        startService(intent);
        Log.d("FloatingButtonApp", "FloatingButtonService started");
    }
}
