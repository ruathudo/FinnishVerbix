package com.finnishverbix;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.finnishverbix.NagvigationDrawer.NavigationDrawerFragment;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;

    ShowcaseView sv;

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
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);


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
