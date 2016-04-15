package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void CreateGroup (View view){

        String name = ((TextView) findViewById(R.id.edtTextGroupName)).getText().toString();
        String desc = ((TextView) findViewById(R.id.edtTextGroupDesc)).getText().toString();
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int phoneNumber = prefs.getInt("phoneNumber", 1);

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        //save to db
        if(dbhelper.CreateGroup(name, desc, phoneNumber + "") != -1){
            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();

            //passing intent
            Intent intent = new Intent(getApplicationContext(),GroupList.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Error saving to database", Toast.LENGTH_SHORT).show();
        }
    }
}
