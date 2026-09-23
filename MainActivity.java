package com.meuapp.localizador;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private boolean isTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("Iniciar Rastreamento");

        setContentView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    toggleService((Button) v);
                } else {
                    requestPermissions();
                }
            }
        });
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    private void toggleService(Button button) {
        Intent intent = new Intent(this, LocationService.class);
        if (!isTracking) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            button.setText("Parar Rastreamento");
            isTracking = true;
            Toast.makeText(this, "Serviço Iniciado", Toast.LENGTH_SHORT).show();
        } else {
            stopService(intent);
            button.setText("Iniciar Rastreamento");
            isTracking = false;
            Toast.makeText(this, "Serviço Parado", Toast.LENGTH_SHORT).show();
        }
    }
}
