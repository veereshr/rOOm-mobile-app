package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewSingleTaskCompleted extends AppCompatActivity {

    TextView tvDueDate;
    TextView tvStartDate;
    TextView tvDesc;
    TextView txtViewTask;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_task);
        findViewsById();
        fetchDetails();
    }

    private void fetchDetails() {
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int eventID = prefs.getInt("eventID", 0);
        String phoneNumber = prefs.getString("phoneNumber", null);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //checking for the user phone number
        if (phoneNumber != null) {
            if (eventID != 0) {
                //Fetching all the task details
                String[] eventDetails = dbHelper.GetTaskInfo(eventID);

                //Setting up all the textview fields
                txtViewTask.setText(eventDetails[0]);
                tvDesc.setText(eventDetails[1]);
                tvDueDate.setText(eventDetails[2]);
                tvStartDate.setText(eventDetails[3]);

                //get people
                ArrayList people = dbHelper.GetPeople(eventID);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, people);
                spinner.setAdapter(adapter);


                List<Bitmap> images = dbHelper.GetImages(eventID);
                for (Bitmap image : images) {
                    ImageView iv = new ImageView(this);
                    iv.setImageBitmap(image);
                    iv.setPadding(10, 10, 10, 10);
                    ((LinearLayout) findViewById(R.id.ViewTaskImageLayout)).addView(iv);
                }
            }
        }
    }

    private void findViewsById() {
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        txtViewTask = (TextView) findViewById(R.id.txtViewTask);
        spinner = (Spinner) findViewById(R.id.ViewTaskSpinner);

    }

    //Button to mark the task is completed
    public void completeTaskBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), CompleteTask.class);
        startActivity(intent);
    }


}
