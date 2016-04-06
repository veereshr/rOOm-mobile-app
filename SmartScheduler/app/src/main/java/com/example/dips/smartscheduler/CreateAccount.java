package com.example.dips.smartscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //TODO get users phonenumber
    }

    public void CreateAccount(View v) {
        //getdata
        String pNum = "";
        String pass = "";
        String passCon = "";
        String fName = "";
        String lName = "";
        String email = "";
        pNum = ((TextView) findViewById(R.id.phoneText)).getText().toString();
        pass = ((TextView) findViewById(R.id.passText)).getText().toString();
        passCon = ((TextView) findViewById(R.id.passConText)).getText().toString();
        fName = ((TextView) findViewById(R.id.fNameText)).getText().toString();
        lName = ((TextView) findViewById(R.id.lNameText)).getText().toString();
        email = ((TextView) findViewById(R.id.emailText)).getText().toString();

        //validate Data
        //Removes all non numbers from phonenumber
        pNum = pNum.replaceAll("[^\\d.]", "");
        ((TextView) findViewById(R.id.phoneText)).setText(pNum);
        //check phonenumber length
        if (pNum.length() < 1) {
            Toast.makeText(this, "Phone number can not be empty", Toast.LENGTH_SHORT).show();
        } else {
            //check if password match
            if (!pass.equals(passCon)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                //TODO CHECK IF USER ALREADY EXIST

                //save to db
                DatabaseHelper dbhelper = new DatabaseHelper(this);
                if(dbhelper.InsertNewUser(pNum, pass, fName, lName, email) != -1){
                    Toast.makeText(v.getContext(), "Account Created", Toast.LENGTH_SHORT).show();

                    //passing intent
                    Intent intent = new Intent(v.getContext(), LogIn.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(v.getContext(), "Error saving to database", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }
}
