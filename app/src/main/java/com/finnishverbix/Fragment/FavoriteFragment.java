package com.finnishverbix.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.finnishverbix.FavoriteFragment.WordItem;
import com.finnishverbix.FavoriteFragment.WordListAdapter;
import com.finnishverbix.R;
import com.finnishverbix.SQL.SqliteHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    ListView listView;
    View rootView;
    SqliteHandler sqliteHandler;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewFavorite);
        sqliteHandler = new SqliteHandler(getActivity());
        showList();

        return rootView;
    }

    private void showList() {
        ArrayList<WordItem> wordList = new ArrayList<WordItem>();
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
        WordListAdapter wordListAdapter = new WordListAdapter(getActivity(),wordList);
        listView.setAdapter(wordListAdapter);
    }
}
