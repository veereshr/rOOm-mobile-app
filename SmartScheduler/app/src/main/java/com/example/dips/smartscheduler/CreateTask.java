package com.example.dips.smartscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


      //  ExpandableListView myListView= (ExpandableListView) findViewById(R.id.expandableListView);

       // String[] groups=new String[]{"Room:314,College,GymBuddies"};
        List<String> listGroupItems=new ArrayList<String>();

        listGroupItems.add("Room:314");
        listGroupItems.add("Room:315");
        listGroupItems.add("Room:316");
        listGroupItems.add("Room:317");

        Spinner dropdown = (Spinner)findViewById(R.id.dropDownGroupName);
       // String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapterGroupName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listGroupItems);
        dropdown.setAdapter(adapterGroupName);

        ListView myFriendList=(ListView)findViewById(R.id.friendsListView);

        myFriendList.setChoiceMode(myFriendList.CHOICE_MODE_MULTIPLE);

        myFriendList.setTextFilterEnabled(true);


        List<String> listFriendItems=new ArrayList<String>();

        listFriendItems.add("Veeresh");
        listFriendItems.add("Jorge");
        listFriendItems.add("Yamini");
        listFriendItems.add("Harsh");

        ArrayAdapter<String> adapterFriendName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listFriendItems);
        myFriendList.setAdapter(adapterFriendName);

        Button btnCreateTask=(Button)findViewById(R.id.btnCreateTask);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ViewTask.class);
                startActivity(i);
            }
        });
    }
}
