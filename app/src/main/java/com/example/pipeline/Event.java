package com.example.pipeline;

import java.io.Serializable;

public class Event implements Serializable {

    //this class is the model class for each event created.
    //for each event created an instance of this class is created
    //this class is essentially used to save elements of type event to the database

    //declaring private variables to be used in this class
    //these are the elements which form an event
    private int id;
    private String title;
    private String date;
    private String description;
    private String color;
    private int category;
    private String toDoList;

    // Event constructor which uses title, date, description, color and todolist
    public Event(String pTitle, String pDate, String pDescription, String pColor, int pCategory, String pToDoList){

        //assigning each pTitle argument to the title variable declared above
        this.title = pTitle;

        //assigning each pDate argument to the date variable declared above
        this.date = pDate;

        //assigning each pDescription argument to the description variable declared above
        this.description = pDescription;

        //assigning each pColor argument to the color variable declared above
        this.color = pColor;

        //assigning each pCategory argument to the category variable declared above
        this.category = pCategory;

        //assigning each pToDoList argument to the toDoList variable declared above
        this.toDoList = pToDoList;
    }

    // this constructor takes ID, title, date, description, color and todolist
    //this constructor is used to update the event since an ID is passed as an argument
    //ID is automatically generated therefore this constructor can only be used to update not create
    public Event(int pID, String pTitle, String pDate, String pDescription, String pColor, int pCategory, String pToDoList){

        //assigning each pID argument to the id variable declared above
        this.id = pID;

        //assigning each pTitle argument to the title variable declared above
        this.title = pTitle;

        //assigning each pDate argument to the date variable declared above
        this.date = pDate;

        //assigning each pDescription argument to the description variable declared above
        this.description = pDescription;

        //assigning each pColor argument to the color variable declared above
        this.color = pColor;

        //assigning each pCategory argument to the category variable declared above
        this.category = pCategory;

        //assigning each pToDoList argument to the toDoList variable declared above
        this.toDoList = pToDoList;
    }


    //setter for ID
    public void setID(int pId){
        this.id = pId;
    }

    //getter for ID
    public int getID(){
        return id;
    }

    //getter for title
    public String getTitle(){
        return title;
    }

    //setter for title
    public void setTitle(String pTitle){
        this.title = pTitle;
    }

    //getter for date
    public String getDate(){
        return date;
    }

    //setter for setDate
    public void setDate(String pDate){
        this.date = pDate;
    }

    //getter for description
    public String getDescription(){
        return description;
    }

    //setter for description
    public void setDescription(String pDesc){
        this.description = pDesc;
    }

    //getter for color
    public String getColor(){
        return color;
    }

    //setter for color
    public void setColor(String pColor){
        color = pColor;
    }

    //getter for category
    public int getCategory(){
        return category;
    }

    //setter for category
    public void setCategory(int pCategory){
        category = pCategory;
    }

    //setter for todolist
    public void setToDoList(String pToDoList){
        this.toDoList = pToDoList;
    }

    //getter for todolist
    public String getToDoList(){
        return toDoList;
    }

}
