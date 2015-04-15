package com.finnishverbix.FavoriteFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finnishverbix.R;

import java.util.ArrayList;

/**
 * Created by longtran on 4/15/2015.
 */
public class WordListAdapter extends BaseAdapter {
    Context context;
    ArrayList<WordItem> wordItems;

    public WordListAdapter(Context context, ArrayList<WordItem> wordItems) {
        this.context = context;
        this.wordItems = wordItems;
    }

    @Override
    public int getCount() {
        return wordItems.size();
    }

    @Override
    public Object getItem(int i) {
        return wordItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        WordItem wordItem = wordItems.get(i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.favorite_list_item,null);

        }
        TextView header = (TextView) view.findViewById(R.id.textViewListHeader);
        TextView meaning = (TextView) view.findViewById(R.id.textViewListMeaning);
        TextView verbtype = (TextView) view.findViewById(R.id.textViewListVerbType);
        header.setText(wordItem.getVerb());
        meaning.setText(wordItem.getMeaning());
        verbtype.setText(wordItem.getType());
        return view;
    }
}
