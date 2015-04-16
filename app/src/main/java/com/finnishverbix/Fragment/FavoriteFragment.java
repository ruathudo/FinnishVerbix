package com.finnishverbix.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.finnishverbix.FavoriteFragment.FavoriteRecycleAdapter;
import com.finnishverbix.FavoriteFragment.RecyclerItemClickListener;
import com.finnishverbix.FavoriteFragment.SwipeDismissRecyclerViewTouchListener;
import com.finnishverbix.FavoriteFragment.WordItem;
import com.finnishverbix.FavoriteFragment.WordListAdapter;
import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;
import com.finnishverbix.WordReviewActivity;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment  {


    View rootView;
    SqliteHandler sqliteHandler;
    ArrayList<WordItem> wordList;
    ProgressDialog mProgressDialog;

    WordListAdapter wordListAdapter;
    ListView listView;

    FavoriteRecycleAdapter favoriteRecycleAdapter;
    RecyclerView recyclerView;
    private int swipe_position;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView  = (RecyclerView) rootView.findViewById(R.id.containerWordFavorite);
       // listView = (ListView) rootView.findViewById(R.id.listViewFavorite);
        sqliteHandler = new SqliteHandler(getActivity());

        new QueryDatabase().execute();

        return rootView;
    }



    private class QueryDatabase extends AsyncTask<Void,Void,Void>{
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

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            favoriteRecycleAdapter = new FavoriteRecycleAdapter(getActivity(),wordList);
            recyclerView.setAdapter(favoriteRecycleAdapter);

            /*favoriteRecycleAdapter.setClickListener(new FavoriteRecycleAdapter.ClickListenerCardView() {
                @Override
                public void itemClicked(View view, int position) {
                    singleItemIntent(position);
                }
            });*/

            //Handle Swipe Dismiss
            SwipeDismissRecyclerViewTouchListener touchListener =
                    new SwipeDismissRecyclerViewTouchListener(recyclerView,
                            new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for(int position : reverseSortedPositions){
                                        swipe_position = position;
                                        Log.d("TOUCH ","TOUCH");
                                        ConfirmationDiaglogBeforeDelete();
                                    }
                                }
                            });
            //wordListAdapter = new WordListAdapter(getActivity(),wordList);
          //  listView.setAdapter(wordListAdapter);
            // Close the progressdialog
            recyclerView.setOnTouchListener(touchListener);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                    recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    singleItemIntent(position);
                }
            }));

            mProgressDialog.dismiss();
        }
    }

    private void ConfirmationDiaglogBeforeDelete() {
        WordItem item = wordList.get(swipe_position);
        final MaterialDialog delete_confirm_dialog;
        delete_confirm_dialog = new MaterialDialog(getActivity());
        delete_confirm_dialog.setTitle("DELETE")
        .setMessage("Are you sure to delete this card:" + item.getVerb() + " ?")
        .setPositiveButton("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_confirm_dialog.dismiss();
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
    private void showList() {
        wordList = new ArrayList<WordItem>();
        wordList.clear();;
        String query = "SELECT * FROM FINNISH_WORDS ";
        Cursor cursor = sqliteHandler.selectQuery(query);
        if(cursor!=null && cursor.getCount()!= 0){
            if(cursor.moveToFirst()){
                do{
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
                }while(cursor.moveToNext());
            }
        }
        cursor.close();

    }
}
