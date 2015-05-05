package com.finnishverbix.FavoriteFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.finnishverbix.R;

import java.util.Collections;
import java.util.List;

/**
 * A Recycler Adapter for Favorite Fragment Recycler View.
 */
public class FavoriteRecycleAdapter extends RecyclerView.Adapter<FavoriteRecycleAdapter.MyFavoriteViewHolder> {

    private LayoutInflater inflater;
    List<WordItem> wordItems = Collections.emptyList(); // to make sure the list is empty
    Context context;

    //CONSTRUCTOR
    public FavoriteRecycleAdapter( Context context,List<WordItem> wordItems) {
        inflater = LayoutInflater.from(context);
        this.wordItems = wordItems;
        this.context = context;
    }

    //VIEW HOLDER TO INFLATE THE ITEMS TO THE VIEW
    @Override
    public MyFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_item = inflater.inflate(R.layout.favorite_list_item,parent,false);
        MyFavoriteViewHolder holder = new MyFavoriteViewHolder(view_item);

        return holder;
    }

    //BIND THe ITEM TO THE VIEW
    @Override
    public void onBindViewHolder(MyFavoriteViewHolder holder, int position) {
        WordItem currentItem = wordItems.get(position);
        holder.title.setText(currentItem.getVerb());
        holder.meaning.setText(currentItem.getMeaning());
        holder.verbtype.setText(currentItem.getType());
    }

    @Override
    public int getItemCount() {
        return wordItems.size();
    }

    //subclass to process the items.
    public class MyFavoriteViewHolder extends  RecyclerView.ViewHolder {
        TextView title;
        TextView meaning;
        TextView verbtype;

        public MyFavoriteViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textViewListHeader);
            meaning = (TextView) itemView.findViewById(R.id.textViewListMeaning);
            verbtype = (TextView) itemView.findViewById(R.id.textViewListVerbType);

        }


    }

}



