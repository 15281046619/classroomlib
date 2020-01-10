package com.xingwang.classroomlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xingwang.classroom.ClassRoomLibUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassRoomLibUtils.startListActivity(this,"栏目");

    }
}
