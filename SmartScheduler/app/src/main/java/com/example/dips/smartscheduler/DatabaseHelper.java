package com.example.dips.smartscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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



    //SQL Statement to get GroupNames Details.
    private static final String GROUPTABLE_DETAILS=
            "SELECT groupName FROM GroupTable " +
                    "INNER JOIN UserGroupTable on GroupTable.groupID = UserGroupTable.groupID "+
                    "WHERE phoneNumber=";



    //SQL Statement to get GroupNames Details.
    private static final String GROUPEVENTTABLE_DETAILS=
            "SELECT eventTitle from EventTable where eventID IN " +
                    "(SELECT eventID FROM GroupEventTable NATURAL JOIN UserGroupTable " +
                    "WHERE phoneNumber=";


    //SQL Statement to check the login credentials
    private static final String CHECK_LOGIN = "SELECT phoneNumber from UserTable where phoneNumber=";

    // SQL Statement to get Event Details
    private static final String EVENTTABLE_DETAILS=
            "SELECT eventTitle, eventDescription, assignTo, dueDate from EventTable where eventID IN " +
                    "(SELECT eventID FROM GroupEventTable NATURAL JOIN UserGroupTable " +
                    "WHERE phoneNumber=";

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

    //used in createAccount to check if user already exist
    public boolean checkIfUserExist(String pNum) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM UserTable WHERE phoneNumber = " + pNum, null);
            return cursor.moveToNext();
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to check if user exist " + e.toString());
        }
        return false;
    }

    //used in LogIn to check if user is valid
    public boolean checkIfValidUser(String[] credentials) {
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase();
            Log.i("Phone Number",credentials[1]);
            Cursor cursor = db.rawQuery("SELECT phoneNumber FROM UserTable WHERE phoneNumber = "+credentials[0]+" and password = "+credentials[1], null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to check if user is valid " + e.toString());
        }
        return false;
    }



    //used in createTask to build out task and add pictures and put in group
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
            for (int i = 0; i < imageList.size(); i++) {
                //get byte array for image aka blob
                byte[] data = getBitmapAsByteArray((Bitmap) imageList.get(i));
                values = new ContentValues();
                values.put("eventID", EventID);
                values.put("picture", data);
                db.insert("EventPictureTable", null, values);
            }

            //add event to group
            values = new ContentValues();
            values.put("eventID", EventID);
            values.put("groupID", groupID);
            db.insert("GroupEventTable", null, values);

            db.close(); // Closing database connection
            Log.i(LOGTAG, "Successfully Inserted New task");
            return 1;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to Insert new task " + e.toString());
        }
        return -1;
    }

    //used in createTask to populate groupMemberDropDown
    public ArrayList<String> GetGroupMembersName(int groupID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ArrayList<String> al = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT firstName, LastName FROM UserTable " +
                    "INNER JOIN UserGroupTable on Usertable.phoneNumber " +
                    "= UserGroupTable.phoneNumber WHERE groupID = " + groupID, null);

            while (cursor.moveToNext()) {
                al.add(cursor.getString(0) + " " + cursor.getString(1));
            }
            cursor.close();
            Log.i(LOGTAG, "successfully got group member ");
            return al;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to get group member " + e.toString());
        }
        return null;
    }

    //used in completeTask to return task data
    public String[] GetTaskInfo(int taskID) {
        try {
            String[] sa = new String[5];

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM EventTable where eventID = " + taskID, null);

            cursor.moveToFirst();
            sa[0] = cursor.getString(1);
            sa[1] = cursor.getString(2);
            sa[2] = cursor.getString(3);
            sa[3] = cursor.getString(4);
            sa[4] = cursor.getString(5);
            cursor.close();
            Log.i(LOGTAG, "Successfully Got Task Info");
            return sa;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to get TaskInfo " + e.toString());
            return null;
        }
    }

    public int completeTask(int EventID, String comments, ArrayList imageList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            SQLiteStatement insertStatement = db.compileStatement("UPDATE EventTable SET comment = ? WHERE eventID = ?");
            insertStatement.bindString(1, comments);
            insertStatement.bindString(2, EventID + "");
            insertStatement.executeInsert();

            //add images
            for (int i = 0; i < imageList.size(); i++) {
                //get byte array for image aka blob
                byte[] data = getBitmapAsByteArray((Bitmap) imageList.get(i));
                ContentValues values = new ContentValues();
                values.put("eventID", EventID);
                values.put("picture", data);
                db.insert("EventPictureTable", null, values);
            }
            Log.i(LOGTAG, "successfully edited task ");
            return 1;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to edited task " + e.toString());
        }
        return -1;
    }

    //used in complete Task
    public ArrayList GetImages(int taskID) {
        try {
            ArrayList<Bitmap> bl = new ArrayList<>();

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT picture FROM EventPictureTable where eventID = " + taskID, null);

            while (cursor.moveToNext()) {
                byte[] imgByte = cursor.getBlob(0);
                bl.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            }
            cursor.close();
            Log.i(LOGTAG, "Successfully Got images");
            return bl;
        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to get images " + e.toString());
            return null;
        }
    }

    //used to convert bitmaps to byteArray for storage
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }




    //used in GroupList.java to display list of all groups
    public String[] getGroupList(int phnNumber){
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //get the cursor to the GroupTable
            Cursor cursor = db.rawQuery(GROUPTABLE_DETAILS+String.valueOf(phnNumber), null);
            //count the number of rows
            int rowNum=cursor.getCount();

            String[] groupNames = new String[rowNum];

            int i=0;
            while (cursor.moveToNext()){  // get the data into array, or class variable
                    groupNames[i]=cursor.getString(0);
                    i++;
                }

            cursor.close();
            db.close();
            Log.i(LOGTAG, "Successfully Fetched GroupNames");
            return groupNames;
        }catch(Exception e){
            Log.i(LOGTAG, "Failed to Fetch GroupNames " + e.toString());
            return null;
        }
    }


    //used in ViewTask.java to display list of all groups
    public String[] getTaskList(int phnNumber){
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //get the cursor
            Cursor cursor = db.rawQuery(GROUPEVENTTABLE_DETAILS+String.valueOf(phnNumber)+")", null);

            //count the number of rows
            int rowNum=cursor.getCount();

            String[] eventNames = new String[rowNum];

            int i=0;
            while (cursor.moveToNext()){  // get the data into array, or class variable
                eventNames[i]=cursor.getString(0);
                Log.d(String.valueOf(i)+ "~>",eventNames[i]);
                i++;
            }

            cursor.close();
            db.close();
            Log.i(LOGTAG, "Successfully Fetched EventNames");
            return eventNames;
        }catch(Exception e){
            Log.i(LOGTAG, "Failed to Fetch EventNames " + e.toString());
            return null;
        }
    }

    //used in ViewSingleTask.java to display the details of single task
    public String[] getTaskDetails(int phnNumber, int position) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            //get the cursor
            Cursor cursor = db.rawQuery(EVENTTABLE_DETAILS + String.valueOf(phnNumber) + ")", null);

            //count the number of rows
            int rowNum = cursor.getCount();

            String taskTitle;
            String taskDescription;
            String user;
            String date;
            String[] taskDetails;

            int i = 0;
            if(position!=0){
                while (cursor.moveToNext()) {  // get the data into array, or class variable
                    i++;
                    if (position == i) {
                        break;
                    }
                }
            }
            cursor.moveToNext();

            if (i == position) {
                taskTitle = cursor.getString(0);
                taskDescription = cursor.getString(1);
                user = cursor.getString(2);
                date = cursor.getString(3);

                taskDetails = new String[]{taskTitle, taskDescription, user, date};
                cursor.close();
                db.close();
                Log.i(LOGTAG, "Successfully Fetched Event Details");
                return taskDetails;
            } else {
                Log.i(LOGTAG, "Problem in fetching event details");
            }

        } catch (Exception e) {
            Log.i(LOGTAG, "Failed to Fetch event details " + e.toString());
            return null;
        }
        return null;
    }
}



