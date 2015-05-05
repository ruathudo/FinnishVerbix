package com.finnishverbix;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.finnishverbix.FavoriteFragment.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reviewing word when click to an item from the Favorite Menu.
 */

public class WordReviewActivity extends ActionBarActivity {
    //Set Up the View.
    Toolbar toolbar;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;

    //Variable for making the expandable list view.
    List<String> listHeader;
    HashMap<String,String> listChild;

    //Variables from intent
    String verb,meaning,type,present,perfect,imperfect,pluperfect,potential,potentialperfect,conditional,infinitive2,infinitive3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_review);
        //Set up toolbar.
        toolbar = (Toolbar) findViewById(R.id.app_bar_sub2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Retrieving data from the intent.
        gettingDataFromIntent();
        //Set up expandable list view
        expandableListView = (ExpandableListView) findViewById(R.id.exapandableListView1);

        //SET HEADER LAYOUT
        TextView title = (TextView) findViewById(R.id.textViewReviewHeader);
        TextView mean = (TextView)findViewById(R.id.textViewMeaning2);
        TextView verbtype = (TextView)findViewById(R.id.textViewVerbtype2);
        title.setText(verb);
        mean.setText(meaning);
        verbtype.setText(type);

        //Make the expandable list
        prepareList();
        expandableListAdapter = new CustomExpandableListAdapter(this,listHeader,listChild);
        expandableListView.setAdapter(expandableListAdapter);


    }

    /**
     * prepare the list by adding the header of the list
     * adding the data from the intent.
     */
    private void prepareList() {
        listHeader = new ArrayList<String>();
        listChild = new HashMap<String,String>();

        listHeader.add("Present");
        listHeader.add("Perfect");
        listHeader.add("Imperfect");
        listHeader.add("Pluperfect");
        listHeader.add("Potential");
        listHeader.add("PotentialPerfect");
        listHeader.add("Conditional");
        listHeader.add("Infinitive2");
        listHeader.add("Infinitive3");

        listChild.put(listHeader.get(0), present);
        listChild.put(listHeader.get(1), perfect);
        listChild.put(listHeader.get(2), imperfect);
        listChild.put(listHeader.get(3), pluperfect);
        listChild.put(listHeader.get(4), potential);
        listChild.put(listHeader.get(5), potentialperfect);
        listChild.put(listHeader.get(6), conditional);
        listChild.put(listHeader.get(7), infinitive2);
        listChild.put(listHeader.get(8),infinitive3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id ==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Receiving the data from intent and add them to some initialized variables.
     */
    public void gettingDataFromIntent() {
        Intent intent = getIntent();
        verb = intent.getStringExtra("Verb");
        meaning = intent.getStringExtra("Meaning");
        type = intent.getStringExtra("Type");
        present = intent.getStringExtra("Present");
        perfect = intent.getStringExtra("Perfect");
        imperfect = intent.getStringExtra("Imperfect");
        pluperfect= intent.getStringExtra("Pluperfect");
        potential = intent.getStringExtra("Potential");
        potentialperfect = intent.getStringExtra("PotentialPerfect");
        conditional = intent.getStringExtra("Conditional");
        infinitive2 = intent.getStringExtra("Infinitive2");
        infinitive3 = intent.getStringExtra("Infinitive3");
        Log.d("RECEIVE", present);
    }
}
