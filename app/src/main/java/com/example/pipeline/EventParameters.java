package com.example.pipeline;

import android.provider.BaseColumns;

public class EventParameters {

    //empty constructor
    private EventParameters () {
    }

    //BaseColumns creates id automatically
    //this class declared the columns to be used in the database, essentially event parameters to be used
    //by the SQLite database throughout the program

    //the method below created the columns in the database with their respective names to be used
    //by the EventsReaderDatabaseHelper class
    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "Events";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_COLOR = "color";
        static final String COLUMN_CATEGORY = "category";
        static final String COLUMN_TODOLIST = "toDoList";
    }
}
