package com.example.dips.smartscheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTask extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private int yearNew, monthNew, dayNew;
    private Bitmap curimage;
    private ArrayList imageList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        //get todays date and put in select date text
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month, day);

        //get current group team members and fill spinner
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int groupID = prefs.getInt("groupID", 1);

        //TODO CONTACTS
        /*
        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
        List<String> listGroupItems = dbhelper.GetGroupMembersName(groupID);
        Spinner dropdown = (Spinner) findViewById(R.id.createTaskTeam);
        listGroupItems.add(0, "");
        ArrayAdapter<String> adapterGroupName = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, listGroupItems);
        dropdown.setAdapter(adapterGroupName);
        */
    }

    //used for Select Date
    @SuppressWarnings("deprecation")
    public void GetDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        yearNew = year;
        monthNew = month;
        dayNew = day;
        ((TextView) findViewById(R.id.createTaskDate)).setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }
    //end of Select Date

    //start of image upload
    public void UploadImage(View view) {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                curimage = null;
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    curimage = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        curimage.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                curimage = (BitmapFactory.decodeFile(picturePath));
                Log.w("CompleteTask", picturePath + "");
            }
            //if image is selected
            if (curimage != null) {
                curimage = Bitmap.createScaledBitmap(curimage, 500, 500, true);
                ImageView iv = new ImageView(this);
                iv.setImageBitmap(curimage);
                iv.setPadding(10, 10, 10, 10);
                imageList.add(curimage);
                ((LinearLayout) findViewById(R.id.createTaskImageLayout)).addView(iv);
            }

        }
    }
    //end of image upload

    public void CreateTask(View view) {

        String title = "";
        String desc = "";
        String assignedTo = "";
        String date = "";
        String startdate = month + "/" + day + "/" + year;

        title = ((TextView) findViewById(R.id.createTaskName)).getText().toString();
        desc = ((TextView) findViewById(R.id.createTaskDesc)).getText().toString();
        assignedTo = ((Spinner) findViewById(R.id.createTaskTeam)).getSelectedItem().toString();
        date = ((TextView) findViewById(R.id.createTaskDate)).getText().toString();

        if (title.equals("")) {
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (desc.equals("")) {
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(yearNew >= year && monthNew >= month && dayNew >= day)) {
            Toast.makeText(this, "Invalid complete by date", Toast.LENGTH_SHORT).show();
            return;
        }

        //get current group
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int groupID = prefs.getInt("groupID", 1);


        //CREATE EVENT IN DB
        int res;
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        res = dbhelper.InsertNewTask(groupID, title, desc, assignedTo, date, startdate, imageList);

        if (res != -1) {
            Toast.makeText(this, "Saved Task", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Error saving to database", Toast.LENGTH_SHORT).show();
        }
    }
}
