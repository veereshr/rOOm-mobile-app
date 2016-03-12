package com.example.dips.smartscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void LogIn(View v){
        Intent intent = new Intent(v.getContext(),GroupList.class);
        startActivity(intent);
    }

    public void CreateAccount(View v){
        Intent intent = new Intent(v.getContext(),CreateAccount.class);
        startActivity(intent);
    }
}