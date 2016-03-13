package com.example.dips.smartscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CompleteTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);
    }

    public void backToViewTasks (View view){
        Intent intent = new Intent(getApplicationContext(), ViewTask.class);
        startActivity(intent);
    }
}
