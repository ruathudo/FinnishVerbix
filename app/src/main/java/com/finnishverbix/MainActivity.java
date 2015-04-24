package com.finnishverbix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.finnishverbix.NagvigationDrawer.NavigationDrawerFragment;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;

    //Create an boardcast daily notification
    AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;



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
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer1, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        if(savedInstanceState == null){
            navigationDrawerFragment.selectItem(0);
        }

        setAlarm();

    }

    public void setAlarm(){


        Calendar alarmStartTime =  Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 7);
        alarmStartTime.set(Calendar.MINUTE,39 );
        alarmStartTime.set(Calendar.SECOND, 0);
        alarmStartTime.set(Calendar.AM_PM, Calendar.AM);

        //alarmStartTime.set(Calendar.DAY_OF_MONTH, alarmStartTime.get(Calendar.DAY_OF_MONTH));
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), getInterval(),pendingIntent);
    }

    private int getInterval() {
        return 1* 24*60*60*1000; // day* hours * minutes* seconds * milliseconds
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
        if(id == R.id.add)
            startActivity(new Intent(this,AddingActivity.class));
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
