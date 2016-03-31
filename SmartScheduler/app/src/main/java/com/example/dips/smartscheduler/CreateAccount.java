package com.example.dips.smartscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void CreateAccount(View v){
        //getdata
        String pNum = ((TextView)findViewById(R.id.phoneText)).getText().toString();
        String pass = ((TextView)findViewById(R.id.passText)).getText().toString();
        String passCon = ((TextView)findViewById(R.id.passConText)).getText().toString();
        String fName = ((TextView)findViewById(R.id.fNameText)).getText().toString();
        String lName = ((TextView)findViewById(R.id.lNameText)).getText().toString();
        String email = ((TextView)findViewById(R.id.emailText)).getText().toString();

        //validate Data
        //TODO check if phone number
        //TODO confirm that password and passwordConfirm all both the same

        pNum = "773-397-8220";
        pass = "PassWord";
        fName = "Jorge";
        lName = "Martinez";
        email = "ME@ME.com";

        //save to db
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        dbhelper.InsertNewUser(pNum,pass,fName,lName,email);

        //passing intent
        Intent intent = new Intent(v.getContext(),GroupList.class);
        startActivity(intent);
    }
}
