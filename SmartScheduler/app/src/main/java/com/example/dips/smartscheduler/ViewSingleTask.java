package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewSingleTask extends AppCompatActivity {


    Button btCompleteTask;
    TextView tvUser;
    TextView tvDate;
    TextView tvDesc;
    TextView txtViewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_task);
        findViewsById();
        fetchDetails();
    }

    private void fetchDetails() {
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int position = prefs.getInt("position", -1);
        int phoneNumber = prefs.getInt("phoneNumber", 0);
        DatabaseHelper dbHelper=new DatabaseHelper(this);

        //checking for the user phone number
        if (phoneNumber != 0) {
            if (position != -1){
                //Fetching all the task details
                String[] eventDetails=dbHelper.getTaskDetails(phoneNumber, position);
                if(eventDetails!=null){
                    //Setting up all the textview fields
                    txtViewTask.setText(eventDetails[0]);
                    tvDesc.setText(eventDetails[1]);
                    tvUser.setText(eventDetails[2]);
                    tvDate.setText(eventDetails[3]);
                }
            }
        }
    }

    //Fetching all the layout items
    private void findViewsById() {
        btCompleteTask = (Button) findViewById(R.id.btCompleteTask);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        txtViewTask = (TextView) findViewById(R.id.txtViewTask);


    }

    //Button to mark the task is completed
    public void completeTaskBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), CompleteTask.class);
        startActivity(intent);
    }


}
