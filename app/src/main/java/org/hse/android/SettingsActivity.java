package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorLight = findViewById(R.id.textView3);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorLight.setText(String.format("%.2f lux", lux));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, light, sensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}