package com.example.dips.smartscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge on 3/31/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "OurDB";
    private static final String DATABASE_NAME = "Room.db";
    private static final int DATABASE_VERSION = 1;

    // SQL Statement to create UserTable.
    private static final String USERTABLE_CREATE =
            "CREATE TABLE UserTable( " +
                    "phoneNumber TEXT PRIMARY KEY," +
                    "password TEXT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT);";

    // SQL Statement to create UserGroupTable.
    private static final String USERGROUPTABLE_CREATE =
            "CREATE TABLE UserGroupTable( " +
                    "phoneNumber TEXT," +
                    "groupID INTEGER);";

    // SQL Statement to create GroupEventTable.
    private static final String GROUPEVENTTABLE_CREATE =
            "CREATE TABLE GroupEventTable( " +
                    "groupID INTEGER," +
                    "eventID INTEGER);";

    // SQL Statement to create EventTable.
    private static final String EVENTTABLE_CREATE =
            "CREATE TABLE EventTable( " +
                    "eventID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "eventTitle TEXT," +
                    "eventDescription TEXT," +
                    "dueDate TEXT," +
                    "startDate TEXT," +
                    "assignTo TEXT," +
                    "comment TEXT," +
                    "completedDate TEXT," +
                    "completedBy TEXT);";

    // SQL Statement to create EventPictureTable.
    private static final String EVENTPICTURETABLE_CREATE =
            "CREATE TABLE EventPictureTable( " +
                    "eventID INTEGER," +
                    "picture BLOB);";

    // SQL Statement to create GroupTable.
    private static final String GROUPTABLE_CREATE =
            "CREATE TABLE GroupTable( " +
                    "groupID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "groupName TEXT," +
                    "groupDesp TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create all tables
        db.execSQL(USERTABLE_CREATE);
        db.execSQL(USERGROUPTABLE_CREATE);
        db.execSQL(GROUPEVENTTABLE_CREATE);
        db.execSQL(EVENTTABLE_CREATE);
        db.execSQL(EVENTPICTURETABLE_CREATE);
        db.execSQL(GROUPTABLE_CREATE);
        Log.i(LOGTAG, "TABLES CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(LOGTAG, "Upgrading");
        //onUpgrade Drop all old tables and remake
        db.execSQL("DROP TABLE IF EXISTS " + USERTABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + USERGROUPTABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPEVENTTABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTTABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTPICTURETABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPTABLE_CREATE);
        onCreate(db);
    }

    //used in CreateAccount.java to make a new user
    public int InsertNewUser(String phoneNumber, String password, String fName, String lName, String email) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            //fill values to prevent injection
            ContentValues values = new ContentValues();
            values.put("phoneNumber", phoneNumber);
            values.put("password", password);
            values.put("firstName", fName);
            values.put("lastName", lName);
            values.put("email", email);

            // Inserting Row
            db.insert("UserTable", null, values);
            db.close(); // Closing database connection
            Log.i(LOGTAG, "Successfully InsertNewUser");
            return 1;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to InsertNewUser " + e.toString());
        }
        return -1;
    }

    public int InsertNewTask(int groupID, String title, String desc, String assignedTo, String date, String startDate, ArrayList imageList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            //fill values to prevent injection
            ContentValues values = new ContentValues();
            values.put("eventTitle", title);
            values.put("eventDescription", desc);
            values.put("assignTo", assignedTo);
            values.put("DueDate", date);
            values.put("StartDate", startDate);
            // Inserting Row
            db.insert("EventTable", null, values);

            //get the inserted events ID
            Cursor cursor = db.rawQuery("SELECT last_insert_rowid();", null);
            cursor.moveToFirst();
            int EventID = cursor.getInt(0);

            //add images
            for(int i = 0 ; i<imageList.size(); i++){
                //get byte array for image aka blob
                byte[] data = getBitmapAsByteArray((Bitmap) imageList.get(i));
                values = new ContentValues();
                values.put("eventID",EventID);
                values.put("picture",data);
                db.insert("EventPictureTable", null, values);
            }

            //add event to group
            values = new ContentValues();
            values.put("eventID",EventID);
            values.put("groupID",groupID);
            db.insert("GroupEventTable", null, values);

            db.close(); // Closing database connection
            Log.i(LOGTAG, "Successfully Inserted New task");
            return 1;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to Insert new task " + e.toString());
        }
        return -1;
    }

    public ArrayList<String> GetGroupMembersName(int groupID){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> al = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT firstName, LastName FROM UserTable " +
                                     "INNER JOIN UserGroupTable on Usertable.phoneNumber " +
                                    "= UserGroupTable.phoneNumber WHERE groupID = " + groupID, null);

        while(cursor.moveToNext()){
            al.add(cursor.getString(0) + " " + cursor.getString(1));
        }
        return al;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
