package com.example.todolistapp;
import android.content.Context;
import org.json.JSONException;

public interface Imodel {
    void showToast(Context context, String txt);//to show toast when using "this."
    public void deleteFromToDoList(String date, String name);//tasks to be removed from the database
    public void updateToDoList(String date, String name,String updateDate, String updateName);
    public void addItem(int userId, String date,String name);//initialize ContentValues object
    public String getItems() throws JSONException; //load tasks from DB
}
