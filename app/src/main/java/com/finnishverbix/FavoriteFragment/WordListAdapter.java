package com.finnishverbix.FavoriteFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finnishverbix.R;
import com.finnishverbix.WordReviewActivity;

import java.util.ArrayList;

/**
 * A CLASS FOR TESTING, NOT IN USE ANYMORE
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
        final WordItem wordItem = wordItems.get(i);
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WordReviewActivity.class);
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
                context.startActivity(intent);
            }
        });

        return view;
    }
}
