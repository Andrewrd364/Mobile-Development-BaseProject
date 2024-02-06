package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showStudent(); }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showTeacher(); }
        });
    }

    private void showStudent(){
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }
    private void showTeacher(){
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }
}