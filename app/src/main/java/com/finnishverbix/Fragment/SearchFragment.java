package com.finnishverbix.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finnishverbix.FavoriteFragment.CustomExpandableListAdapter;
import com.finnishverbix.FavoriteFragment.WordItem;
import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;
import com.finnishverbix.SearchFragmentSupplement.JSONfuntions;
import com.gc.materialdesign.views.ButtonRectangle;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Search Fragment Class - The main fragment
 */
public class SearchFragment extends Fragment {

    View rootView;
    //Expandable list view variables
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;
    List<String> listHeader;
    HashMap<String, String> listChild;

    //Searching View variables
    FloatingActionButton btnSave;
    ButtonRectangle btnSearch;
    EditText edtTextSearch;

    //Searching handler variables.
    private ProgressDialog mProgressDialog;
    SqliteHandler sqliteHandler;
    WordItem WordItem = null;
    boolean wordFoundFromServer = false;
    boolean wordFoundFromDB = false;
    String e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13;
    boolean hasNetworkConnection = true;

    final String PREFS_NAME = "MyPrefsFile";

    ShowcaseView sv;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        //Initialize some items in the view
        btnSearch = (ButtonRectangle) rootView.findViewById(R.id.buttonSearch);
        btnSave = (FloatingActionButton) rootView.findViewById(R.id.fabSave);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.exapandableListView);
        edtTextSearch = (EditText) rootView.findViewById(R.id.editText);

        //For not focusing on the edit text.  -- not succeed.
        InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtTextSearch.getWindowToken(), 0);
        RelativeLayout searchContainer = (RelativeLayout) rootView.findViewById(R.id.searchContainer);

        //Inititialize list
        listHeader = new ArrayList<String>();
        listChild = new HashMap<String, String>();

        //Create sqlite handler
        sqliteHandler = new SqliteHandler(rootView.getContext());
        //Handle search button clicked
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Searching().execute();
            }
        });
        btnSave.setVisibility(View.VISIBLE);
        btnSave.show();
        //Handle saved button clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Saved button", "BUTTON CLICKED");
                //Running an insert query
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
                //Decide to add to the database or not
                // If the word is already in the database then NOT.
                if (wordFoundFromServer && !wordFoundFromDB) {
                    sqliteHandler.executeQuery(insertQuery);
                    Log.d("Saved button", "BUTTON CLICKED");
                    Toast.makeText(rootView.getContext(), "Success Adding", Toast.LENGTH_LONG).show();
                    wordFoundFromDB = true;
                    wordFoundFromServer = false;
                }
            }
        });


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //CREATE SCHOWCASE VIEW AND ONLY RUN ON THE FIRST TIME

        SharedPreferences settings = rootView.getContext().getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            createShowCaseView();

            settings.edit().putBoolean("my_first_time", false).commit();
        }


    }

    //SHOWCASE VIEW
    private void createShowCaseView() {
        //set the closing button
        final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        //define targets
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        btnSearch = (ButtonRectangle) rootView.findViewById(R.id.buttonSearch);
        ViewTarget target = new ViewTarget(btnSearch);
        //Create showcase view
        sv = new ShowcaseView.Builder(getActivity(), true)
                .setTarget(target)
                .setContentTitle("Button Search")
                .setContentText("Click here to find the search")
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        //Another showcase  view.
                        ViewTarget viewTarget = new ViewTarget(toolbar);
                        new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(viewTarget)
                                .setContentTitle("ToolBar")
                                .setContentText("Left icon is Menu , the right icon is Help")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .build().setButtonPosition(lps);
                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {
                    }
                })
                .build();
        sv.setButtonPosition(lps);
    }

    //Running the searching task in a differnt thread / background
    private class Searching extends AsyncTask<Void, Void, Void> {
        //Before running
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

        //When running
        @Override
        protected Void doInBackground(Void... voids) {
            showList();
            return null;
        }

        //After running
        @Override
        protected void onPostExecute(Void args) {
            if (WordItem != null) {
                prepareList();
                //Create View result
                TextView title = (TextView) rootView.findViewById(R.id.textViewResultHeader);
                TextView mean = (TextView) rootView.findViewById(R.id.textViewMeaning);
                TextView verbtype = (TextView) rootView.findViewById(R.id.textViewVerbtype);
                title.setText(WordItem.getVerb());
                mean.setText(WordItem.getMeaning());
                verbtype.setText(WordItem.getType());

                //Set expandable list
                expandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(), listHeader, listChild);
                expandableListView.setAdapter(expandableListAdapter);

                edtTextSearch.setText("");
                btnSave.setVisibility(View.VISIBLE);
                if (wordFoundFromDB) {
                    btnSave.hide();
                } else btnSave.show();
            } else { // IF Does not find the word then
                //Clear the previous result, show "WORD NOT FOUND"
                TextView title = (TextView) rootView.findViewById(R.id.textViewResultHeader);
                title.setText("WORD NOT FOUND ! ");
                listChild.clear();
                listHeader.clear();
                expandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(), listHeader, listChild);
                expandableListView.setAdapter(expandableListAdapter);
                btnSave.setVisibility(View.VISIBLE);
                btnSave.hide();

                if (!hasNetworkConnection)
                    Toast.makeText(rootView.getContext(), "No network connection, please check the network connection", Toast.LENGTH_LONG).show();

            }
            mProgressDialog.dismiss();
        }
    }

    //Searching
    private void showList() {

        //First search in the database
        String query = "SELECT * FROM FINNISH_WORDS WHERE Verb = '" + edtTextSearch.getText().toString().toLowerCase() + "';";
        Cursor cursor = sqliteHandler.selectQuery(query);
        if (cursor != null && cursor.getCount() != 0) {
            wordFoundFromDB = true;
            wordFoundFromServer = false;
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

        } else { // if not found then request from serveer
            if (isNetworkConnected()) { /// check the interneet connection first
                //Getting the json from the server
                JSONObject jsonObject = JSONfuntions.getJSONfromURL("http://finnishverbixapi-env.elasticbeanstalk.com/json?word=" + edtTextSearch.getText().toString().toLowerCase());
                if (!jsonObject.isNull("verb")) {
                    wordFoundFromServer = true;
                    wordFoundFromDB = false;
                    try {
                        //Extracting json and saving the content to WordItem
                        Log.d("Test JSON", "present " + jsonObject.get("verb").toString());
                        JSONObject active = jsonObject.getJSONObject("active");
                        JSONObject infinitive = jsonObject.getJSONObject("infinitive");
                        Log.d("Test JSON", "present " + active.get("present"));
                        WordItem = new WordItem();
                        WordItem.setVerb(jsonObject.get("verb").toString());
                        WordItem.setMeaning("");
                        WordItem.setType("");
                        WordItem.setPresent(active.get("present").toString());
                        WordItem.setPerfect(active.get("Perfect").toString());
                        WordItem.setImperfect(active.get("Imperfect").toString());
                        WordItem.setPluperfect(active.get("pluperfect").toString());
                        WordItem.setPotential(active.get("Potential").toString());
                        WordItem.setPotentialperfect(active.get("conditional_perfect").toString());
                        WordItem.setConditional(active.get("conditional").toString());
                        WordItem.setInfinitive2(infinitive.get("infinitive2").toString());
                        WordItem.setInfinitive3(infinitive.get("Infinitive3").toString());
                        wordFoundFromServer = true;
                        e2 = jsonObject.get("verb").toString();
                        e3 = "";
                        e4 = "";
                        e5 = active.get("present").toString();
                        e6 = active.get("Perfect").toString();
                        e7 = active.get("Imperfect").toString();
                        e8 = active.get("pluperfect").toString();
                        e9 = active.get("Potential").toString();
                        e10 = active.get("conditional_perfect").toString();
                        e11 = active.get("conditional").toString();
                        e12 = infinitive.get("infinitive2").toString();
                        e13 = infinitive.get("Infinitive3").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WordItem = null;
                    wordFoundFromServer = false;
                    wordFoundFromDB = false;
                }
            } else {
                WordItem = null;
                wordFoundFromServer = false;
                wordFoundFromDB = false;
                hasNetworkConnection = false;
            }
        }
        cursor.close();
    }

    //CHECK IF THERE IS INTERNET CONNECTION
    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) rootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    //PREPARE FOR THE EXPANDABLE LIST
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
