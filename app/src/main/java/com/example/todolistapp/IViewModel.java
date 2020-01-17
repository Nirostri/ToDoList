package com.example.todolistapp;

public interface IViewModel {

    public  void getItems();
    public  void addItem(int userId, String date,String name);
    public  void deleteItem();
    public  void updated();
    public  void setModel(Imodel model);
    public  void setView(Iview view);
}
