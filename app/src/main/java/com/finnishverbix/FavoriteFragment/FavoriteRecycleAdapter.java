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
 * Created by longtran on 4/16/2015.
 */
public class FavoriteRecycleAdapter extends RecyclerView.Adapter<FavoriteRecycleAdapter.MyFavoriteViewHolder> {

    ClickListenerCardView clickListener;
    private LayoutInflater inflater;
    List<WordItem> wordItems = Collections.emptyList(); // to make sure the list is empty
    Context context;

    public FavoriteRecycleAdapter( Context context,List<WordItem> wordItems) {
        inflater = LayoutInflater.from(context);
        this.wordItems = wordItems;
        this.context = context;
    }

    @Override
    public MyFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_item = inflater.inflate(R.layout.favorite_list_item,parent,false);
        MyFavoriteViewHolder holder = new MyFavoriteViewHolder(view_item);

        return holder;
    }

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

    public void setClickListener(ClickListenerCardView clickListener){
        this.clickListener = clickListener;

    }



    public class MyFavoriteViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView title;
        TextView meaning;
        TextView verbtype;

        public MyFavoriteViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textViewListHeader);
            meaning = (TextView) itemView.findViewById(R.id.textViewListMeaning);
            verbtype = (TextView) itemView.findViewById(R.id.textViewListVerbType);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener !=null)
            {
                clickListener.itemClicked(view,getPosition());
            }
        }

    }

    public interface ClickListenerCardView {
        public void itemClicked(View view, int position);
    }
}



