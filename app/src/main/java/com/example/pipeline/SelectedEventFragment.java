package com.example.pipeline;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SelectedEventFragment extends Fragment {

    //declaring variables to be used in this fragment class
    private String title;
    private String date;
    private String description;
    private String color;
    private int ID;
    private int category;
    private String categoryName;
    private String todolist;
    private MaterialToolbar toolbar;


    public SelectedEventFragment() {
        // Required empty public constructor
    }

    public static SelectedEventFragment newInstance(String param1, String param2) {
        SelectedEventFragment fragment = new SelectedEventFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_selected_event, container, false);

        //Getting the bundle passed from the home fragment to use in this class
        Bundle bundle = getArguments();
        //setting title variable to the attribute passed via the bundle with key "title"
        title = bundle.getString("title");

        //setting date variable to the attribute passed via the bundle with key "date"
        date = bundle.getString("date");

        //setting description variable to the attribute passed via the bundle with key "description"
        description = bundle.getString("description");

        //setting color variable to the attribute passed via the bundle with key "color"
        color = bundle.getString("color");

        //setting ID variable to the attribute passed via the bundle with key "ID"
        ID = bundle.getInt("ID");

        category = bundle.getInt("category");

        //setting todolist variable to the attribute passed via the bundle with key "todolist"
        todolist = bundle.getString("ToDoList");

        //locating the titleText TextView from the current layout resource file
        TextView titleText = view.findViewById(R.id.TitleTextView);

        //locating the dateDate TextView from the current layout resource file
        TextView dateDate = view.findViewById(R.id.dateTextView);

        //locating the descriptionText TextView from the current layout resource file
        TextView descriptionText = view.findViewById(R.id.descriptionTextView);

        //locating the category TextView from the current layout resource file
        TextView categoryTextView = view.findViewById(R.id.categoryTextView);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(color));
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
//        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        //converting string to jsonarray to arraylist to display todolist tasks
        List<String> data = new ArrayList<>();

        switch (category){
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

        categoryTextView.setText(categoryName);
        //declaring a jsonarray
        JSONArray jsonArray = null;
        try {
            //instantiating the jsonArray using the todolist passed through the bundle into this fragment
            jsonArray = new JSONArray(todolist);
        } catch (JSONException e) {
            //in the case of an exception the stack trace will be printed
            e.printStackTrace();
        }

        //for the length of the jsonArray
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                //adding each jsonArray element to the data arraylist
                data.add((String) jsonArray.get(i));
            } catch (JSONException e) {
                //in the event of an exception print the stack trace
                e.printStackTrace();
            }
        }

        //setting an arrayadapter using the list_view_layout as a template for each element in the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_view_layout, data);
        //locating the listview to output the array to by means of the above arrayadapter
        ListView eventsList = view.findViewById(R.id.listView);
        //setting the eventslist arrayadapter
        eventsList.setAdapter(arrayAdapter);


        //setting title text from the bundle
        titleText.setText(title);
        //setting date text from the bundle
        dateDate.setText(date);
        //setting description text from the bundle
        descriptionText.setText(description);

        //setting background color to the color preference passed via the bundle
        view.findViewById(R.id.background).setBackgroundColor(Color.parseColor(color));

        //locating the back floating action button
        FloatingActionButton back = view.findViewById(R.id.backFloatingActionButton);
        //setting an onclick listener to the back button
        back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //replave current fragment with the home fragment effectively returning to home fragment
                MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), null).commit();
                //resetting toolbar background
                toolbar.setBackgroundColor(Color.parseColor("#01579b"));
            }
        });

        //locating edit floating action button
        FloatingActionButton edit = view.findViewById(R.id.editFloatingActionButton);
        //setting onclick listener to the edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a bundle to pass to edit fragment to be able to make use of the data being displayed in this fragment
                Bundle bundle = new Bundle();
                //adding title to the bundle with key "title"
                bundle.putString("title", title);
                //adding date to the bundle with key "date"
                bundle.putString("date", date);
                //adding description to the bundle with key "description"
                bundle.putString("description", description);
                //adding color to the bundle with key "color"
                bundle.putString("color", color);
                //adding ID to the bundle with key "ID"
                bundle.putInt("ID", ID);
                //adding ToDoList to the bundle with key "todolist"
                bundle.putString("ToDoList", todolist);
                //adding category to the bundle with key "category"
                bundle.putInt("category", category);



                //creating an instance of EditEventFragment
                EditEventFragment editEventFragment = new EditEventFragment();

                //setting the arguments for the above instance to the bundle declared above
                editEventFragment.setArguments(bundle);

                //replacing the current fragment with the EditEventFragment instance to pass the bundle created above
                MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, editEventFragment).commit();
                //resetting toolbar background
                toolbar.setBackgroundColor(Color.parseColor("#01579b"));

            }
        });


        return view;
    }
}
