package com.example.pipeline;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class HomeFragment extends Fragment implements MyAdapter.selectedEvent {

    private MyAdapter myAdapter;

    public HomeFragment() {
        // Required empty public constructor
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
        //inflating view
        //locating the respective resource layout file for the home fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //instantiating the instance of EventsReaderDatabaseHelper passing the current activity as context
        EventsReaderDatabaseHelper dbHelper = new EventsReaderDatabaseHelper(getActivity());

        //locating the EventsRecyclerView and assigning it to the variable eventsRecyclerView
        RecyclerView eventsRecyclerView = view.findViewById(R.id.EventsRecyclerView);

        //using listEvent() from EventsReaderDatabaseHelper to list all records from the database
        //saving the result to the eventsList of type arraylist
        ArrayList<Event> eventsList = dbHelper.listEvent();

        //instantiating the myAdapter variable declared above
        myAdapter = new MyAdapter(getActivity(), eventsList, this);

        //setting the above adapter to the eventsRecyclerView
        eventsRecyclerView.setAdapter(myAdapter);
        //setting a layout manager to the eventsRecyclerView with the current
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //declaring and locating the add floating action button to create a new event
        FloatingActionButton add = view.findViewById(R.id.AddEventFloatingAction);

        //setting an onclick listener to the add button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //replacing the current home fragment with the add event fragment essentially allowing a user to create a new event
                MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, new AddEventFragment(), null).commit();
            }
        });

        //locating the search view in the current layout resource file
        SearchView searchView = view.findViewById(R.id.searchView);

        //calling the search method passing the searchview located above as an argument
        search(searchView);

        return view;
    }

    //selected event override event
    @Override
    public void selectedEvent(Event event) {
        //declaring bundle to be passed to the selected event fragment allowing visibility to the data passed
        Bundle bundle = new Bundle();

        //passing title to SelectedEventFragment with key "title"
        bundle.putString("title", event.getTitle());

        //passing date to SelectedEventFragment with key "date"
        bundle.putString("date", event.getDate());

        //passing description to SelectedEventFragment with key "description"
        bundle.putString("description", event.getDescription());

        //passing color to SelectedEventFragment with key "color"
        bundle.putString("color", event.getColor());

        //passing ID to SelectedEventFragment with key "ID"
        bundle.putInt("ID", event.getID());

        //passing categories to SelectedEventFragment with key "category"
        bundle.putInt("category", event.getCategory());

        //passing ToDoList to SelectedEventFragment with key "ToDoList"
        bundle.putString("ToDoList", event.getToDoList());

        //creating an instance of SelectedEventFragment
        SelectedEventFragment selectedEventFragment = new SelectedEventFragment();

        //setting the arguments for the above instance to the bundle declared above
        selectedEventFragment.setArguments(bundle);

        //replacing the current fragment with the selectedEventFragment instance to pass the bundle declared
        MainActivity.fragmentManager.beginTransaction().replace(R.id.frame, selectedEventFragment).commit();

    }

    //search method to filter the events in the respective arraylist
    private void search(SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //setting a filter to the adapter declared above using the newText argument
                myAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
