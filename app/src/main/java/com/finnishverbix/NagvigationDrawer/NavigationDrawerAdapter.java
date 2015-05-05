package com.finnishverbix.NagvigationDrawer;

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
 * Adapter of navigation drawer
 * Include : the list of navigation drawer item.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyCustomViewHolder> {


    private LayoutInflater inflater;
    List<NavigationDrawerItem> drawerItemList = Collections.emptyList();
    Context context;
    private ClickListener clickListener;

    //CONSTRUCTOR
    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> navigationDrawerItems){
        inflater = LayoutInflater.from(context);
        this.drawerItemList = navigationDrawerItems;
        this.context = context;
    }

    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate drawerItem
        //Display the item to the navigation drawer.
        View view_item = inflater.inflate(R.layout.navigation_drawer_item,parent,false);
        MyCustomViewHolder holder = new MyCustomViewHolder(view_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyCustomViewHolder holder, int position) {
        //Set each of the item in the navigation drawer.
        NavigationDrawerItem currentItem = drawerItemList.get(position);
        holder.icon.setImageResource(currentItem.iconId);
        holder.title.setText(currentItem.title);
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    //A subclass for setting the navigation drawer item
    public class MyCustomViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView title;
        ImageView icon;


        public MyCustomViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_item_title);
            icon = (ImageView) itemView.findViewById(R.id.drawer_item_icon);
            //Set the item clickable and handle the click
            itemView.setOnClickListener(this);
        }

        //Handling the click
        @Override
        public void onClick(View view) {
            if(clickListener !=null)
            {
                //Opening other fragment based on the position
                clickListener.itemClicked(view,getPosition());
            }
        }
    }
    //INTERFACE FOR SELECTING THE ITEM IN THE DRAWER.
    public interface ClickListener{
        public void itemClicked(View view , int position);


    }
}
