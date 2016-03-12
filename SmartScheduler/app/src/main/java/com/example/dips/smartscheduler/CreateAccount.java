package com.example.dips.smartscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void CreateAccount(View v){
        Intent intent = new Intent(v.getContext(),GroupList.class);
        startActivity(intent);
    }
}
