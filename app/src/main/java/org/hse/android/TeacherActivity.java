package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class TeacherActivity extends AppCompatActivity {

    private Date currentTime = new Date();
    private final TimeZone timeZone = TimeZone.getTimeZone("GMT+5");
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        final Spinner spinner = findViewById(R.id.spinner);
        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = adapter.getItem(position);
                Log.d("TeacherActivity", "selected item: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time = findViewById(R.id.textTime);
        initTime();

        status = findViewById(R.id.textStatus);
        subject = findViewById(R.id.textSubject);
        cabinet = findViewById(R.id.textCabinet);
        corp = findViewById(R.id.textCorp);
        teacher = findViewById(R.id.textTeacher);

        initData();
    }

    private void initGroupList(List<StudentActivity.Group> groups){
        groups.add(new StudentActivity.Group(1, "Преподаватель 1"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 2"));
    }
    private void initTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        simpleDateFormat.setTimeZone(timeZone);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("ru", "RU"));
        dayFormat.setTimeZone(timeZone);

        String formattedTime = simpleDateFormat.format(currentTime);
        String formattedDay = dayFormat.format(currentTime);

        time.setText(String.join(" ", formattedTime, formattedDay.substring(0,1).toUpperCase() + formattedDay.substring(1)));
    }

    private void initData(){
        status.setText("Нет пар");
        subject.setText("Дисциплина");
        cabinet.setText("Кабинет");
        corp.setText("Корпус");
        teacher.setText("Преподаватель");
    }
}