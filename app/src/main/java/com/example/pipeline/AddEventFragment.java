package com.example.pipeline;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;


public class AddEventFragment extends Fragment {

    //declaring private variables to be used within this java class
    private List<String> toDoList;
    private EditText newTaskEditText;
    private Spinner categorySpinner;
    private EventsReaderDatabaseHelper databaseHelper;
    private EditText title;
    private EditText date;
    private EditText description;
    private String titleText;
    private String dateText;
    private String descriptionText;
    private String colorChoice;
    private Event event;


    //empty class constructor
    public AddEventFragment() {

    }

    // on create override as created by default when creating a fragment java class
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    //On create view as created by default when createing a fragment java class
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, linking class to layout in res folder
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        //creating a new instance of EventsReaderDatabaseHelper with the current context obtained by getActivity
        databaseHelper = new EventsReaderDatabaseHelper(getActivity());

        //locating the components in the respective layout file and assigning them to class variables to be able
        //to make use of those components
        title = view.findViewById(R.id.titleEditText);
        date = view.findViewById(R.id.dateTextEdit);
        description = view.findViewById(R.id.descriptionTextEdit);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        //declaring the toDoList as declared at the top of the class as a new Array List to be able to make use of it below
        toDoList = new ArrayList<>();


        //declaring a new array adapter
        //the array adapter will be used to display the text from the array list formatting it as is in the list_view_layout...
        //...and using it to display the elements from the array list to the list view in the fragment_add_event layour resource file
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_view_layout, toDoList);

        //locating the list view to display elements of the arraylist, as they are created, to the same list view
        ListView eventsList = view.findViewById(R.id.listView);

        //setting the array adapter to the events list
        eventsList.setAdapter(arrayAdapter);

        //locating the edit text or 'text input' field which the user will use to create a new task to be added to the toDoList array list
        newTaskEditText = view.findViewById(R.id.newTaskEditText);

        //setting an onclick listener to the list view, locating the respective text view being clicked
        //and adding a strikethrough through the text.
        //this will be used when a task is completed, removing it from the list
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                //the line below adds a strikethrough through the text
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        //locating the add button in the layout resource file
        Button addButton = view.findViewById(R.id.addButton);

        //setting an on click listener to the add button
        // this add button will add the text located in the newTaskEditText edit text to the array list and display
        //to the list view, showing the new task added
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemEntered = newTaskEditText.getText().toString();
                //ensuring no blank tasks are inserted
                if(!itemEntered.equals("")){
                    //adding tasks to the arraylist
                    toDoList.add(itemEntered);
                    //setting text field of newTaskEditText to an empty string, removing the current text which would
                    //be inserted into the arraylist
                    newTaskEditText.setText("");
                    //notifying adapter dataset changed for it to update listview accordingly
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        //setting onclick listener for elements of the list view containing to do list tasks
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //locating the item at its position and removing it
                toDoList.remove(position);
                //notifying the arrayAdapter to update accordingly
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //locating the confirm button within the layour resource file in question
        FloatingActionButton confirm = view.findViewById(R.id.confirmFloatingAction);

        //setting an onclick listener to the confirm button to handle the click once the confirm button is clicked
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating the new event
                //if the validation return true then a toast will be displayed to indicate invalid data has been entered
                if(validateNewEvent()){
                    //toast alerting incorrect data has been entered
                    Toast toast = Toast.makeText(getActivity(), "Please ensure all fields are filled correctly", Toast.LENGTH_LONG);
                    //showing the toast
                    toast.show();
                }else {
                    //if the validation returns false execute the following

                    //obtain text from title edit text and assign it to the titleText private variable
                    titleText = title.getText().toString();
                    //obtain date from date and assign it to the dateText private variable
                    dateText = date.getText().toString();
                    //obtain text from description edit text and assign it to the descriptionText private variable
                    descriptionText = description.getText().toString();
                    //obtain color option from categorySpinner and assign it to the colorChoice private variable
                    switch (categorySpinner.getSelectedItemPosition()){
                        //setting color depending on category
                        case 0: colorChoice = "#29b6f6";
                            break;
                        case 1: colorChoice = "#73e8ff";
                            break;
                        case 2: colorChoice = "#58a5f0";
                            break;
                        case 3: colorChoice = "#0086c3";
                            break;
                        case 4: colorChoice = "#00b0ff";
                            break;
                        case 5: colorChoice = "#81d4fa";
                            break;
                    }


                    //declaration of a json array to be used to store the arraylist of elements in the toDoList arraylist
                    JSONArray jsonArray = new JSONArray(toDoList);

                    //converting the above jsonarray to a stringin order for a string to be saved in the database allowing efficient saving
                    String toDoListString = jsonArray.toString();

                    // creating a new instance of the Event class in order for the save to be called
                    event = new Event(titleText, dateText, descriptionText, colorChoice, categorySpinner.getSelectedItemPosition(), toDoListString);

                    //saving the above instance of Event to the database by using createEvent in the EventsReaderDatabaseHelper class
                    databaseHelper.createEvent(event);

                    //making use of the fragment manager in the main activity java class to replace the fragment displayed in the frame
                    // replacing it with the HomeFragment fragment, returning the user to the home once the new event is saved
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), null).commit();
                }

            }
        });

        //locating the cancel floating action button in the layout resource file
        FloatingActionButton cancel = view.findViewById(R.id.cancelFloatingAction);

        //setting an onclick listener to the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the cancel button is clicked the fragment currently on displayed in the frame if replaced with the
                //home fragment, essentially taking the user back to the home screen
                MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), null).commit();
            }
        });

        return view;
    }

    //private method to validate the event
    //ensures valid data is entered
    private boolean validateNewEvent(){
        if(
                //ensuring no blank fields are present
                title.getText().toString().equals("") |
                date.getText().toString().equals("") |
                description.getText().toString().equals("")
        ){
            //return true if data is invalid
            return true;
        } else {
            //validating date if data fields are not blank
            String dateString = date.getText().toString();
            //try catch handling errors
            try{
                //attempt to match the date entered to the pattern dd/mm/yyyy
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date parseDateResult = dateFormat.parse(dateString);
            }catch(ParseException e){
                //if it does not match the pattern
                //return true if invalid
                return true;
            }
            int day;
            int month;
            int year;
            try{
                //try to convert date elements to int to continue to validate date entered
                day = Integer.parseInt(dateString.substring(0,2));
                month = Integer.parseInt(dateString.substring(3,5));
                year = Integer.parseInt(dateString.substring(6));
            }catch (Exception e){
                //in the event an invalid date pattern is entered
                //toast alerting incorrect data has been entered
                Toast toast = Toast.makeText(getActivity(), "Date must be in the format dd/mm/yyyy", Toast.LENGTH_LONG);
                //showing the toast
                toast.show();
                return true;
            }
            //check that the day is valid
            if (day < 1  || day > 31 ){
                return true;
            }
            //check that the month is valid
            else if(month <1 || month >12){
                return true;
            }
            //check that the year is valid
            else if (year < 2020){
                return true;
            }
            //check that no invalid years are entered
            else if (year > 2030){
                return true;
            }
            //check that a valid date for February is entered
            else if(day > 29 && month == 2){
                return true;
            }
            else if (day == 31 && month == 4 || day == 31 && month == 6 || day == 31 && month == 9 || day == 31 && month == 11){
                return true;
            }
            else{
                //if all checks are made successfully and a valid date is entered
                //returning false implies all data is valid and no empty fields are present
                return false;
            }
        }
    }



}
