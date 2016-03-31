package com.example.dips.smartscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

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
                    "eventID INTEGER PRIMARY KEY," +
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
                    "groupID INTEGER PRIMARY KEY," +
                    "groupName TEXT," +
                    "groupDesp TEXT);";

    public DatabaseHelper(Context context){
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
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
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
    public void InsertNewUser(String phoneNumber, String password, String fName, String lName, String email){
        try
        {
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
        }catch(Exception e){
            Log.i(LOGTAG, "Failed to InsertNewUser " + e.toString());
        }
    }
}
