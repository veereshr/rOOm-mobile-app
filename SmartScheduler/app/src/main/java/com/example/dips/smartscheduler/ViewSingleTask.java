package com.example.dips.smartscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ViewSingleTask extends AppCompatActivity {


    Button btCompleteTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_task);
        findViewsById();



    }

    private void findViewsById() {
        btCompleteTask = (Button) findViewById(R.id.btCompleteTask);

    }

    public void completeTaskBtn(View view){
        Intent intent = new Intent(getApplicationContext(), CompleteTask.class);
        startActivity(intent);
    }
}
