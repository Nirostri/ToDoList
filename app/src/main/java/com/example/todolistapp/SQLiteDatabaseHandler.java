package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper implements Imodel {

    // create SQLite database
    private SQLiteDatabase db;
    private Context context;
    public static final String DATABASE_NAME = "toDo_db";
    private static final int DATABASE_VERSION = 1;
    private static final String  TABLE_NAME= "Activities";

    abstract class Columns {
        // all the database columns
        private static final String COL_1 = "serialNum";
        private static final String COL_2 = "date";
        private static final String COL_3 = "name";
        private static final String COL_4 = "userId";
    }


    /***
     * Constructor
     * @param context
     */
    public SQLiteDatabaseHandler(Context context) {
        // calling SQLiteOpenHelper constructor
        super(context,DATABASE_NAME,null,DATABASE_VERSION );

        // set which activity is using the context
        this.context = context;

        // must be last line in the constructor,in order to make sure all variables are initialized prior to onCreate()
        this.db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create Activities Table
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "("+Columns.COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Columns.COL_2+" TEXT," +
                Columns.COL_3+" TEXT,"+
                Columns.COL_4+" INTEGER);");
        this.showToast(this.context,"initialized");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME +"'");
        onCreate(db);
    }

    @Override
    public void showToast(Context context, String txt) {
        Toast.makeText(context,txt,Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteFromToDoList(String date, String name) {
        //task to be removed from the database
        String whereClause = "date=? AND name =?" ;
        String whereArgs[] = new String[] {date, name};
        //delete the task and show toast
        if (db.delete(TABLE_NAME,whereClause,whereArgs)==0) {
            Toast.makeText(context,"Error: the task does not appear",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context,"The task deleted successfully",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateToDoList(String date, String name, String updateDate,
                               String updateName) {
        ContentValues values = new ContentValues();
        String whereArgs[] = new String[] {date, name};
        String whereClause = "date=? AND name =?" ;

        //fill in the values
        values.put(Columns.COL_2, updateDate);
        values.put(Columns.COL_3, updateName);
        if (db.update(TABLE_NAME,values,whereClause,whereArgs)==0) {
            Toast.makeText(context,"Error: task does not appear",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context,"The task updated successfully",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void addItem(int userId, String date, String name) {
        //initialize ContentValues object
        ContentValues values = new ContentValues();

        values.put(Columns.COL_2, date);
        values.put(Columns.COL_3, name);
        values.put(Columns.COL_4, userId);

        //make the insertion to the DB
        if (db.insert(TABLE_NAME,null,values)!=-1) {
            this.showToast(this.context,"Task as been added to list");
        } else {
            this.showToast(this.context,"Error: try again to add");
        }
    }

    @Override
    public String getItems() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        json.put("toDoItems",arr);

        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        try{
            while (cursor.moveToNext()){
                JSONObject item = new JSONObject();
                item.put("serial",cursor.getString(cursor.getColumnIndex("serialnum")));
                item.put("id",cursor.getString(cursor.getColumnIndex("userid")));
                item.put("item",cursor.getString(cursor.getColumnIndex("name")));
                item.put("date",cursor.getString(cursor.getColumnIndex("date")));
                arr.put(item);
            }
        }
        finally{
            cursor.close(); //must close the cursor at the end
        }
        return json.toString();
    }
}
