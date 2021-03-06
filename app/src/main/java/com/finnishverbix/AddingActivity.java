package com.finnishverbix;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.finnishverbix.SQL.SqliteHandler;

/**
 * This class is only for testing, class is used for adding the verb to the database
 */

public class AddingActivity extends ActionBarActivity {
    //Variables of the view
    Toolbar toolbar;
    Button saved;
    EditText edit2,edit3,edit4,edit5,edit6,edit7,edit8,edit9, edit10,edit11,edit12,edit13;

    //Variables of database
    SqliteHandler sqliteHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        //Set up the toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar_sub);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize the database and map the view.
        sqliteHandler = new SqliteHandler(this);
        edit2 = (EditText) findViewById(R.id.editText2);
        edit3 = (EditText) findViewById(R.id.editText3);
        edit4 = (EditText) findViewById(R.id.editText4);
        edit5 = (EditText) findViewById(R.id.editText5);
        edit6 = (EditText) findViewById(R.id.editText6);
        edit7 = (EditText) findViewById(R.id.editText7);
        edit8 = (EditText) findViewById(R.id.editText8);
        edit9 = (EditText) findViewById(R.id.editText9);
        edit10 = (EditText) findViewById(R.id.editText10);
        edit11 = (EditText) findViewById(R.id.editText11);
        edit12 = (EditText) findViewById(R.id.editText12);
        edit13 = (EditText) findViewById(R.id.editText13);

        //Initialize the "Save Button" and handle the click event.
        saved = (Button) findViewById(R.id.buttonAddNew);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get text from the edit text.
                String e2 = edit2.getText().toString();
                String e3 = edit3.getText().toString();
                String e4 = edit4.getText().toString();
                String e5 = edit5.getText().toString();
                String e6 = edit6.getText().toString();
                String e7 = edit7.getText().toString();
                String e8 = edit8.getText().toString();
                String e9 = edit9.getText().toString();
                String e10 = edit10.getText().toString();
                String e11 = edit11.getText().toString();
                String e12 = edit12.getText().toString();
                String e13 = edit13.getText().toString();

                //Create a Query.
                String insertQuery = "INSERT INTO FINNISH_WORDS(Verb,Meaning,Type,Present,Perfect," +
                        "Imperfect,Pluperfect,Potential," +
                        "PotentialPerfect,Conditional,Infinitive2,Infinitive3) values ('"
                        + e2 + "','"
                        + e3 + "','"
                        + e4 + "','"
                        + e5 + "','"
                        + e6 + "','"
                        + e7 + "','"
                        + e8 + "','"
                        + e9 + "','"
                        + e10 + "','"
                        + e11 + "','"
                        + e12 + "','"
                        + e13 + "')";

                sqliteHandler.executeQuery(insertQuery);
                //Reset the Edit Texts
                edit2.setText("");
                edit3.setText("");
                edit4.setText("");
                edit5.setText("");
                edit6.setText("");
                edit7.setText("");
                edit8.setText("");
                edit9.setText("");
                edit10.setText("");
                edit11.setText("");
                edit12.setText("");
                edit13.setText("");

                //Notify user of suceeding
                Toast.makeText(getApplicationContext(),"Success Adding",Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adding, menu);
        return true;
    }


    /**
     * Handling the Home/Up button for turning back to the main activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id ==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
