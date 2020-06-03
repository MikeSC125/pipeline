package com.example.pipeline;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditEventFragment extends Fragment {

    //declaring variables to be used in this class
    private List<String> toDoList;
    private EditText newTaskEditText;
    private EditText titleText;
    private EditText dateDate;
    private EditText descriptionText;
    private Spinner categorySpinner;
    private String colorChoice;
    private int ID;
    private List<String> data = new ArrayList<>();
    private EventsReaderDatabaseHelper databaseHelper;
    private MaterialToolbar toolbar;

    public EditEventFragment() {
        // Required empty public constructor
    }

    public static EditEventFragment newInstance(String param1, String param2) {
        EditEventFragment fragment = new EditEventFragment();
        return fragment;
    }

    //default onCreate method created when creating fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //default onCreateView created when creating a fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, linking this java file to the fragment_edit_event layout resource file
        View view =  inflater.inflate(R.layout.fragment_edit_event, container, false);

        //Bundle below is receiving the data sent via Bundle from the SelectedEventFragment
        //getArguments() gets the arguments sent through to the current screen when the fragment is replaced to show this EditEventFragment
        Bundle bundle = getArguments();

        //assigning the bundle element with key "title" to the title string
        String title = bundle.getString("title");

        //assigning the bundle element with key "date" to the date string
        String date = bundle.getString("date");

        //assigning the bundle element with key "description" to the description string
        String description = bundle.getString("description");

        // assigning the bundle element with key "color" to the color string
        String color = bundle.getString("color");

        //assigning bundle element with key "ID" to the ID integer
        ID = bundle.getInt("ID");

        //assigning bundle element with key "category" to the category integer
        int category = bundle.getInt("category");

        //assigning title string to the bundle element with key "title"
        String todolist = bundle.getString("ToDoList");

        //locating the relevant components in the layout resource file of this fragment
        titleText = view.findViewById(R.id.TitleEditText);
        dateDate = view.findViewById(R.id.dateEditText);
        descriptionText = view.findViewById(R.id.descriptionEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        //loacting toolbar from actviity
        toolbar = getActivity().findViewById(R.id.toolbar);

        //setting the title edit text to the one obtained from the bundle
        titleText.setText(title);
        //setting the date edit text to the date obtained from the bundle
        dateDate.setText(date);
        //setting the description edit text to the one obtained from the bundle
        descriptionText.setText(description);
        //setting the category spinner to the category passed via bundle
        categorySpinner.setSelection(category);
        //altering toolbar background
        toolbar.setBackgroundColor(Color.parseColor(color));

        view.findViewById(R.id.background).setBackgroundColor(Color.parseColor(color));

        // declaring the json array
        JSONArray jsonArray = null;
        try {
            //try instantiate the json array with the todolist element obtained from the bundle
            jsonArray = new JSONArray(todolist);
        } catch (JSONException e) {
            //if something goes wrong with the above instantiation the exception e will be caught
            //stack trace will be printed
            e.printStackTrace();
        }

        //for loop as long as the jsonArray adding each element of the json array back to an arraylist called data
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                //adding each element to the data arraylist
                data.add((String) jsonArray.get(i));
            } catch (JSONException e) {
                //if the above task fails the exception will be caught here and stack trace printed
                e.printStackTrace();
            }
        }

        //array adapter is used to convert each element in the arraylist 'data' to the type as declared in the
        //list_view_layout layout resource file to display each element of the arraylist to the list view tasksList
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_view_layout, data);
        // locating the eventList component in the layout resource file of the current fragment
        ListView tasksList = view.findViewById(R.id.listView);
        //setting the adapter to the tasksList ListView component passing the array adapter declared above as a parameter
        //which will be the array adapter used
        tasksList.setAdapter(arrayAdapter);

        //locating the editText which allows the user to add new tasks to the list view of tasks to do
        newTaskEditText = view.findViewById(R.id.newTaskEditText);

        //setting an onclick listener to the tasksList
        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                //adding a strikethrough through the task clicked on once completed
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        //locating the button to add a new task to the tasksList
        Button addButton = view.findViewById(R.id.addButton);

        //setting an onclick listener to the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtaining the text entered and converting it to string
                String itemEntered = newTaskEditText.getText().toString();
                if(itemEntered.equals("") == false){
                    //ensuring no blank tasks are inserted

                    //data arraylist adds the new task
                    data.add(itemEntered);

                    //clear the EditText to allow new tasks to be added
                    newTaskEditText.setText("");

                    //notifying the adapter that the dataset changes for it to update accordingly
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //locating the item at its position and removing it
                data.remove(position);
                //notifying the arrayAdapter to update accordingly
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //locating the cancel floating action button in the layout resource file
        FloatingActionButton cancel = view.findViewById(R.id.cancelFloatingActionButton);

        //setting an onclick listener to the cancel floating action button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //using the fragmentManager in the MainActivity, replace the current fragment with
                //the home fragment, essentially taking the user back to the home screen
                MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), null).commit();
                //resetting toolbar background
                toolbar.setBackgroundColor(Color.parseColor("#01579b"));
            }
        });

        //locating the update floating action button in the current layout resource file for the current fragment
        FloatingActionButton update = view.findViewById(R.id.UpdateDataFloatingActionButton);

        //setting an on click listener for the update button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateNewEvent()){
                    //toast alerting incorrect data has been entered
                    Toast toast = Toast.makeText(getActivity(), "Please ensure all fields are filled correctly", Toast.LENGTH_LONG);
                    //showing the toast
                    toast.show();
                }else {

                    //instantiating a json Array using the data arraylist containing all the tasks in the list view
                    JSONArray jsonArray = new JSONArray(data);
                    //converting the jsonArray to a string to be stored in the database efficiently
                    String toDoListString = jsonArray.toString();

                    switch (categorySpinner.getSelectedItemPosition()) {
                        case 0:
                            colorChoice = "#29b6f6";
                            break;
                        case 1:
                            colorChoice = "#90caf9";
                            break;
                        case 2:
                            colorChoice = "#58a5f0";
                            break;
                        case 3:
                            colorChoice = "#0086c3";
                            break;
                        case 4:
                            colorChoice = "#00b0ff";
                            break;
                        case 5:
                            colorChoice = "#81d4fa";
                            break;
                    }

                    // instantiating an Event using the contents of the respective EditText fields in the form to update the event at hand
                    // this instance is the updated event instance
                    Event updatedEvent = new Event(ID, titleText.getText().toString(), dateDate.getText().toString(), descriptionText.getText().toString(), colorChoice, categorySpinner.getSelectedItemPosition(), toDoListString);

                    //instantiating the EventsReaderDatabaseHelper instance to use the getActivity method to get the current context
                    databaseHelper = new EventsReaderDatabaseHelper(getActivity());

                    //Using the fragmentManager in the main activity replace the current fragment with the home fragment essentially taking the user home
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), null).commit();
                    //resetting toolbar background
                    toolbar.setBackgroundColor(Color.parseColor("#01579b"));
                    //using the EventsReaderDatabaseHelper the updateEvent method will make use of the updated event instance of the Event class
                    // which will then be saved to the database updating the previous record since the ID will uniquely identify the record to be updated
                    databaseHelper.updateEvent(updatedEvent);
                }
            }
        });

        return view;
    }

    //private method to validate the event
    //ensures valid data is entered
    private boolean validateNewEvent(){
        if(
            //ensuring no blank fields are present
                titleText.getText().toString().equals("") |
                        dateDate.getText().toString().equals("") |
                        descriptionText.getText().toString().equals("")
        ){
            //return true if data is invalid
            return true;
        } else {
            //validating date if data fields are not blank
            String dateString = dateDate.getText().toString();
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
            //check that a valid date for febuary is entered
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
