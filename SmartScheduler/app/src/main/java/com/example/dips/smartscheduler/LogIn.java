package com.example.dips.smartscheduler;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
    EditText phnNumberTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

       phnNumberTxt= (EditText) findViewById(R.id.phoneTextLogIn);
    }

    public void LogIn(View v){

        //TODO FILL THIS IN WITH REAL USER DATA
try {
    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
    int phnNumber = Integer.parseInt(phnNumberTxt.getText().toString());
    editor.putInt("phoneNumber", phnNumber);
    editor.commit();

    Intent intent = new Intent(v.getContext(), GroupList.class);
    startActivity(intent);
}catch(Exception e){
    Toast.makeText(getApplicationContext(),"Enter valid Login Credentials",Toast.LENGTH_LONG).show();
}
}

    public void CreateAccount(View v){
        Intent intent = new Intent(v.getContext(),CreateAccount.class);
        startActivity(intent);
    }
}