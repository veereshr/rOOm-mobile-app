package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        viewListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //TODO FILL THIS IN WITH REAL task DATA
                SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                //TODO NOTICE THAT WE START COUNTING FROM 1 NOT 0
                editor.putInt("eventID", 1);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ViewSingleTask.class);
                startActivity(intent);
            }
        });
    }

    public void BackToGroupList (View view){
        Intent intent = new Intent(getApplicationContext(), GroupList.class);
        startActivity(intent);
    }

    public void CreateAccount(View v){
        Intent intent = new Intent(v.getContext(),CreateTask.class);
        startActivity(intent);
    }

}

