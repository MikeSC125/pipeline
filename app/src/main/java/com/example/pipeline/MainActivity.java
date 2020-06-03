package com.example.pipeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private MaterialToolbar toolbar;

    //this onOptionsItemSelected handles the selection of the Analytics tab in the menu launching the AnalyticsActivity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //the following intent opens the AnalyticsActivity activity
        Intent intent = new Intent(MainActivity.this, AnalyticsActivity.class);
        //start the above activity
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //locating the respective layout resource file called activity_main
        setContentView(R.layout.activity_main);
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
                //taking user to the analytics activity once item is selected
                Intent intent = new Intent(getApplicationContext(), AnalyticsActivity.class);
                startActivity(intent);
                //closing drawer in main activity
                drawer.closeDrawers();
                return true;
            }
        });

        //fragment manager allows us to replace the current fragment displayed
        fragmentManager= getSupportFragmentManager();

        //replacing the frame as located in the activity_main layout resource file with the HomeFragment showing the home
        //fragment inside the frame
        fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
    }
}
