package com.example.dips.smartscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        ListView viewListTask=(ListView)findViewById(R.id.viewTaskListView);
        List<String> tasks=new ArrayList<String>();

        tasks.add("Cooking");
        tasks.add("Dish Wash");
        tasks.add("Room Cleaning");

        ArrayAdapter tasksAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, (List<String>) tasks) ;
        viewListTask.setAdapter(tasksAdapter);
    }
}

