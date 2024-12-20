package com.tommasov.mg4floatingnovalauncher;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startFloatingButtonService();
        finish();
    }

    private void startFloatingButtonService() {
        Intent intent = new Intent(this, FloatingButtonService.class);
        startService(intent);
    }
}
