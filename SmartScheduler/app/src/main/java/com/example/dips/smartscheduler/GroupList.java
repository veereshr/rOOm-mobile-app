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

public class GroupList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        //populate list
        ListView viewListTask=(ListView)findViewById(R.id.viewGroupListView);
        List<String> tasks=new ArrayList<>();
        tasks.add("Group 1");
        tasks.add("Group 2");
        tasks.add("Group 3");
        ArrayAdapter tasksAdapter= new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, tasks) ;
        viewListTask.setAdapter(tasksAdapter);
        //set on click true
        viewListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,long id) {

                //TODO FILL THIS IN WITH REAL Group DATA
                SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                //TODO NOTICE THAT WE START COUNTING FROM 1 NOT 0
                editor.putInt("groupID", 1);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ViewTask.class);
                startActivity(intent);
            }
        });
    }

    public void NewGroupBtn(View view){
        Intent intent = new Intent(getApplicationContext(), CreateGroup.class);
        startActivity(intent);
    }

    public void InvGroupBtn(View view){
        Intent intent = new Intent(getApplicationContext(), InviteToGroup.class);
        startActivity(intent);
    }
}
