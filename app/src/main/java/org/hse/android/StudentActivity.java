package org.hse.android;

import androidx.annotation.NonNull;
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
import java.util.TimeZone;

public class StudentActivity extends AppCompatActivity {

    private Date currentTime = new Date();
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;

    private final TimeZone timeZone = TimeZone.getTimeZone("GMT+5");
    private final String Tag = "StudentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final Spinner spinner = findViewById(R.id.spinner);
        List<Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = adapter.getItem(position);
                Log.d(Tag, "selected item: " + item);
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

    private final String[] groupName = {"ПИ", "БИ", "Э"};
    private final String[] admissionYear = {"20", "21", "22"};
    private final String[] numOfGroup = {"1", "2", "3"};

    private void initGroupList(List<StudentActivity.Group> groups){
        int id = 0;
        for (String i : groupName) {
            for (String j : admissionYear) {
                for (String k : numOfGroup) {
                    String name = String.join("-", i, j, k);
                    groups.add(new Group(id++, name));
                }
            }
        }
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
        status.setText(R.string.teacherActivity_textStatus);
        subject.setText(R.string.teacherActivity_textSubject);
        cabinet.setText(R.string.teacherActivity_textCabinet);
        corp.setText(R.string.teacherActivity_textCorp);
        teacher.setText(R.string.teacherActivity_textTeacher);
    }

    static class Group {
        private Integer id;
        private String name;

        public Group(Integer id, String name){
            this.id = id;
            this.name = name;
        }
        public Integer getId(){
            return id;
        }
        public void setId(Integer id){
            this.id = id;
        }
        @NonNull
        public String toString(){
            return name;
        }
        public String getName(){
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}