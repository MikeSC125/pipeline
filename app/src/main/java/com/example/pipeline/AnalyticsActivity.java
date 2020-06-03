package com.example.pipeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    //declaring variables to be used within this activity java class
    AnyChartView pieChart;
    ArrayList<Event> events;
    EventsReaderDatabaseHelper eventsReaderDatabaseHelper;
    private DrawerLayout drawer;
    private MaterialToolbar toolbar;

    //default onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the context view of this layout resource file
        setContentView(R.layout.activity_analytics);

        //instantiating the eventsReaderDatabaseHelper instance
        eventsReaderDatabaseHelper = new EventsReaderDatabaseHelper(this);

        //assigning the result of the read from the database by means of the eventsReaderDatabaseHelper class
        //to the events arraylist since the listEvent method returns an arraylist
        events = eventsReaderDatabaseHelper.listEvent();

        //finding pie chart in the layout resource file
        pieChart = findViewById(R.id.pieChart);
        APIlib.getInstance().setActiveAnyChartView(pieChart);
        //calling the piechart method
        setUpPieChart();

        //locating drawer component
        drawer = findViewById(R.id.drawer);
        //locating toolbar component
        toolbar = findViewById(R.id.toolbar);
        //toggle allows the opening and closing of the drawer declared above by means of the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        //adding listener to enable the toggle when the hamburger menu is selected
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view
        NavigationView navigationView = findViewById(R.id.navigation);
        //handling the selection of attributes in the hamburger meu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //finish closes current activity returning to the previous activity, closing the drawer upon return
                finish();
                return true;
            }
        });
    }

    //pie chart method
    public void setUpPieChart(){
        // creating an instance of the pie chart from anychart library
        Pie pie = AnyChart.pie();

        //declaring a new arraylist called list
        List<DataEntry> data = new ArrayList<>();

        //using EventsReaderDatabaseHelper method to get totals and names of categories
        ArrayList<CategoryTotals> categoryTotals = eventsReaderDatabaseHelper.groupCategories();
        //Arraylist to hold colors of each respective category
        String [] categoryColors= new String [categoryTotals.size()];
        //for loop adding the color and values to create a pie chart to the list declared above
        for(int i=0;i<categoryTotals.size();i++){
            //adding a new data entry for each total
            data.add(new ValueDataEntry(categoryTotals.get(i).getCategory(), categoryTotals.get(i).getTotal()));
            //adding the color of the respective category to the arraylist of colors
            //this will be used to set the color palette of the piechart
            categoryColors[i] = categoryTotals.get(i).getColor();
        }
        //assigning the list as the data to be used to create the pie chart
        pie.data(data);
        //setting the to pie chart container  the one created above
        pieChart.setChart(pie);
        //sets colors of each segment to the color of the event
        pie.palette(categoryColors);

    }

}
