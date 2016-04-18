package com.example.dips.smartscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CompleteTask extends AppCompatActivity {

    private Bitmap curimage;
    private ArrayList imageList = new ArrayList();
    private List<Bitmap> completedImages; // just the ones that were added on this screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);

        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        int taskID = prefs.getInt("eventID", 1);

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        String[] listGroupItems = dbhelper.GetTaskInfo(taskID);
        ((TextView) findViewById(R.id.completeTaskTitle)).setText(listGroupItems[0]);
        ((TextView) findViewById(R.id.completeTaskDesc)).setText(listGroupItems[1]);
        ((TextView) findViewById(R.id.completeTaskDue)).setText(listGroupItems[2]);

        //get todays date
        Calendar calendar = Calendar.getInstance();
        String finishDate = calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR);

        ((TextView) findViewById(R.id.completeTaskFinish)).setText(finishDate);

        //GET PICTURES
        List<Bitmap> images = dbhelper.GetImages(taskID);
        for ( Bitmap image : images){
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(image);
            iv.setPadding(10, 10, 10, 10);
            ((LinearLayout) findViewById(R.id.completeTaskImageLayout)).addView(iv);
        }
    }

    public void UploadImage(View view){

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                curimage = (BitmapFactory.decodeFile(picturePath));
                Log.w("CompleteTask", picturePath + "");
            }
            //if image is selected
            if(curimage != null){
                curimage = Bitmap.createScaledBitmap(curimage, 500, 500, true);
                ImageView iv = new ImageView(this);
                iv.setImageBitmap(curimage);
                iv.setPadding(10,10, 10, 10);
                imageList.add(curimage);
                ((LinearLayout) findViewById(R.id.completeTaskImageLayout)).addView(iv);
            }

        }
    }

    public void CompleteTest(View view){
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        String comments = ((TextView) findViewById(R.id.completeTaskDesc)).getText().toString();
        int taskID = prefs.getInt("eventID", 1);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        if(dbhelper.completeTask(taskID,comments,imageList) == -1){
            Toast.makeText(this,"Error saving to database",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Task Completed",Toast.LENGTH_SHORT).show();
        backToViewTasks();
    }

    public void backToViewTasks (){
        Intent intent = new Intent(getApplicationContext(), ViewTask.class);
        startActivity(intent);
    }
}
