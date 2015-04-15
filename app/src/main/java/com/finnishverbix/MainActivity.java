package com.finnishverbix;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.finnishverbix.NagvigationDrawer.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SET UP THE TOOL BAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Finnish Verbix");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ADD NAVIGATION DRAWER
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer1);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer1,(DrawerLayout) findViewById(R.id.drawer_layout),toolbar);
        if(savedInstanceState == null){
            navigationDrawerFragment.selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.navigate)
            navigationDrawerFragment.selectItem(2);
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}