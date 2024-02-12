package org.hse.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
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