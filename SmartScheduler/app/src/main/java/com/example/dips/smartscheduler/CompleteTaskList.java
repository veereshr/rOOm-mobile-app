package com.example.dips.smartscheduler;

import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompleteTaskList extends Fragment {

    public static FragmentActivity CmpltTaskFragActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fetch phoneNumber through SharedPreferences
        RelativeLayout CmpltTaskListTabRelLayout = (RelativeLayout) inflater.inflate(R.layout.activity_complete_task_list, container, false);
        CmpltTaskFragActivity = (FragmentActivity) super.getActivity();

        SharedPreferences prefs = CmpltTaskFragActivity.getSharedPreferences("Data", 0x0000);
        String phoneNumber = prefs.getString("phoneNumber", "1");

        Log.d("Phone Number :", phoneNumber);
        //create DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());

        String[] eventNames = dbHelper.getCmpltTaskList(phoneNumber);

        Log.d("event Names Size :", String.valueOf(eventNames.length));

        //set NOTASKDATA text view visibilty property
        TextView txtNoCmpltTask = (TextView) CmpltTaskListTabRelLayout.findViewById(R.id.txtViewCmpltTaskData);
        if (eventNames.length == 0) {
            Log.d("Event Size:", String.valueOf(eventNames.length));
            txtNoCmpltTask.setVisibility(View.VISIBLE);
        }

        //populate list
        ListView viewListTask = (ListView) CmpltTaskListTabRelLayout.findViewById(R.id.listViewCmpltTaskList);
        List<String> eventList = new ArrayList<>();


        //add eventNames to the list
        for (int i = 0; i < eventNames.length; i++) {
            eventList.add(eventNames[i]);
            Log.d("Event Names:", eventList.get(i));
        }

        ArrayAdapter tasksAdapter = new ArrayAdapter<String>(CmpltTaskFragActivity.getApplicationContext(), android.R.layout.simple_expandable_list_item_1, eventList);
        viewListTask.setAdapter(tasksAdapter);
        viewListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                SharedPreferences.Editor editor = CmpltTaskFragActivity.getSharedPreferences("Data", 0x0000).edit();
                editor.putInt("eventID", 1);
                editor.putInt("position", position);
                editor.commit();
                Intent intent = new Intent(CmpltTaskFragActivity.getApplicationContext(), ViewSingleTask.class);
                startActivity(intent);
            }
        });
        tasksAdapter.notifyDataSetChanged();

        return CmpltTaskListTabRelLayout;
    }
}
