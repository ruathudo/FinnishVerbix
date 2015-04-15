package com.finnishverbix.FavoriteFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.finnishverbix.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by longtran on 4/15/2015.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    //DECLARE VARIABLE
    private Context context;
    private List<String> listHeader;
    private HashMap<String,String> listDataChild;

    public CustomExpandableListAdapter(Context context, List<String> Strings, HashMap<String, String> listDataChild){
        this.context = context;
        this.listHeader = Strings;
        this.listDataChild = listDataChild;
    }


    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return listHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return (String) listDataChild.get(this.listHeader.get(i));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }



    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerItem = (String) getGroup(i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_header,null);
        }
        TextView title = (TextView) view.findViewById(R.id.textViewTenseHeader);
        title.setText(headerItem);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i,i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_child,null);
        }
        TextView child = (TextView) view.findViewById(R.id.textViewListChild);
        child.setText(childText);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
