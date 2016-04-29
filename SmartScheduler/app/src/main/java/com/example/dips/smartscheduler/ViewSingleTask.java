package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewSingleTask extends AppCompatActivity {


    Button btCompleteTask;
    TextView tvUser;
    TextView tvDueDate;
    TextView tvStartDate;
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
        String phoneNumber = prefs.getString("phoneNumber", null);
        DatabaseHelper dbHelper=new DatabaseHelper(this);

        //checking for the user phone number
        if (phoneNumber != null) {
            if (position != -1){
                //Fetching all the task details
                String[] eventDetails=dbHelper.getTaskDetails(phoneNumber, position);
                if(eventDetails!=null){
                    //Setting up all the textview fields
                    txtViewTask.setText(eventDetails[0]);
                    tvDesc.setText(eventDetails[1]);
                    tvDueDate.setText(eventDetails[2]);
                    tvStartDate.setText(eventDetails[3]);
                    tvUser.setText(eventDetails[4]);
                    eventID = Integer.parseInt(eventDetails[5]);
                    Log.i("Event id",eventID+"");

                    List<Bitmap> images = dbHelper.GetImages(eventID);
                    for ( Bitmap image : images){
                        ImageView iv = new ImageView(this);
                        iv.setImageBitmap(image);
                        iv.setPadding(10, 10, 10, 10);
                        ((LinearLayout) findViewById(R.id.layoutShowPictures)).addView(iv);
                    }
                }

            }
        }
        Toast.makeText(this,position+""+phoneNumber,Toast.LENGTH_SHORT).show();
    }

    //Fetching all the layout items
    int eventID;
    private void findViewsById() {
        btCompleteTask = (Button) findViewById(R.id.btCompleteTask);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        txtViewTask = (TextView) findViewById(R.id.txtViewTask);


    }

    //Button to mark the task is completed
    public void completeTaskBtn(View view) {


        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Data", 0x0000).edit();
        editor.putInt("eventID", eventID);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), CompleteTask.class);
        startActivity(intent);
    }


}
