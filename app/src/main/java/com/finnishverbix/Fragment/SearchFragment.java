package com.finnishverbix.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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

import com.finnishverbix.FavoriteFragment.CustomExpandableListAdapter;
import com.finnishverbix.FavoriteFragment.WordItem;
import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;
import com.gc.materialdesign.views.ButtonRectangle;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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
        btnSearch = (ButtonRectangle) rootView.findViewById(R.id.buttonSearch);
        btnSave = (FloatingActionButton) rootView.findViewById(R.id.fabSave);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.exapandableListView);
        edtTextSearch = (EditText) rootView.findViewById(R.id.editText);
        RelativeLayout searchContainer = (RelativeLayout) rootView.findViewById(R.id.searchContainer);
        searchContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (edtTextSearch.isFocused()) {
                        Rect outRect = new Rect();
                        edtTextSearch.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            edtTextSearch.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //CREATE SCHOWCASE VIEW
        SharedPreferences settings = rootView.getContext().getSharedPreferences(PREFS_NAME,0);
        if(settings.getBoolean("my_first_time",true)) {
            createShowCaseView();

            settings.edit().putBoolean("my_first_time", false).commit();
        }


    }

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
