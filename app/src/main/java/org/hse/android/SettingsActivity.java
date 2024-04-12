package org.hse.android;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private ImageView imageView;
    private static final String IMAGE_FILE_NAME = "photo.jpg";
    private File imageFile;
    private Bitmap imageBitmap;
    private static final String PREF_TEXT_KEY = "saved_text";
    private TextInputEditText textInputEditText;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorLight = findViewById(R.id.textView3);

        printSensorList();

        takePictureEvent();

        textInputEditText = findViewById(R.id.name);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadTextFromSharedPreferences();
    }
    private void takePictureEvent(){
        Button btnOpenCamera = findViewById(R.id.takePhoto);
        Button btnSave = findViewById(R.id.save);
        imageView = findViewById(R.id.imageView);

        loadImageFromStorage();
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent();
                } else {
                    requestCameraPermission();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextToSharedPreferences();
                if (imageBitmap != null) {
                    saveImageToFile();
                }
            }
        });
    }
    private void saveTextToSharedPreferences() {
        String text = textInputEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TEXT_KEY, text);
        editor.apply();
    }

    private void loadTextFromSharedPreferences() {
        String savedText = sharedPreferences.getString(PREF_TEXT_KEY, "");
        textInputEditText.setText(savedText);
    }
    private void printSensorList(){
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<String> sensorNames = new ArrayList<>();
        for (Sensor currentSensor : sensorList) {
            sensorNames.add(currentSensor.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);

        ListView listView = findViewById(R.id.sensor_listview);
        listView.setAdapter(adapter);
    }
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }
    private void saveImageToFile() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, IMAGE_FILE_NAME);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("tag", "", e);
        }
    }
    private void loadImageFromStorage() {
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_FILE_NAME);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            }
        } catch (FileNotFoundException e) {
            Log.e("tag", "", e);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorLight.setText(String.format("%.2f " + this.getString(R.string.settingsActivity_lux), lux));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}