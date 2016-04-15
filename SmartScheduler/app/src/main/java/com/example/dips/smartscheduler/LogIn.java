package com.example.dips.smartscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
    EditText phnNumberTxt;
    EditText passTextLogin;
    int phnNumber;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewsById();

    }

    private void findViewsById() {
        phnNumberTxt= (EditText) findViewById(R.id.phoneTextLogIn);
        passTextLogin = (EditText) findViewById(R.id.passTextLogin);
    }

    public void LogIn(View v){

        //TODO FILL THIS IN WITH REAL USER DATA
try {
    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
    phnNumber = Integer.parseInt(phnNumberTxt.getText().toString());
    password = passTextLogin.getText().toString();
    DatabaseHelper dbhelper = new DatabaseHelper(this);
    String[] credentials = new String[] { phnNumber+"", password};
    if(dbhelper.checkIfValidUser(credentials)){
        editor.putInt("phoneNumber", phnNumber);
        editor.commit();
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }
    else{
        Toast.makeText(this,"User credential is invalid",Toast.LENGTH_SHORT).show();
    }




}catch(Exception e){
    Toast.makeText(getApplicationContext(),"Enter valid Login Credentials",Toast.LENGTH_LONG).show();
}
}

    public void CreateAccount(View v){
        Intent intent = new Intent(v.getContext(),CreateAccount.class);
        startActivity(intent);
    }


}