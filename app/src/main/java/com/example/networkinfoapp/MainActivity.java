package com.example.networkinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private boolean isWiFiEnabled = false;
    private boolean isTouchDown = false;

    private TextView textView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        Button button = findViewById(R.id.look);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        isTouchDown = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isTouchDown = false;
                        break;
                }
                updateInfoVisibility();
                return false;
            }
        });
        updateContent();
    }

    private void updateContent() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Objects.requireNonNull(manager);

        String text;
        if (manager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            text = "-";
            isWiFiEnabled = false;
        } else {
            WifiInfo info = manager.getConnectionInfo();
            int ipAddress = info.getIpAddress();
            text = convertIpAddress(ipAddress);
            isWiFiEnabled = true;
        }

        updateInfoVisibility();
        textView.setText(text);
    }

    private void updateInfoVisibility() {
        textView.setVisibility(
                (!isWiFiEnabled || isTouchDown) ? View.VISIBLE : View.INVISIBLE);
    }

    private String convertIpAddress(int ipAddress) {
        return String.format(Locale.JAPAN,
                "%02d.%02d.%02d.%02d",
                (ipAddress) & 0xff,
                (ipAddress>> 8) & 0xff,
                (ipAddress>>16) & 0xff,
                (ipAddress>>24) & 0xff);
    }
}
