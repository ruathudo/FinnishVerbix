package com.finnishverbix.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.finnishverbix.FavoriteFragment.FavoriteRecycleAdapter;
import com.finnishverbix.FavoriteFragment.RecyclerItemClickListener;
import com.finnishverbix.FavoriteFragment.SwipeDismissRecyclerViewTouchListener;
import com.finnishverbix.FavoriteFragment.WordItem;

import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;
import com.finnishverbix.WordReviewActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Favorite Fragment Class.
 */
public class FavoriteFragment extends Fragment {


    View rootView;
    //Variable for handling the sqlite
    SqliteHandler sqliteHandler;
    ArrayList<WordItem> wordList;
    ProgressDialog mProgressDialog;

    //Variable for the Recycler view
    FavoriteRecycleAdapter favoriteRecycleAdapter;
    RecyclerView recyclerView;
    private int swipe_position;

    //Variable for show case view.
    final String PREFS_NAME = "MyPrefsFile";
    ShowcaseView sv;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        // Initialize variables.
        sqliteHandler = new SqliteHandler(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.containerWordFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //Run the listing task in the background
        new QueryDatabase().execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //Create showcase view
    private void createShowCaseView() {
        final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(android.widget.RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        //define targets
        recyclerView = (RecyclerView) rootView.findViewById(R.id.containerWordFavorite);

        //Create showcase view
        ViewTarget viewTarget = new ViewTarget(recyclerView);
        new ShowcaseView.Builder(getActivity(), true)
                .setTarget(viewTarget)
                .setContentTitle("Word Cards")
                .setContentText("Your favorite words are saved here." +
                        "\n Click to the item to see more, " +
                        "\n Swipe to delete the item")
                .setStyle(R.style.CustomShowcaseTheme3)
                .build().setButtonPosition(lps);
    }

    private class QueryDatabase extends AsyncTask<Void, Void, Void> {
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

            favoriteRecycleAdapter = new FavoriteRecycleAdapter(getActivity(), wordList);
            recyclerView.setAdapter(favoriteRecycleAdapter);

            //Handle Swipe Dismiss
            favoriteRecycleAdapter = new FavoriteRecycleAdapter(getActivity(), wordList);
            recyclerView.setAdapter(favoriteRecycleAdapter);
            SwipeDismissRecyclerViewTouchListener touchListener =
                    new SwipeDismissRecyclerViewTouchListener(
                            recyclerView,
                            new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        swipe_position = position;
                                        //Show the confirmation dialog
                                        ConfirmationDiaglogBeforeDelete();

                                    }
                                }
                            });
            //Handling the selecting item task
            recyclerView.setOnTouchListener(touchListener);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                    recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    singleItemIntent(position); // Create an intent to switch to WordReview Activity
                }
            }));
            //Save the status of the showcase so that it opens only once.
            SharedPreferences settings = rootView.getContext().getSharedPreferences(PREFS_NAME, 0);
            if (settings.getBoolean("my_first_time_favorite", true)) {
                createShowCaseView();
                settings.edit().putBoolean("my_first_time_favorite", false).commit();
            }


            mProgressDialog.dismiss();
        }
    }

    //Create the confirmation dialof for the swipe to delete.
    private void ConfirmationDiaglogBeforeDelete() {
        final WordItem item = wordList.get(swipe_position);
        final MaterialDialog delete_confirm_dialog;
        delete_confirm_dialog = new MaterialDialog(getActivity());
        delete_confirm_dialog.setTitle("DELETE")
                .setMessage("Are you sure to delete this card:" + item.getVerb() + " ?")
                .setPositiveButton("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String query = "DELETE FROM FINNISH_WORDS WHERE Verb = '" + item.getVerb() + "';";
                        sqliteHandler.executeQuery(query); //Delete in the Database
                        delete_confirm_dialog.dismiss(); // the confirm dialog disapear
                        wordList.remove(swipe_position); // remove the item from the list
                        favoriteRecycleAdapter.notifyDataSetChanged(); //notify the adapter for the change
                        Toast.makeText(rootView.getContext(), "Deleted", Toast.LENGTH_SHORT).show(); //Show the word has been deleted
                    }
                }).setNegativeButton("NO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_confirm_dialog.dismiss();
            }
        });
        delete_confirm_dialog.show();
    }


    private void singleItemIntent(int position) {
        //CREATE INTENT AND PUT EXTRASTRING TO THE SINGLE ITEM VIEW ACTIVIIES
        WordItem wordItem = wordList.get(position);
        Intent intent = new Intent(rootView.getContext(), WordReviewActivity.class);
        intent.putExtra("Verb", wordItem.getVerb());
        intent.putExtra("Meaning", wordItem.getMeaning());
        intent.putExtra("Type", wordItem.getType());
        intent.putExtra("Present", wordItem.getPresent());
        intent.putExtra("Perfect", wordItem.getPerfect());
        intent.putExtra("Imperfect", wordItem.getImperfect());
        intent.putExtra("Pluperfect", wordItem.getPluperfect());
        intent.putExtra("Potential", wordItem.getPotential());
        intent.putExtra("PotentialPerfect", wordItem.getPotentialperfect());
        intent.putExtra("Conditional", wordItem.getConditional());
        intent.putExtra("Infinitive2", wordItem.getInfinitive2());
        intent.putExtra("Infinitive3", wordItem.getInfinitive3());
        rootView.getContext().startActivity(intent);
    }

    //LISTING THW WORDS from the database
    private void showList() {
        wordList = new ArrayList<WordItem>();
        wordList.clear();
        ;
        String query = "SELECT * FROM FINNISH_WORDS ";
        Cursor cursor = sqliteHandler.selectQuery(query);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    WordItem WordItem = new WordItem();
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
                    wordList.add(WordItem);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

    }
}
