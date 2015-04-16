package com.finnishverbix.Fragment;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.finnishverbix.FavoriteFragment.CustomExpandableListAdapter;
import com.finnishverbix.FavoriteFragment.WordItem;
import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    View rootView;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;
    List<String> listHeader;
    HashMap<String,String> listChild;

    FloatingActionButton btnSave;
    ButtonRectangle btnSearch;
    EditText edtTextSearch;
    private ProgressDialog mProgressDialog;
    SqliteHandler sqliteHandler;
    WordItem WordItem = null;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        btnSearch = (ButtonRectangle) rootView.findViewById(R.id.buttonSearch);
        btnSave = (FloatingActionButton) rootView.findViewById(R.id.fabSave);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.exapandableListView);
        edtTextSearch = (EditText) rootView.findViewById(R.id.editText);

        listHeader = new ArrayList<String>();
        listChild = new HashMap<String,String>();

        sqliteHandler = new SqliteHandler(rootView.getContext());
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Searching().execute();
            }
        });

        return rootView;
    }

    private class Searching extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Retrieving Data");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            showList();
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            if(WordItem != null){
                prepareList();

                TextView title = (TextView) rootView.findViewById(R.id.textViewResultHeader);
                TextView mean = (TextView)rootView.findViewById(R.id.textViewMeaning);
                TextView verbtype = (TextView)rootView.findViewById(R.id.textViewVerbtype);
                title.setText(WordItem.getVerb());
                mean.setText(WordItem.getMeaning());
                verbtype.setText(WordItem.getType());



                expandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(),listHeader,listChild);
                expandableListView.setAdapter(expandableListAdapter);

                edtTextSearch.setText("");
                btnSave.setVisibility(View.VISIBLE);
                btnSave.show();
            }
            else {
                TextView title = (TextView) rootView.findViewById(R.id.textViewResultHeader);
                title.setText("WORD NOT FOUND ! ");
                listChild.clear();
                listHeader.clear();
                expandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(),listHeader,listChild);
                expandableListView.setAdapter(expandableListAdapter);
                btnSave.setVisibility(View.VISIBLE);
                btnSave.hide();
            }
            mProgressDialog.dismiss();
        }
    }

    private void showList() {

        String query = "SELECT * FROM FINNISH_WORDS WHERE Verb = '"+edtTextSearch.getText().toString().toLowerCase() + "';";
        Cursor cursor = sqliteHandler.selectQuery(query);
        if(cursor!=null && cursor.getCount()!= 0) {
            if (cursor.moveToFirst()) {
                WordItem = new WordItem();
                WordItem.setVerb(cursor.getString(cursor.getColumnIndex("Verb")));
                WordItem.setMeaning(cursor.getString(cursor.getColumnIndex("Meaning")));
                WordItem.setType(cursor.getString(cursor.getColumnIndex("Type")));
                WordItem.setPresent(cursor.getString(cursor.getColumnIndex("Present")));
                WordItem.setPerfect(cursor.getString(cursor.getColumnIndex("Perfect")));
                WordItem.setImperfect(cursor.getString(cursor.getColumnIndex("Imperfect")));
                WordItem.setPluperfect(cursor.getString(cursor.getColumnIndex("Pluperfect")));
                WordItem.setPotential(cursor.getString(cursor.getColumnIndex("Potential")));
                WordItem.setPotentialperfect(cursor.getString(cursor.getColumnIndex("PotentialPerfect")));
                WordItem.setConditional(cursor.getString(cursor.getColumnIndex("Conditional")));
                WordItem.setInfinitive2(cursor.getString(cursor.getColumnIndex("Infinitive2")));
                WordItem.setInfinitive3(cursor.getString(cursor.getColumnIndex("Infinitive3")));
                //DO EXPANDABLE VIEW;
            }
        }else WordItem = null;
        cursor.close();
    }
    private void prepareList() {


        listHeader.add("Present");
        listHeader.add("Perfect");
        listHeader.add("Imperfect");
        listHeader.add("Pluperfect");
        listHeader.add("Potential");
        listHeader.add("PotentialPerfect");
        listHeader.add("Conditional");
        listHeader.add("Infinitive2");
        listHeader.add("Infinitive3");

        listChild.put(listHeader.get(0), WordItem.getPresent());
        listChild.put(listHeader.get(1), WordItem.getPerfect());
        listChild.put(listHeader.get(2), WordItem.getImperfect());
        listChild.put(listHeader.get(3), WordItem.getPluperfect());
        listChild.put(listHeader.get(4), WordItem.getPotential());
        listChild.put(listHeader.get(5), WordItem.getPotentialperfect());
        listChild.put(listHeader.get(6), WordItem.getConditional());
        listChild.put(listHeader.get(7), WordItem.getInfinitive2());
        listChild.put(listHeader.get(8), WordItem.getInfinitive3());

    }


}
