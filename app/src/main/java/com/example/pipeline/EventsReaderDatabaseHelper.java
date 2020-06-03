package com.example.pipeline;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.pipeline.EventParameters.EventEntry;
import java.util.ArrayList;

public class EventsReaderDatabaseHelper extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	1;
    private	static final String	DATABASE_NAME = "pipelineDB";

    //constructor for EventsReaderDatabaseHelper taking context as an argument
    public EventsReaderDatabaseHelper(Context context) {
        //passing context, database name, and database version to super class accepting the arguments in question
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating database if it does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        //the string below creates a database if it does not existing
        //with table name as declared above,
        //ID which is automatically incrememnted
        //title column accepting the title from the model to be saved
        //date column accepting the date from the model to be saved
        //description column accepting the description from the model to be saved
        //color column accepting the color from the model to be saved
        //todolist column accepting the todolist as a string from the model to be saved
        String	CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + EventParameters.EventEntry.TABLE_NAME
                + "(" + EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventParameters.EventEntry.COLUMN_TITLE + " TEXT,"
                + EventEntry.COLUMN_DATE + " TEXT,"
                + EventEntry.COLUMN_DESCRIPTION + " TEXT,"
                + EventEntry.COLUMN_COLOR + " TEXT,"
                + EventEntry.COLUMN_CATEGORY + " INTEGER,"
                + EventEntry.COLUMN_TODOLIST + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    //when the database is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop table if the table exists when the db is updated
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        //pass db argument to the onCreate
        onCreate(db);
    }

    // column names are keys allowing us to create a map of them
    //this method will be called to add an event to the database
    public void createEvent(Event event){
        ContentValues values = new ContentValues();

        //entering the title of the new event
        values.put(EventEntry.COLUMN_TITLE, event.getTitle());

        //entering the date of the new event
        values.put(EventEntry.COLUMN_DATE, event.getDate());

        //entering the description of the new event
        values.put(EventEntry.COLUMN_DESCRIPTION, event.getDescription());

        //entering the color of the new event
        values.put(EventEntry.COLUMN_COLOR, event.getColor());

        //entering the category of the new event
        values.put(EventEntry.COLUMN_CATEGORY, event.getCategory());

        //entering the ToDoList of the new event
        values.put(EventEntry.COLUMN_TODOLIST, event.getToDoList());

        SQLiteDatabase db = this.getWritableDatabase();

        //inserting into the database
        db.insert(EventEntry.TABLE_NAME, null, values);
    }

    //This method is used to update the event in question, this will make use of the ID to uniquely identify the event
    public void updateEvent(Event event){
        ContentValues values = new ContentValues();

        //entering the title of the event to be updated
        values.put(EventEntry.COLUMN_TITLE, event.getTitle());

        //entering the date of the event to be updated
        values.put(EventEntry.COLUMN_DATE, event.getDate());

        //entering the description of the event to be updated
        values.put(EventEntry.COLUMN_DESCRIPTION, event.getDescription());

        //entering the color of the event to be updated
        values.put(EventEntry.COLUMN_COLOR, event.getColor());

        //entering the category of the new event
        values.put(EventEntry.COLUMN_CATEGORY, event.getCategory());

        //entering the ToDoList of the event to be updated
        values.put(EventEntry.COLUMN_TODOLIST, event.getToDoList());

        SQLiteDatabase db = this.getWritableDatabase();

        //updating the event where the ID is the one in question
        db.update(EventEntry.TABLE_NAME, values, EventEntry._ID	+ "	= ?", new String[] {String.valueOf(event.getID())});
    }

    //deleting an event method
    public void deleteEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        //locates the method by ID to uniquely idetify it and remove it from the database
        db.delete(EventEntry.TABLE_NAME, EventEntry._ID	+ "	= ?", new String[] { String.valueOf(id)});
    }

    // this method lists all events and returns them as an arraylist
    public ArrayList<Event> listEvent(){

        //selecting all records from the database table name as declared above
        String sql = "SELECT * FROM " + EventEntry.TABLE_NAME;

        //getReadableDatabase offers the abiity to read the database and traverse it as required
        SQLiteDatabase db = this.getReadableDatabase();

        //declaration of a new arraylist to be loaded with results and returned
        ArrayList<Event> listEvents = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        //if at least one record is in the database...
        if(cursor.moveToFirst()){
            do{
                //retrieve ID
                int id = Integer.parseInt(cursor.getString(0));

                //retrieve title
                String title = cursor.getString(1);

                //retrieve date
                String date = cursor.getString(2);

                //retrieve description
                String description = cursor.getString(3);

                //retrieve color
                String color = cursor.getString(4);

                //retrieve category
                int category = cursor.getInt(5);

                //retrieve toDoList
                String toDoList = cursor.getString(6);

                //create a new event with the above attributes which were obtained for the respective record and
                //add it to the arraylist declared within this method to be returned
                listEvents.add(new Event(id, title, date, description, color, category, toDoList));
            }
            //while the list is not empty
            while (cursor.moveToNext());
        }
        //close the cursor to stop traversing
        cursor.close();

        //return the listEvents arraylist with all events
        return listEvents;
    }

    public ArrayList<CategoryTotals> groupCategories(){
        String sql = "SELECT category, COUNT(*), color FROM " + EventEntry.TABLE_NAME + " GROUP BY color";
        //getReadableDatabase offers the ability to read the database and traverse it as required
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<CategoryTotals> listStats = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        //traversing the records grouping by category
        if(cursor.moveToFirst()){
            do{
                //getting initial column value for category
                int categoryInt = cursor.getInt(0);
                //getting total by parsing column 1
                int total = Integer.parseInt(cursor.getString(1));
                //getting color by parsing column 2
                String color = cursor.getString(2);
                String categoryName="";
                switch (categoryInt){
                    case 0: categoryName = "Exam";
                        break;
                    case 1: categoryName = "Assignment";
                        break;
                    case 2: categoryName = "Academic";
                        break;
                    case 3: categoryName = "Conference";
                        break;
                    case 4: categoryName = "Leisure";
                        break;
                    case 5: categoryName = "Meeting";
                        break;
                }
                // creating a new instance of category totals using the category name total and color
                listStats.add(new CategoryTotals(categoryName, total, color));
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        return listStats;
    }
}
